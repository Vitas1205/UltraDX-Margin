//package com.fota.fotamargin.task.delivery;
//
//import com.fota.fotamargin.common.enums.ContractTypeEnum;
//import com.fota.fotamargin.common.util.TimeUtils;
//import com.fota.fotamargin.manager.aop.SyncTaskAnnotation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
///**
// * @author taoyuanming
// * Created on 2018/8/11
// * Description 上新周合约定时任务
// */
//@Component
//@EnableScheduling
//@SyncTaskAnnotation(taskUniqueId = "Margin_EffectWeekly", defaultExpireTime = "600", timeUnit = "EX")
//public class EffectNextWeeklyFuturesScheduledTask {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    private Executor executor = Executors.newFixedThreadPool(1);
//
//    /**
//     * 每周五的下午4点01份
//     */
//    @Scheduled(cron="0 1 16 ? * 6")
//    public void execute() {
//        long taskTime = TimeUtils.getTimeInMillis();
//        log.info("Execute EffectNextWeeklyFuturesScheduledTask Starting, Started Time: {}", TimeUtils.formatMillisecondsToString(taskTime));
//
//        executor.execute(new EffectFuturesRunnable(ContractTypeEnum.WEEK.getCode()));
//
//        log.info("Execute EffectNextWeeklyFuturesScheduledTask Done, Finished Time: {}, Consumed Time: {}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - taskTime);
//    }
//}
