package com.fota.fotamargin.job.common;


import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.fota.fotamargin.job.ForcedLiquidationJobInit;

/**
 * @author taoyuanming
 * Created on 2018/9/8
 * Description 交割
 */
public class JobRunner implements Runnable {

    @Override
    public void run() {
        //cron表达式任务使用com.dangdang.ddframe.job.lite.api.JobScheduler;

        //交割任务
        new JobScheduler(ForcedLiquidationJobInit.createRegistryCenter(), JobConfiguration.createDeliveryJobConfiguration()).init();
        //上新任务
        new JobScheduler(ForcedLiquidationJobInit.createRegistryCenter(), JobConfiguration.createEffectJobConfiguration()).init();

    }
}
