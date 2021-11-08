package com.fota.fotamargin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;

/**
 * @author taoyuanming
 * Created on 2018/8/2
 * Description jedis config
 */
@Configuration
@RefreshScope
public class JedisConfig {

//    @Value("${spring.redis.host}")
    private String host;

//    @Value("${spring.redis.port}")
    private int port;

//    @Value("${spring.redis.timeout}")
    private int timeout = 6000;

//    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle = 10;

//    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis = 10000;

//    @Value("${spring.redis.pool.max-total}")
    private int maxTotal = 500;

    private JedisPool jedisPool;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        return jedisPool;
    }

    /**
     * release jedisPool
     */
    public @PreDestroy void releaseJedisPool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
