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
// * Created on 2018/8/7
// * Description 上新月合约定时任务
// */
//@Component
//@EnableScheduling
//@SyncTaskAnnotation(taskUniqueId = "Margin_EffectMonthly", defaultExpireTime = "600", timeUnit = "EX")
//public class EffectNextMonthlyFuturesScheduledTask {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    private Executor executor = Executors.newFixedThreadPool(1);
//
//    /**
//     * 每月最后一个周四下午4点整01分
//     */
//    @Scheduled(cron="0 1 16 ? * 5")
//    public void execute() {
//        long taskTime = TimeUtils.getTimeInMillis();
//        log.info("Execute EffectNextMonthlyFuturesScheduledTask Starting, Started Time: {}", TimeUtils.formatMillisecondsToString(taskTime));
//
//        executor.execute(new EffectFuturesRunnable(ContractTypeEnum.MONTH.getCode()));
//
//        log.info("Execute EffectNextMonthlyFuturesScheduledTask Done, Finished Time: {}, Consumed Time: {}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - taskTime);
//    }
//}
