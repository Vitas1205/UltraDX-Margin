package com.fota.fotamargin.job.common;

import com.fota.fotamargin.job.ForcedLiquidationJobInit;
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
public class JobPool {
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 2;
    private static final Long KEEP_ALIVE_TIME = 1000L;
    private static final int WORK_QUEUE_CAPACITY = 1024;

    private static ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    private static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadFactory threadFactory = new CommonJobThreadFactory();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(WORK_QUEUE_CAPACITY), threadFactory);
    }

    static class CommonJobThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "common-job-thread-pool-" + id.addAndGet(1));
        }
    }
}
