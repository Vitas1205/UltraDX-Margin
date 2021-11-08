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
// * Description 交割季度合约定时任务
// */
//@Component
//@EnableScheduling
//@SyncTaskAnnotation(taskUniqueId = "Margin_DeliveryQuarterly", defaultExpireTime = "3600", timeUnit = "EX")
//public class DeliveryQuarterlyFuturesScheduledTask {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    private Executor executor = Executors.newFixedThreadPool(1);
//
//    /**
//     * 每月最后一个周三下午4点整
//     */
//    @Scheduled(cron="0 0 16 ? * 4")
//    public void execute() {
//        long taskTime = TimeUtils.getTimeInMillis();
//        log.info("Execute DeliveryQuarterlyFuturesScheduledTask Starting, Started Time: {}", TimeUtils.formatMillisecondsToString(taskTime));
//
//        executor.execute(new DeliveryRunnable(ContractTypeEnum.SEASON.getCode()));
//
//        log.info("Execute DeliveryQuarterlyFuturesScheduledTask Done, Finished Time: {}, Consumed Time: {}", TimeUtils.getCurrentTime(), TimeUtils.getTimeInMillis() - taskTime);
//    }
//}
