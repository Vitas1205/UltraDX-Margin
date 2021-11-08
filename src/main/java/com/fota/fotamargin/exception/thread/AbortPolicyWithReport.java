package com.fota.fotamargin.exception.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Tao Yuanming
 * Created on 2018/11/13
 * Description
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbortPolicyWithReport.class);

    public AbortPolicyWithReport() {
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        LOGGER.error("java.util.concurrent.RejectedExecutionException: Task " + r.toString() + " rejected from " + e.toString());
        super.rejectedExecution(r, e);
    }
}
