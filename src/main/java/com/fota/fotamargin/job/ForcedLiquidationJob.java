package com.fota.fotamargin.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.thread.ForcedLiquidationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author taoyuanming
 * Created on 2018/8/17
 * Description
 */
public class ForcedLiquidationJob implements SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForcedLiquidationJob.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        LOGGER.info("JOB_" + TimeUtils.getCurrentMTime() + "_" + shardingContext.getShardingItem() + "_" + shardingContext.getShardingTotalCount());
        JobThreadPool.execute(new ForcedLiquidationRunner(shardingContext.getShardingItem(), shardingContext.getShardingTotalCount()));
    }
}
