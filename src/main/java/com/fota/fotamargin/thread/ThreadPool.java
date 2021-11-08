package com.fota.fotamargin.thread;

import com.fota.fotamargin.job.JobThreadPool;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author taoyuanming
 * Created on 2018/8/3
 * Description 线程池
 */
@Component
public class ThreadPool {
//    public static final int CORE_POOL_SIZE = 3 * JobThreadPool.CORE_POOL_SIZE;
//    public static final int MAX_POOL_SIZE = 4 * JobThreadPool.CORE_POOL_SIZE;

    public static final int CORE_POOL_SIZE = 20;
    public static final int MAX_POOL_SIZE = CORE_POOL_SIZE;
    public static final Long KEEP_ALIVE_TIME = 1000L;
    public static final int WORK_QUEUE_CAPACITY = 100;

    private static ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadFactory threadFactory = new BatchForcedLiquidationThreadFactory();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(WORK_QUEUE_CAPACITY), threadFactory);
    }

    static class BatchForcedLiquidationThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "batchForcedLiquidation-thread-pool-" + id.addAndGet(1));
        }
    }
}
