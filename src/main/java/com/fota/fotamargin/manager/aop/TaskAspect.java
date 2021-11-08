package com.fota.fotamargin.manager.aop;

import com.fota.fotamargin.common.util.JedisClusterUtil;
import com.fota.fotamargin.manager.RedisManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author taoyuanming
 * Created on 2018/7/18
 * Description 定时任务切面
 */
@Component
@Aspect
public class TaskAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAspect.class);

    @Autowired
    private RedisManager redisManager;

    /**
     * com.fota包下 以ScheduledTask结尾的类 以execute结尾的方法
     * @param point
     * @throws Throwable
     */
    @Around("(execution(* com.fota..*ScheduledTask.*execute(..)))")
    public void syncTask(ProceedingJoinPoint point) throws Throwable {
        SyncTaskAnnotation syncTaskAnnotation = point.getTarget().getClass().getAnnotation(SyncTaskAnnotation.class);
        if (syncTaskAnnotation != null) {
            //执行方法之前
            String taskUniqueId = syncTaskAnnotation.taskUniqueId();
            String defaultExpireTime = syncTaskAnnotation.defaultExpireTime();
            String timeUnit = syncTaskAnnotation.timeUnit();

            Boolean ifAbsent = JedisClusterUtil.tryDistributedLockWithTimeUnit(taskUniqueId, "", timeUnit, Long.parseLong(defaultExpireTime));
            if (ifAbsent) {
                //执行方法
                try {
                    point.proceed();
                } catch (Throwable throwable) {
                    LOGGER.error("TaskAspect proceed error: ", throwable);
                }finally {
                    //执行方法之后
                    redisManager.deleteValue(taskUniqueId);
                }
            } else {
                //该定时任务已在执行或已被执行
            }
        } else {
            //没有使用SyncTaskAnnotation注解则直接执行方法
            point.proceed();
        }
    }
}
