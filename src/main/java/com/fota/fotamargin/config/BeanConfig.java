package com.fota.fotamargin.config;

import com.fota.data.manager.IndexCacheManager;
import com.fota.data.service.SpotIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Yuanming Tao
 * Created on 2018/10/8
 * Description
 */
@Component
public class BeanConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SpotIndexService spotIndexService;

    @Bean
    public IndexCacheManager indexCacheManager() {
        return new IndexCacheManager(redisTemplate, spotIndexService);
    }
}
