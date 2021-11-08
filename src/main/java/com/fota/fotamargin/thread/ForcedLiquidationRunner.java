package com.fota.fotamargin.thread;

import com.alibaba.fastjson.JSON;
import com.fota.asset.domain.UserContractDTO;
import com.fota.asset.service.AssetService;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.job.ForcedLiquidationJobInit;
import com.fota.fotamargin.job.JobThreadPool;
import com.fota.fotamargin.manager.AssetManager;
import com.fota.fotamargin.manager.MarginManager;
import com.fota.fotamargin.manager.log.ForcedLog;
import com.fota.fotamargin.manager.trade.ContractCategoryManager;
import com.fota.ticker.entrust.entity.CompetitorsPriceDTO;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.enums.ContractStatus;
import com.fota.trade.service.ContractCategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author taoyuanming
 * Created on 2018/8/2
 * Description 爆仓
 */
public class ForcedLiquidationRunner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForcedLiquidationRunner.class);
    private static MarginManager marginManager = BeanUtil.getBean(MarginManager.class);
    private static AssetManager assetManager = BeanUtil.getBean(AssetManager.class);
    private static ContractCategoryManager contractCategoryManager = BeanUtil.getBean(ContractCategoryManager.class);

    private int shardingItem;
    private int shardingTotalCount;
    public ForcedLiquidationRunner(int shardingItem, int shardingTotalCount) {
        this.shardingItem = shardingItem;
        this.shardingTotalCount = shardingTotalCount;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        //获取所有未交割的合约
        Map<Long, ContractCategoryDTO> ContractCategoryMap = new HashMap<>();
        List<ContractCategoryDTO> contractCategoryDTOList = null;
        try {
            //只查询交易中的合约，不包括交割中的合约
            contractCategoryDTOList = contractCategoryManager.getContractByStatus(ContractStatus.PROCESSING.getCode());
        } catch (Exception e) {
            LOGGER.error("contractCategoryService.getContractByStatus exception!", e);
            return;
        }
        if (CollectionUtils.isEmpty(contractCategoryDTOList)) {
            LOGGER.error("contractCategoryService.contractCategoryService return empty!");
            return;
        }
        for (ContractCategoryDTO contractCategoryDTO : contractCategoryDTOList) {
            //回滚中则停止爆仓检测 已在检测爆仓此时进行回滚 回滚前在elastic-job控制台停止爆仓检测任务
            ContractCategoryMap.put(contractCategoryDTO.getId(), contractCategoryDTO);
        }

        //获取合约最新价
        Map<String, BigDecimal> latestPriceMap = null;
        try {
            latestPriceMap = marginManager.getLatestPriceMap(contractCategoryDTOList);
        } catch (Exception e) {
            LOGGER.error("getLatestPriceMap exception:", e);
            return;
        }
        if (MapUtils.isEmpty(latestPriceMap)) {
            LOGGER.error("latestPriceMap is empty!");
            return;
        }
        ForcedLog.forcedLog("forcedLiquidation>>ForcedLiquidationRunner: latestPriceMap:{} contractCategoryDTOList:{}", JSON.toJSONString(latestPriceMap), JSON.toJSONString(contractCategoryDTOList));

//        List<CompetitorsPriceDTO> competitorsPriceDTOList = null;
//        try {
//            competitorsPriceDTOList = realTimeEntrust.getContractCompetitorsPrice();
//        } catch (Exception e) {
//            LOGGER.error("realTimeEntrust.getCompetitorsPrice exception!", e);
//            e.printStackTrace();
//            return;
//        }
//        if (CollectionUtils.isEmpty(competitorsPriceDTOList)) {
//            LOGGER.error("realTimeEntrust.getCompetitorsPrice return empty!");
//            return;
//        }
//
//        Map<String, BigDecimal> latestPriceMap = new HashMap<>();
//        for (CompetitorsPriceDTO competitorsPriceDTO : competitorsPriceDTOList) {
//            //type=2:合约 id:合约id orderDirection:1卖 2买
//            if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.SELL.getCode() == competitorsPriceDTO.getOrderDirection()) {
//                latestPriceMap.put(competitorsPriceDTO.getId() + com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.SELL.getName(), competitorsPriceDTO.getPrice());
//            } else if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.BUY.getCode() == competitorsPriceDTO.getOrderDirection()) {
//                latestPriceMap.put(competitorsPriceDTO.getId() + com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.BUY.getName(), competitorsPriceDTO.getPrice());
//            } else {
//                LOGGER.error("competitorsPriceDTO orderDirection unknown，competitorsPriceDTO:{}", competitorsPriceDTO);
//            }
//        }

//        //获取所有合约账户
//        List<UserContractDTO> allUserContractDTOList = null;
//        try {
//            allUserContractDTOList = assetService.getAllContractAccount();
//        } catch (Exception e) {
//            LOGGER.error("assetService.getAllContractAccount exception!", e);
//            e.printStackTrace();
//            return;
//        }
//        if (CollectionUtils.isEmpty(allUserContractDTOList)) {
//            LOGGER.error("assetService.getAllContractAccount return empty!");
//            return;
//        }
//
//        // TODO: 2018/8/23  优化
//        // 不同环境任务分组不同，每个任务分片处理部分用户
//        allUserContractDTOList = getShardingUsers(allUserContractDTOList);
//        if (CollectionUtils.isEmpty(allUserContractDTOList)) {
//            LOGGER.error("assetService.getAllContractAccount return empty!");
//            return;
//        }

        List<Long> shardingUserIdList = assetManager.getUserIdBySharding(shardingTotalCount, shardingItem);
        if (CollectionUtils.isEmpty(shardingUserIdList)) {
            LOGGER.error("assetManager.getUserIdBySharding return empty!");
            return;
        }

        // 用户分批处理
        Map<Integer, List<Long>> userlistMap = new HashMap<>();
        int size = shardingUserIdList.size();
        int j = ThreadPool.CORE_POOL_SIZE - 1;
        if (size <= j) {
            userlistMap.put(1, shardingUserIdList);
        } else {
            int div = size / j;
            int rem = size % j;

            int i = 1;
            for (; i <= j; i++) {
                userlistMap.put(i, shardingUserIdList.subList((i - 1) * div, i * div));
            }
            if (rem != 0) {
                userlistMap.put(i, shardingUserIdList.subList((i - 1) * div, (i - 1) * div + rem));
            }
        }

        CountDownLatch countDownLatch = new CountDownLatch(userlistMap.size());
        for (Map.Entry<Integer, List<Long>> entry : userlistMap.entrySet()) {
            ThreadPool.execute(new BatchForcedLiquidationRunner(entry.getValue(), countDownLatch, latestPriceMap, ContractCategoryMap));
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("ForcedLiquidationRunner Error !", e);
        }
        ForcedLog.forcedLog("ForcedLiquidationRunner>>pollElapse:{}", System.currentTimeMillis() - start);
    }


    /**
     * 用户分片
     * @param allUserContractDTOList
     * @return
     */
    public List<UserContractDTO> getShardingUsers(List<UserContractDTO> allUserContractDTOList) {
        List<UserContractDTO> shardingUsers = new ArrayList<>();
        if (CollectionUtils.isEmpty(allUserContractDTOList)) {
            return shardingUsers;
        }
        for (UserContractDTO userContractDTO : allUserContractDTOList) {
            if (null == userContractDTO.getUserId()) {
                LOGGER.error("userId is empty, UserContractDTO:{}", userContractDTO);
                continue;
            }
            if (userContractDTO.getUserId() % ForcedLiquidationJobInit.SHARDING_TOTAL_COUNT == this.shardingItem) {
                shardingUsers.add(userContractDTO);
            }
        }
        return shardingUsers;
    }

//
//    public List<UserContractDTO> testFilter(List<UserContractDTO> userContractDTOS) {
//        List<Long> users = new ArrayList<>();
//        users.add(17764593889L);
//        List<UserContractDTO> allUserContractDTOList2 = new ArrayList<>();
//        for (UserContractDTO userContractDTO : userContractDTOS) {
//            for (Long userId : users) {
//                if (userId.equals(userContractDTO.getUserId())) {
//                    allUserContractDTOList2.add(userContractDTO);
//                }
//            }
//        }
//        return allUserContractDTOList2;
//    }
}
