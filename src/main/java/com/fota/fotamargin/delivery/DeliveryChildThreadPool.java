package com.fota.fotamargin.delivery;

import com.fota.fotamargin.exception.thread.AbortPolicyWithReport;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author taoyuanming
 * Created on 2018/11/13
 * Description 交割子线程池
 */
@Component
public class DeliveryChildThreadPool {
    public static final int CORE_POOL_SIZE = 18;
    public static final int MAX_POOL_SIZE = 20;
    public static final Long KEEP_ALIVE_TIME = 1000L;
    public static final int WORK_QUEUE_CAPACITY = 1000;

    private static ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(WORK_QUEUE_CAPACITY), new DeliveryChildThreadFactory(), new AbortPolicyWithReport());
    }

    static class DeliveryChildThreadFactory implements ThreadFactory {
        private static AtomicLong id = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "delivery-child-thread-pool-" + id.addAndGet(1));
        }
    }
}
