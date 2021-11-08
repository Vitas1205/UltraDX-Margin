package com.fota.fotamargin.job;


import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.millis.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author taoyuanming
 * Created on 2018/8/17
 * Description
 */
public class ForcedLiquidationJobInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForcedLiquidationJobInit.class);

    public static final String NAME_SPACE = BeanUtil.getBean(PropertiesConfig.class).getNamespace();

    /**
     * 分片数，fota-margin部署实例数
     */
    public static final int SHARDING_TOTAL_COUNT = 2;

    /**
     * 如果任务配置shardingTotalCount=1并且部署两台机器；则每次到定时任务的执行时间，只有一台机器会执行并且该台机器shardingContext.getShardingItem()=0
     * 如果任务配置shardingTotalCount=2并且部署两台机器；则每次到定时任务的执行时间，则两台机器都会执行并且每台机器执行时的shardingContext.getShardingItem()分别为0和1
     * 如果不需要任务分片任务配置shardingTotalCount=1即可
     * @return
     */
    public static CoordinatorRegistryCenter createRegistryCenter() {
        LOGGER.info("zk serverList: {}", BeanUtil.getBean(PropertiesConfig.class).getZkServerLists());
        LOGGER.info("elastic.job.namespace: {}", NAME_SPACE);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(BeanUtil.getBean(PropertiesConfig.class).getZkServerLists(), NAME_SPACE));
        regCenter.init();
        return regCenter;
    }

    public static LiteJobConfiguration createJobConfiguration() {
        // 创建作业配置
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("forcedLiquidationJob", "1534425848000;300", SHARDING_TOTAL_COUNT).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, ForcedLiquidationJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return simpleJobRootConfig;
    }
}
