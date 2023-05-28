package com.sm.iam.config;

import com.sm.core.data.config.redis.BaseRedisConfig;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig extends BaseRedisConfig {

    public RedisConfig() {
        super("iam", "iamConnectionFactory", "iamRedisTemplate");
    }

    @Bean
    public BeanFactoryPostProcessor iamRedisPersistence() {
        return createRedisTemplateBean();
    }
}
