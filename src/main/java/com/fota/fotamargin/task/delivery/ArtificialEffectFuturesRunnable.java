package com.fota.fotamargin.task.delivery;

import com.fota.fotamargin.common.enums.DeliveryEffectTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Tao Yuanming
 * Created on 2018/9/26
 * Description
 */
public class ArtificialEffectFuturesRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtificialEffectFuturesRunnable.class);

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(16), new ArtificialEffectThreadFactory());

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            LOGGER.warn("ArtificialEffectFuturesRunnable InterruptedException!", e);
        } finally {
            threadPool.execute(new EffectFuturesRunnable(DeliveryEffectTypeEnum.ARTIFICIALITY_SIMILAR_AUTO));
        }
    }


    static class ArtificialEffectThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "artificial-effect-thread-pool-" + id.addAndGet(1));
        }
    }
}
