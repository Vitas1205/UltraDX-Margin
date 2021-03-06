//package com.fota.fotamargin.task.delivery;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
///**
// * @author taoyuanming
// * Created on 2018/8/7
// * Description 定时任务
// */
//@Configuration
//@EnableScheduling
//public class SchedulerConfig {
//	@Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        //线程池大小
//        scheduler.setPoolSize(3);
//        //线程名字前缀
//        scheduler.setThreadNamePrefix("spring-task-delivery-thread");
//        return scheduler;
//    }
//}