package com.aaa.es.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 15:35
 * @version: ${version}
 * @description：
 *      jedis操作redis
 */
@SpringBootApplication
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;
    
    @Bean
    public JedisCluster getJedisCluster() {
        String nodes = redisProperties.getNodes();
        String[] nodeArray = nodes.split(",");
        Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
        
        for (String node : nodeArray) {
            String[] hostAndPortArray = node.split(":");
            HostAndPort hostAndPort = new HostAndPort(hostAndPortArray[0], Integer.parseInt(hostAndPortArray[1]));
            hostAndPortSet.add(hostAndPort);
        }
        JedisCluster jedisCluster = new JedisCluster(hostAndPortSet, Integer.parseInt(redisProperties.getCommandTimeout()), Integer.parseInt(redisProperties.getMaxAttempts()));
        return jedisCluster;
    }

}
