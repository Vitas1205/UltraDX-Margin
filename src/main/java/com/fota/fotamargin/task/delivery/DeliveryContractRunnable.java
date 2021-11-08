package com.fota.fotamargin.task.delivery;

import com.fota.asset.service.ContractService;
import com.fota.fotamargin.common.enums.*;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.delivery.DeliveryChildThreadPool;
import com.fota.fotamargin.delivery.PositionRunnable;
import com.fota.fotamargin.manager.DeliveryManager;
import com.fota.fotamargin.manager.RedisManager;
import com.fota.fotamargin.manager.UserPositionManager;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.DeliveryCompletedDTO;
import com.fota.trade.domain.ResultCode;
import com.fota.trade.domain.UserPositionDTO;
import com.fota.trade.service.ContractOrderService;
import com.fota.trade.service.UserPositionService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.fota.fotamargin.task.delivery.DeliveryRunnable.initNewContract;

/**
 * @author taoyuanming
 * Created on 2018/8/7
 * Description 交割
 */
public class DeliveryContractRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryContractRunnable.class);
    private static ContractOrderService contractOrderService = BeanUtil.getBean(ContractOrderService.class);
    private static UserPositionManager userPositionManager = BeanUtil.getBean(UserPositionManager.class);

    private ContractCategoryDTO deliveryContract;
    Map<String, BigDecimal> deliveryIndexList;
    private Set<Long> userIdList;
    private CountDownLatch countDownLatch;
    private DeliveryEffectTypeEnum deliveryEffectTypeEnum;

    public DeliveryContractRunnable(ContractCategoryDTO deliveryContract, Map<String, BigDecimal> deliveryIndexList, Set<Long> userIdList, CountDownLatch countDownLatch, DeliveryEffectTypeEnum deliveryEffectTypeEnum) {
        this.deliveryContract = deliveryContract;
        this.deliveryIndexList = deliveryIndexList;
        this.userIdList = userIdList;
        this.countDownLatch = countDownLatch;
        this.deliveryEffectTypeEnum = deliveryEffectTypeEnum;
    }

    @Override
    public void run() {
        try {
            deliveryContract();
        } catch (Exception e) {
            LOGGER.error("Delivery>> DeliveryContractRunnable deliveryContract exception,", e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void deliveryContract() {
        List<UserPositionDTO> userPositionDTOList = null;
        BigDecimal index = deliveryIndexList.get(deliveryContract.getAssetName().toUpperCase());

        //交割指数错误不交割
        if (index == null) {
            LOGGER.error("Delivery>>DeliveryIndexDTO index error! deliveryIndexList:{}, deliveryContract:{}", deliveryIndexList, deliveryContract);
            return;
        }

        // 根据合约获取所有持仓
        userPositionDTOList = userPositionManager.listPositionByContractId(deliveryContract.getId());
        LOGGER.info("Delivery>>totalPosition:{}, contract:{}", userPositionDTOList.size(), deliveryContract.getContractName());
        if (CollectionUtils.isEmpty(userPositionDTOList)) {
            //撤销该合约所有未撮合的挂单
            //撤单之前该合约的所有持仓已经交割，此时如果撤强平单，爆仓检测依然可以通过没有强平单来判断强平单已处理完，因为强平单的撮合实际上是平仓，交割已经把持仓平掉
            contractOrderService.cancelOrderByContractId(deliveryContract.getId(), new HashMap<>());
            //合约更新为已交割
//                contractCategoryService.updateContractStatus(deliveryContract.getId(), ContractStatus.DELIVERED);
            initNewContract(deliveryContract, deliveryEffectTypeEnum);
            return;
        }

        // 持仓分批处理
        Map<Integer, List<UserPositionDTO>> positionListMap = new HashMap<>();
        int size = userPositionDTOList.size();
        int j = DeliveryChildThreadPool.CORE_POOL_SIZE - 1;
        if (size <= j) {
            positionListMap.put(1, userPositionDTOList);
        } else {
            int div = size / j;
            int rem = size % j;

            int i = 1;
            for (; i <= j; i++) {
                positionListMap.put(i, userPositionDTOList.subList((i - 1) * div, i * div));
            }
            if (rem != 0) {
                positionListMap.put(i, userPositionDTOList.subList((i - 1) * div, (i - 1) * div + rem));
            }
        }

        CountDownLatch childCountDownLatch = new CountDownLatch(positionListMap.size());
        for (Map.Entry<Integer, List<UserPositionDTO>> entry : positionListMap.entrySet()) {
            DeliveryChildThreadPool.execute(new PositionRunnable(userIdList, childCountDownLatch, entry.getValue(), index, deliveryContract));
        }

        try {
            childCountDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("Delivery>> PositionRunnable InterruptedException !", e);
        }

        //撤销该合约所有未撮合的挂单
        //撤单之前该合约的所有持仓已经交割，此时如果撤强平单，爆仓检测依然可以通过没有强平单来判断强平单已处理完，因为强平单的撮合实际上是平仓，交割已经把持仓平掉
        contractOrderService.cancelOrderByContractId(deliveryContract.getId(), new HashMap<>());
        //合约更新为已交割
//            contractCategoryService.updateContractStatus(deliveryContract.getId(), ContractStatus.DELIVERED);
        initNewContract(deliveryContract, deliveryEffectTypeEnum);
    }
}
