package com.fota.fotamargin.job.common;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.fota.fotamargin.job.delivery.DeliveryJob;
import com.fota.fotamargin.job.effect.EffectJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tao Yuanming
 * Created on 2018/9/8
 * Description
 */
public class JobConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfiguration.class);

    /**
     * 交割
     * @return
     */
    public static LiteJobConfiguration createDeliveryJobConfiguration() {
        // 创建作业配置
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("deliveryJob", "0 0 8 28 * ?", 1).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DeliveryJob.class.getCanonicalName());
        // 定义Lite作业根配置
        //非毫秒任务使用 com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return simpleJobRootConfig;
    }

    /**
     * 上新
     * @return
     */
    public static LiteJobConfiguration createEffectJobConfiguration() {
        // 创建作业配置
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("effectJob", "30 4 8 28 * ?", 1).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, EffectJob.class.getCanonicalName());
        // 定义Lite作业根配置
        //非毫秒任务使用 com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return simpleJobRootConfig;
    }
}
