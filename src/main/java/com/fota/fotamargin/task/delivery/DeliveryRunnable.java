package com.fota.fotamargin.task.delivery;

import com.alibaba.fastjson.JSON;
import com.fota.common.enums.RedisKeyEnum;
import com.fota.fotamargin.common.constant.RedisConstant;
import com.fota.fotamargin.common.enums.*;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.delivery.DeliveryChildThreadPool;
import com.fota.fotamargin.delivery.PenetrateRunnable;
import com.fota.fotamargin.manager.MarginManager;
import com.fota.fotamargin.manager.RedisManager;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.enums.ContractStatus;
import com.fota.trade.service.ContractCategoryService;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author taoyuanming
 * Created on 2018/8/7
 * Description 交割
 */
public class DeliveryRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryRunnable.class);

    private static ContractCategoryService contractCategoryService = BeanUtil.getBean(ContractCategoryService.class);
    private static MarginManager marginManager = BeanUtil.getBean(MarginManager.class);
    private static RedisManager redisManager = BeanUtil.getBean(RedisManager.class);

    private static ThreadPoolExecutor deliveryThreadPool = new ThreadPoolExecutor(6, 8,
            1000L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(64), new deliveryThreadFactory());

    private List<Long> contractIdList;
    private DeliveryEffectTypeEnum deliveryEffectTypeEnum;

    public DeliveryRunnable() {
        this.deliveryEffectTypeEnum = DeliveryEffectTypeEnum.AUTO;
    }

    public DeliveryRunnable(List<Long> contractIdList, DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        this.contractIdList = contractIdList;
        this.deliveryEffectTypeEnum = deliveryEffectTypeEnum;
    }

    @Override
    public void run() {
        if (!DeliveryEffectTypeEnum.AUTO.equals(deliveryEffectTypeEnum)) {
            if (CollectionUtils.isNotEmpty(contractIdList)) {
                long start = TimeUtils.getTimeInMillis();
                LOGGER.info("Delivery>>ArtificialDelivery>>Execute DeliveryFuturesScheduledTask Starting, start:{}", TimeUtils.formatMillisecondsToString(start));
                List<ContractCategoryDTO> deliveryContractList = new ArrayList<>();
                ContractCategoryDTO contractCategoryDTO;
                for (Long contractId : contractIdList) {
                    try {
                        contractCategoryDTO = contractCategoryService.getContractById(contractId);
                        if (contractCategoryDTO != null) {
                            deliveryContractList.add(contractCategoryDTO);
                        } else {
                            LOGGER.error("Delivery>>ArtificialDelivery>>contractCategoryService.getContractById return null, contractId:{}", contractId);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Delivery>>ArtificialDelivery>>contractCategoryService.getContractById exception, contractId:{}", contractId);
                    }
                }
                if (CollectionUtils.isEmpty(deliveryContractList)) {
                    LOGGER.error("Delivery>>ArtificialDelivery>>DeliveryRunnable deliveryContractList is empty!");
                    return;
                }
                Integer status = ContractStatus.PROCESSING.getCode();
                deliveryContractList = deliveryContractList.stream().filter(deliveryContract -> status.equals(deliveryContract.getStatus())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(deliveryContractList)) {
                    LOGGER.error("Delivery>>ArtificialDelivery>>DeliveryRunnable deliveryContractList is empty after filter!");
                    return;
                }
                LOGGER.info("Delivery>>ArtificialDelivery>>DeliveryRunnable deliveryContractList: {}", deliveryContractList);
                delivery(deliveryContractList, null, deliveryEffectTypeEnum);
                LOGGER.info("Delivery>>ArtificialDelivery>>Execute DeliveryFuturesScheduledTask Done, end:{}, elapse:{}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - start);
            }
            return;
        }
        long start = TimeUtils.getTimeInMillis();
        LOGGER.info("Delivery>>Execute DeliveryFuturesScheduledTask Starting, start:{}", TimeUtils.formatMillisecondsToString(start));

        Long deliveryMillis = TimeUtils.getDeliveryMillis();
        //按交割时间获取未交割的合约
        int i = 0;
        List<ContractCategoryDTO> contractCategoryDTOList = null;
        do {
            try {
                contractCategoryDTOList = contractCategoryService.getContractByStatus(ContractStatus.PROCESSING.getCode());
            } catch (Exception e) {
                LOGGER.error("Delivery>>contractCategoryService.getContractByStatus exception!", e);
                return;
            }
            i++;
        } while (CollectionUtils.isEmpty(contractCategoryDTOList) && i <= 2);
        if (CollectionUtils.isEmpty(contractCategoryDTOList)) {
            LOGGER.error("Delivery>>DeliveryRunnable contractCategoryDTOList empty!");
            return;
        }
        LOGGER.info("Delivery>>DeliveryRunnable contractCategoryDTOList: {}", contractCategoryDTOList);
        LOGGER.info("Delivery>>deliveryMillis:{}", deliveryMillis);

        List<ContractCategoryDTO> deliveryContractList = new ArrayList<>();
        for (ContractCategoryDTO contractCategoryDTO : contractCategoryDTOList) {
            //按交割时间区别
            if (deliveryMillis.compareTo(TimeUtils.getDeliveryMillis(contractCategoryDTO.getDeliveryDate())) == 0) {
                deliveryContractList.add(contractCategoryDTO);
            }
        }
        if (CollectionUtils.isEmpty(deliveryContractList)) {
            LOGGER.error("Delivery>>DeliveryRunnable deliveryContractList empty!");
            return;
        }
        LOGGER.info("Delivery>>DeliveryRunnable deliveryContractList: {}", deliveryContractList);

        delivery(deliveryContractList, null, deliveryEffectTypeEnum);

        //刷新market缓存的合约数据
//        redisManager.deleteValue("MARKET_ALL_ACTIVE_CONTRACT");
        LOGGER.info("Delivery>>Execute DeliveryFuturesScheduledTask Done, end:{}, elapse:{}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - start);
    }

    /**
     * 初始化新合约
     * @param oldContract
     * @return
     */
    public static ContractCategoryDTO generateContract(ContractCategoryDTO oldContract, DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        if (DeliveryEffectTypeEnum.ARTIFICIALITY_SIMILAR_AUTO.equals(deliveryEffectTypeEnum)) {
            ContractCategoryDTO newContract = new ContractCategoryDTO();
            newContract.setId(null);
            newContract.setContractName(oldContract.getContractName());
            newContract.setAssetId(oldContract.getAssetId());
            newContract.setAssetName(oldContract.getAssetName());
            newContract.setStatus(ContractStatus.UNOPENED.getCode());
            newContract.setTotalAmount(oldContract.getTotalAmount());
            newContract.setUnfilledAmount(oldContract.getTotalAmount());
            newContract.setDeliveryDate(oldContract.getDeliveryDate());
            newContract.setContractType(oldContract.getContractType());
            newContract.setGmtCreate(new Date());
            newContract.setGmtModified(new Date());
            return newContract;
        }

        long nextDeliveryMillis = TimeUtils.calculateNextDeliveryMillis(oldContract.getDeliveryDate(), oldContract.getContractType());
        ContractCategoryDTO newContract = new ContractCategoryDTO();
        newContract.setId(null);
        newContract.setContractName(oldContract.getAssetName().toUpperCase() + TimeUtils.getDeliveryDateStr(nextDeliveryMillis));
        newContract.setAssetId(oldContract.getAssetId());
        newContract.setAssetName(oldContract.getAssetName());
        newContract.setStatus(ContractStatus.UNOPENED.getCode());
        newContract.setTotalAmount(oldContract.getTotalAmount());
        newContract.setUnfilledAmount(oldContract.getTotalAmount());
        newContract.setDeliveryDate(nextDeliveryMillis);
        newContract.setContractType(oldContract.getContractType());
        newContract.setGmtCreate(new Date());
        newContract.setGmtModified(new Date());
//        newContract.setContractSize(oldContract.getContractSize());
        return newContract;
    }

    /**
     * 交割完初始化下一期合约
     * @param deliveryContract
     */
    public static void initNewContract(ContractCategoryDTO deliveryContract, DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        contractCategoryService.saveContract(generateContract(deliveryContract, deliveryEffectTypeEnum));
    }

    /**
     * 合约交割
     * @param deliveryContractList
     */
    public static void delivery(List<ContractCategoryDTO> deliveryContractList, Long deliveryTime, DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        if (CollectionUtils.isEmpty(deliveryContractList)) {
            return;
        }
        //获取此时标的物的交割指数
        Map<String, BigDecimal> deliveryIndexList;
        //重试两次
        int j = 0;
        do {
            deliveryIndexList = marginManager.getDeliveryIndex(deliveryTime);
            j++;
        } while (MapUtils.isEmpty(deliveryIndexList) && j <= 2);
        if (MapUtils.isEmpty(deliveryIndexList)) {
            LOGGER.error("Delivery>>DeliveryRunnable deliveryIndexList empty!");
            return;
        }
        LOGGER.info("Delivery>>DeliveryRunnable deliveryIndexList:{}", JSON.toJSONString(deliveryIndexList));
//        //获取此时标的物的交割指数
//        List<DeliveryIndexDTO> deliveryIndexDTOList;
//        //重试两次
//        int j = 0;
//        do {
//            if (deliveryTime == null) {
//                // TODO: 2018/10/16
//                deliveryIndexDTOList = deliveryIndexService.getDeliveryIndex();
//            } else {
//                //根据回滚时间获取交割指数
//                deliveryIndexDTOList = deliveryIndexService.getDeliveryIndexByRealTime(TimeUtils.getDeliveryMillisForIndex(deliveryTime));
//            }
//            j++;
//        } while (CollectionUtils.isEmpty(deliveryIndexDTOList) && j <= 2);
//        if (CollectionUtils.isEmpty(deliveryIndexDTOList)) {
//            LOGGER.error("Delivery>>DeliveryRunnable deliveryIndexDTOList empty!");
//            return;
//        }
//        LOGGER.info("Delivery>>DeliveryRunnable deliveryIndexDTOList: {}", deliveryIndexDTOList);

        for (ContractCategoryDTO deliveryContract : deliveryContractList) {
            //停止该合约挂单、撮合（包括强平单的撮合）;先更新为交割中
            contractCategoryService.updateContractStatus(deliveryContract.getId(), ContractStatus.DELIVERING);
        }
        redisManager.deleteValue(RedisKeyEnum.MARGIN_TRADING_CONTRACT.getKey());

        //本次交割的相关用户
        Set<Long> userIdList = Sets.newConcurrentHashSet();
        CountDownLatch countDownLatch = new CountDownLatch(deliveryContractList.size());

        for (ContractCategoryDTO deliveryContract : deliveryContractList) {
            deliveryThreadPool.execute(new DeliveryContractRunnable(deliveryContract, deliveryIndexList, userIdList, countDownLatch, deliveryEffectTypeEnum));
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("Delivery>> DeliveryContractRunnable InterruptedException !", e);
        }
        LOGGER.info("Delivery>>deliveryPosition done time:{}", System.currentTimeMillis());

        //设置redis key标志已交割完
        redisManager.saveValueIfAbsent(RedisConstant.MARGIN_DELIVERY_JOB_DONE + TimeUtils.getDeliveryDateMillis(), "true");

        //交割完判断用户是否穿仓
        //获取所有未交割的合约
        List<ContractCategoryDTO> contractCategoryDTOList =  contractCategoryService.getContractByStatus(ContractStatus.PROCESSING.getCode());
        if (CollectionUtils.isEmpty(contractCategoryDTOList)) {
            LOGGER.error("Delivery>>DeliveryRunnable calculateUserRights error, contractCategoryDTOList is empty!");
            return;
        }
        Map<String, BigDecimal> latestPriceMap = null;
        try {
            latestPriceMap = marginManager.getLatestPriceMap(contractCategoryDTOList);
        } catch (Exception e) {
            LOGGER.error("Delivery>>getLatestPriceMap Exception:", e);
            return;
        }
        if (MapUtils.isEmpty(latestPriceMap)) {
            LOGGER.error("Delivery>>latestPriceMap is empty!");
            return;
        }
        LOGGER.info("Delivery>>latestPriceMap:{}", JSON.toJSONString(latestPriceMap));

        Long[] userIdArr = userIdList.toArray(new Long[0]);
        Map<Integer, Long[]> userIdArrMap = new HashMap<>();
        int size = userIdArr.length;
        int count = DeliveryChildThreadPool.CORE_POOL_SIZE - 1;
        if (size <= count) {
            userIdArrMap.put(1, userIdArr);
        } else {
            int div = size / count;
            int rem = size % count;

            int i = 1;
            for (; i <= count; i++) {
                userIdArrMap.put(i, Arrays.copyOfRange(userIdArr, (i - 1) * div, i * div));
            }
            if (rem != 0) {
                userIdArrMap.put(i, Arrays.copyOfRange(userIdArr, (i - 1) * div, (i - 1) * div + rem));
            }
        }

        Set<Long> deliveringIdSet = marginManager.getDeliveringContractIds();
        CountDownLatch penetrateCountDownLatch = new CountDownLatch(userIdArrMap.size());
        for (Map.Entry<Integer, Long[]> entry : userIdArrMap.entrySet()) {
            DeliveryChildThreadPool.execute(new PenetrateRunnable(latestPriceMap, entry.getValue(), penetrateCountDownLatch, deliveringIdSet));
        }

        try {
            penetrateCountDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("Delivery>>PenetrateRunnable InterruptedException!", e);
        }
    }

    static class deliveryThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "delivery-thread-pool-" + id.addAndGet(1));
        }
    }

}
