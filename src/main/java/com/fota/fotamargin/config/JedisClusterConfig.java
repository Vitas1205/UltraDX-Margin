package com.fota.fotamargin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author taoyuanming
 * Created on 2018/8/2
 * Description jedis config
 */
@Configuration
@RefreshScope
public class JedisClusterConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    private int maxIdle = 10;

    private long maxWaitMillis = 10000;

    private int maxTotal = 500;

    @Bean
    public JedisCluster jedisCluster() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setTestOnBorrow(true);
        return new JedisCluster(getClusterNodes(), 2000, 100, jedisPoolConfig);
    }

    public Set<HostAndPort> getClusterNodes() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //解析集群配置
        String[] clusterNodes = nodes.split(",");
        for (String clusterNode : clusterNodes) {
            String[] node = clusterNode.split(":");
            String host = node[0];
            int port = Integer.parseInt(node[1]);
            jedisClusterNodes.add(new HostAndPort(host, port));
        }
        return jedisClusterNodes;
    }

}
