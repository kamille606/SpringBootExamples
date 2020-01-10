package com.aaa.es.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 14:35
 * @version: ${version}
 * @description：
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String nodes;
    private String maxAttempts;
    private String commandTimeout;
    private String expire;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(String maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(String commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
