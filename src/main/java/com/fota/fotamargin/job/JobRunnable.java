package com.fota.fotamargin.job;


import com.dangdang.ddframe.job.lite.millis.api.JobScheduler;

/**
 * @author taoyuanming
 * Created on 2018/8/17
 * Description
 */
public class JobRunnable implements Runnable {

    @Override
    public void run() {
        new JobScheduler(ForcedLiquidationJobInit.createRegistryCenter(), ForcedLiquidationJobInit.createJobConfiguration()).init();
    }
}
