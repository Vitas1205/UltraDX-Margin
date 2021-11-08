package com.fota.fotamargin.service.impl;

import com.fota.fotamargin.common.enums.DeliveryEffectTypeEnum;
import com.fota.fotamargin.task.delivery.ArtificialEffectFuturesRunnable;
import com.fota.fotamargin.task.delivery.DeliveryRunnable;
import com.fota.fotamargin.task.delivery.EffectFuturesRunnable;
import com.fota.margin.domain.ResultCode;
import com.fota.margin.service.MarginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author taoyuanming
 * Created on 2018/8/25
 * Description 交割
 */
public class MarginServiceImpl implements MarginService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(16), new DeliveryThreadFactory());

//
//    @Autowired
//    private RedisManager redisManager;

    /**
     * 交割合约
     * @param integer
     * @return
     */
    @Override
    public ResultCode deliveryFutures(Integer integer) {
//        Integer contractType = ContractTypeEnum.getCode(integer);
//        if (contractType == null) {
//            return ResultCode.error(1, "contractType is wrong");
//        }
//        try {
//            new DeliveryRunnable().run();
//        } catch (Exception e) {
//            log.error("deliveryFutures exception", e);
//            return ResultCode.error(2, "deliveryFutures exception");
//        }

        return ResultCode.success();
    }

    @Override
    public ResultCode EffectNextFutures(Integer integer) {
//        Integer contractType = ContractTypeEnum.getCode(integer);
//        if (contractType == null) {
//            return ResultCode.error(1, "contractType is wrong");
//        }
//        try {
//            new EffectFuturesRunnable().run();
//        } catch (Exception e) {
//            log.error("EffectNextFutures exception", e);
//            return ResultCode.error(2, "EffectNextFutures exception");
//        }

        return ResultCode.success();
    }

    /**
     * 手动交割
     * @param contractIdList
     * @return
     */
    @Override
    public ResultCode artificialDeliveryAndEffect(List<Long> contractIdList) {
        log.info("artificialDeliveryAndEffect, contractIdList:{}", contractIdList);
        threadPoolExecutor.execute(new DeliveryRunnable(contractIdList, DeliveryEffectTypeEnum.ARTIFICIALITY_SIMILAR_AUTO));
        threadPoolExecutor.execute(new ArtificialEffectFuturesRunnable());
        return ResultCode.success();
    }

    static class DeliveryThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "delivery-thread-pool-" + id.addAndGet(1));
        }
    }
}
