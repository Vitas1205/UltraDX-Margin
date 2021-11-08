package com.fota.fotamargin.job.effect;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.fota.fotamargin.common.constant.LogConstant;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.job.common.JobPool;
import com.fota.fotamargin.manager.RedisManager;
import com.fota.fotamargin.task.delivery.EffectFuturesRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author taoyuanming
 * Created on 2018/9/8
 * Description 上新
 */
public class EffectJob implements SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(EffectJob.class);

    private static RedisManager redisManager = BeanUtil.getBean(RedisManager.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        LOGGER.info("Effect>>EffectJob_" + TimeUtils.getCurrentMTime() + "_" + shardingContext.getShardingItem());
        JobPool.execute(new EffectFuturesRunnable());
    }
}
