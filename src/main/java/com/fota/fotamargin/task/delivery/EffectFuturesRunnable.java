package com.fota.fotamargin.task.delivery;

import com.fota.common.enums.RedisKeyEnum;
import com.fota.fotamargin.common.constant.RedisConstant;
import com.fota.fotamargin.common.enums.DeliveryEffectTypeEnum;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.manager.RedisManager;
import com.fota.margin.util.RedisUtils;
import com.fota.market.enums.KLineResolutionEnum;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.enums.ContractStatus;
import com.fota.trade.service.ContractCategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taoyuanming
 * Created on 2018/8/11
 * Description 上新
 */
public class EffectFuturesRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EffectFuturesRunnable.class);
    
    private static ContractCategoryService contractCategoryService = BeanUtil.getBean(ContractCategoryService.class);
    private static RedisManager redisManager = BeanUtil.getBean(RedisManager.class);

    private DeliveryEffectTypeEnum deliveryEffectTypeEnum;

    public EffectFuturesRunnable() {
        this.deliveryEffectTypeEnum = DeliveryEffectTypeEnum.AUTO;
    }

    public EffectFuturesRunnable(DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        this.deliveryEffectTypeEnum = deliveryEffectTypeEnum;
    }

    @Override
    public void run() {
        long start = TimeUtils.getTimeInMillis();
        LOGGER.info("Effect>>Execute EffectNextFuturesScheduledTask Starting, start:{}", TimeUtils.formatMillisecondsToString(start));
        LOGGER.info("Effect>>key:{}", RedisConstant.MARGIN_DELIVERY_JOB_DONE + TimeUtils.getDeliveryDateMillis());
        loop(System.currentTimeMillis() + 300000, false, System.currentTimeMillis() + 1800000);
        //删除key
        redisManager.deleteValue(RedisConstant.MARGIN_DELIVERY_JOB_DONE + TimeUtils.getDeliveryDateMillis());
        LOGGER.info("Effect>>Execute EffectNextFuturesScheduledTask Done, end:{}, elapse: {}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - start);


    }


    private void loop(long deadline, Boolean waitTimeOut, long exit) {
        Boolean deliveryDone = redisManager.getValue(RedisConstant.MARGIN_DELIVERY_JOB_DONE + TimeUtils.getDeliveryDateMillis()) != null;
        if (deliveryDone) {
            //存在表示交割完
            effect(deadline, waitTimeOut, exit, deliveryDone);
        } else {
            //不存在表示没有交割完
            if (waitTimeOut) {
                if (System.currentTimeMillis() > exit) {
                    LOGGER.error("Effect>> exit!");
                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LOGGER.error("InterruptedException:", e);
                }
                loop(deadline, waitTimeOut, exit);
            } else {
                if (System.currentTimeMillis() < deadline) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LOGGER.error("InterruptedException:", e);
                    }
                    loop(deadline, waitTimeOut, exit);
                } else {
                    LOGGER.info("Effect>> timeout!");
                    waitTimeOut = true;
                    effect(deadline, waitTimeOut, exit, deliveryDone);
                }
            }

        }
    }

    private void effect(long deadline, Boolean waitTimeOut, long exit, Boolean deliveryDone) {

        //获取交割中合约
        List<ContractCategoryDTO> deliveringContractDTOList = contractCategoryService.getContractByStatus(ContractStatus.DELIVERING.getCode());
        if (deliveringContractDTOList == null) {
            LOGGER.warn("Effect>>deliveringContractDTOList is empty!");
            deliveringContractDTOList = new ArrayList<>();
        }
        LOGGER.info("Effect>>deliveringContractDTOList:{}", deliveringContractDTOList);
        //获取待上新合约
        int i = 0;
        List<ContractCategoryDTO> unopenedContractDTOList;
        do {
            unopenedContractDTOList = contractCategoryService.getContractByStatus(ContractStatus.UNOPENED.getCode());
            i++;
        } while (CollectionUtils.isEmpty(unopenedContractDTOList) && i <= 2);
        if (unopenedContractDTOList == null) {
            LOGGER.warn("Effect>>EffectFuturesRunnable unopenedContractDTOList is empty!");
            unopenedContractDTOList = new ArrayList<>();
        }
        LOGGER.info("Effect>>EffectFuturesRunnable unopenedContractDTOList: {}", unopenedContractDTOList);

        //只更新和待上新合约相同标的物的交割中合约为已交割
        for (ContractCategoryDTO deliveringContract : deliveringContractDTOList) {
            for (ContractCategoryDTO unopenedContract : unopenedContractDTOList) {
                if (ObjectUtils.equals(unopenedContract.getAssetId(), deliveringContract.getAssetId()) && ObjectUtils.equals(unopenedContract.getContractType(), deliveringContract.getContractType())) {
                    contractCategoryService.updateContractStatus(deliveringContract.getId(), ContractStatus.DELIVERED);
                    saveKlineConnectedFromContract(unopenedContract.getId(), deliveringContract.getId());
                    LOGGER.info("Effect>>EffectFuturesRunnable deliveringContract updateContractStatus: {}", deliveringContract);
                }
            }
        }

        //上新待上新的合约
        // TODO: 2018/8/11  优化外部系统接口 减少调用次数
        for (ContractCategoryDTO contractCategoryDTO : unopenedContractDTOList) {
            contractCategoryService.updateContractStatus(contractCategoryDTO.getId(), ContractStatus.PROCESSING);
            LOGGER.info("Effect>>EffectFuturesRunnable unopenedContractDTO updateContractStatus: {}", contractCategoryDTO);
        }

        //刷新market缓存的合约数据该key包括 交易中的和交割中的合约
        redisManager.deleteValue(RedisKeyEnum.MARGIN_ACTIVE_CONTRACT.getKey());
        //该key只包括交易中合约
        redisManager.deleteValue(RedisKeyEnum.MARGIN_TRADING_CONTRACT.getKey());

        //删除行情23小时合约usdtK线
        String key = com.fota.market.util.RedisUtils.getCotr23hoursKLineRedisKeyForWeb(TimeUtils.get23Hour());
        redisManager.deleteValue(key);
        LOGGER.info("Effect>>EffectFuturesRunnable delete Cotr23hoursKLineRedisKey: {}", key);



        if (waitTimeOut && !deliveryDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException:", e);
            }
            loop(deadline, waitTimeOut, exit);
        }
    }

    /**
     * 交割上新后新合约和上一期合约关联，用与交割上新后K线衔接
     * @param newContractId
     * @param precedingContractId
     */
    private void saveKlineConnectedFromContract(Long newContractId, Long precedingContractId) {
        String key;
        for (KLineResolutionEnum kLineResolutionEnum : KLineResolutionEnum.values()) {
            key = RedisUtils.getKLineConnectedRedisKey(newContractId, kLineResolutionEnum.getCode());
            redisManager.saveValue(key, String.valueOf(precedingContractId));
            LOGGER.info("Effect>>saveKlineConnectedFromContract, key:{}, value:{}", key, precedingContractId);
        }
    }

}
