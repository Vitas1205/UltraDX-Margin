package com.fota.fotamargin.manager.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taoyuanming
 * Created on 2018/7/18
 * Description 同步多机部署的定时任务注解
 */

@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncTaskAnnotation {
    /**
     * 任务唯一标示，所有任务的taskUniqueId不能相同，redis的key
     * @return
     */
    String taskUniqueId();

    /**
     * 默认的失效时间，任务执行耗时的一定倍数
     * @return
     */
    String defaultExpireTime();

    /**
     * redis的key的失效时间的单位： EX = seconds; PX = milliseconds
     * @return
     */
    String timeUnit();
}
