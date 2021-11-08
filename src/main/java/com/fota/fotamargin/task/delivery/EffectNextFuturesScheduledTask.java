//package com.fota.fotamargin.task.delivery;
//
//import com.fota.fotamargin.common.enums.ContractTypeEnum;
//import com.fota.fotamargin.common.util.TimeUtils;
//import com.fota.fotamargin.manager.RedisManager;
//import com.fota.fotamargin.manager.aop.SyncTaskAnnotation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
///**
// * @author taoyuanming
// * Created on 2018/8/7
// * Description 上新月合约定时任务
// */
//@Component
//@EnableScheduling
//@SyncTaskAnnotation(taskUniqueId = "Margin_EffectNextFutures", defaultExpireTime = "3600", timeUnit = "EX")
//public class EffectNextFuturesScheduledTask {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    private Executor executor = Executors.newFixedThreadPool(1);
//
//    @Autowired
//    private RedisManager redisManager;
//
//    /**
//     * 每月28号下午4点多
//     */
//    @Scheduled(cron="0 46 11 ? * 6")
//    public void execute() {
//        long taskTime = TimeUtils.getTimeInMillis();
//        log.info("Execute EffectNextFuturesScheduledTask Starting, Started Time: {}", TimeUtils.formatMillisecondsToString(taskTime));
//
//        executor.execute(new EffectFuturesRunnable());
//        //刷新market缓存的合约数据
//        redisManager.deleteValue("MARKET_ALL_ACTIVE_CONTRACT");
//        log.info("Execute EffectNextFuturesScheduledTask Done, Finished Time: {}, Consumed Time: {}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - taskTime);
//    }
//}
