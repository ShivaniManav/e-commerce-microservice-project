package com.sm.core.data.config.redis;

import com.sm.core.data.builder.DataSourceProps;
import com.sm.core.data.config.db.Vendors;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public abstract class BaseRedisConfig {

    String serviceName;

    String connectionFactory;

    String templateName;

    public BaseRedisConfig(String serviceName, String connectionFactory, String templateName) {
        this.serviceName = serviceName;
        this.connectionFactory = connectionFactory;
        this.templateName = templateName;
    }

    public BeanFactoryPostProcessor createRedisTemplateBean() {
        return configurableListableBeanFactory -> {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) configurableListableBeanFactory;
            registry.registerBeanDefinition(connectionFactory,
                    BeanDefinitionBuilder.genericBeanDefinition(LettuceConnectionFactory.class, this::getConnectionFactory)
                            .getBeanDefinition());
            registry.registerBeanDefinition(templateName,
                    BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class, this::getTemplate)
                            .addPropertyReference("connectionFactory", connectionFactory)
                            .addDependsOn(connectionFactory)
                            .getBeanDefinition());
        };
    }


    private LettuceConnectionFactory getConnectionFactory() {
        return new LettuceConnectionFactory(getRedisConfiguration());
    }

    private RedisStandaloneConfiguration getRedisConfiguration() {
        DataSourceProps.loadDataSourceProps(serviceName,Vendors.REDIS, Vendors.REDIS);
        String rootKey = DataSourceProps.getRootKey(serviceName,Vendors.REDIS, Vendors.REDIS);

        return new RedisStandaloneConfiguration(
                        DataSourceProps.getStringProperty(rootKey, "redis.hostname"),
                        DataSourceProps.getNumericProperty(rootKey, "redis.port").intValue()
                );
    }

    private RedisTemplate<String, Object> getTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(getConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
