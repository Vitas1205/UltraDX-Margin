package com.fota.fotamargin.delivery;

import com.fota.fotamargin.common.entity.InternalMethodResult;
import com.fota.fotamargin.common.enums.InternalMethodResultCodeEnum;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.common.util.FUUID;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.manager.MarginManager;
import com.fota.fotamargin.message.EmailAndSmsRunnable;
import com.fota.fotamargin.message.MessageThreadPool;
import com.fota.fotamargin.message.MqRunnable;
import com.fota.fotamargin.task.delivery.DeliveryContractRunnable;
import com.fota.margin.domain.MarginMqDto;
import com.fota.margin.domain.MqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Yuanming Tao
 * Created on 2018/11/14
 * Description
 */
public class PenetrateRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryContractRunnable.class);

    private static MarginManager marginManager = BeanUtil.getBean(MarginManager.class);

    private Map<String, BigDecimal> latestPriceMap;
    private Long[] userIdArr;
    private CountDownLatch countDownLatch;
    private Set<Long> deliveringIdSet;

    public PenetrateRunnable(Map<String, BigDecimal> latestPriceMap, Long[] userIdArr, CountDownLatch countDownLatch, Set<Long> deliveringIdSet) {
        this.latestPriceMap = latestPriceMap;
        this.userIdArr = userIdArr;
        this.countDownLatch = countDownLatch;
        this.deliveringIdSet = deliveringIdSet;
    }

    @Override
    public void run() {
        try {
            checkPenetrate();
        } catch (Exception e) {
            LOGGER.error("Delivery>>PenetrateRunnable checkPenetrate exception,", e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void checkPenetrate() {
        InternalMethodResult internalMethodResult;
        BigDecimal userTotalRights;
        for (Long userId : userIdArr) {
            try {
                internalMethodResult = marginManager.calculateUserRights(userId, latestPriceMap, deliveringIdSet);
            } catch (Exception e) {
                LOGGER.error("Delivery>>DeliveryRunnable marginManager.calculateUserRights Exception, userId:{}, latestPriceMap:{}, e:", userId, latestPriceMap, e);
                continue;
            }
            if (!InternalMethodResultCodeEnum.SUCCESS.getCode().equals(internalMethodResult.getCode())) {
                LOGGER.error("Delivery>>DeliveryRunnable calculateUserRights error, msg:{}, userId:{}", internalMethodResult.getMsg(), userId);
                continue;
            }
            userTotalRights = (BigDecimal)internalMethodResult.getRes();
            if (userTotalRights.compareTo(BigDecimal.ZERO) < 0) {
                //不提示用户已穿仓
                // 判断合约账户总权益是否为负 为负记录后台管理系统MQ
                // 交割完的合约没有持仓，但用户可能有其他合约的持仓
                MessageThreadPool.execute(new MqRunnable(MqConstant.MARGIN_TOPIC, MqConstant.PENETRATE_WARN, new MarginMqDto(MqConstant.TYPE_PENETRATE, userId, null, userTotalRights, TimeUtils.getTimeInMillis(), FUUID.getUUUID())));
                MessageThreadPool.execute(new MqRunnable(MqConstant.MARGIN_TOPIC, MqConstant.PENETRATE_RECORD, new MarginMqDto(MqConstant.TYPE_PENETRATE, userId, null, userTotalRights, TimeUtils.getTimeInMillis(), FUUID.getUUUID())));
                MessageThreadPool.execute(new EmailAndSmsRunnable(userId, MqConstant.TYPE_PENETRATE));
                marginManager.updateOrInsertUserOperationLimit(userId);
            }
        }
    }
}
