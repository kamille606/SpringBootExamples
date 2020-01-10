package com.aaa.es.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 16:04
 * @version: ${version}
 * @description：
 */
@Service
public class RedisService {

    @Autowired
    private JedisCluster jedisCluster;

    public String get(String key) {
        return jedisCluster.get(key);
    }

    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    public Long del(String key) {
        return jedisCluster.del(key);
    }

    public Long expire(String key, Integer seconds) {
        return jedisCluster.expire(key, seconds);
    }

}
