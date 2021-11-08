package com.fota.fotamargin.job;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author taoyuanming
 * Created on 2018/8/17
 * Description job线程池
 */
@Component
public class JobThreadPool {
    public static final int CORE_POOL_SIZE = ForcedLiquidationJobInit.SHARDING_TOTAL_COUNT;
    public static final int MAX_POOL_SIZE = 2 * ForcedLiquidationJobInit.SHARDING_TOTAL_COUNT;
    public static final Long KEEP_ALIVE_TIME = 1000L;
    public static final int WORK_QUEUE_CAPACITY = 1024;

    private static ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadFactory threadFactory = new ForcedLiquidationThreadFactory();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(WORK_QUEUE_CAPACITY), threadFactory);
    }

    static class ForcedLiquidationThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "forcedLiquidation-thread-pool-" + id.addAndGet(1));
        }
    }
}
