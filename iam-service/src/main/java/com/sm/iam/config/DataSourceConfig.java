package com.sm.iam.config;

import com.sm.core.data.config.db.SMBaseDBConfig;
import com.sm.core.data.config.db.Vendors;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig extends SMBaseDBConfig {

    public DataSourceConfig() {
        super("com.sm", "sm_iam", "iamTransactionManager", "iamPersistanceUnit", Vendors.MYSQL);
    }

    @Bean
    public BeanFactoryPostProcessor iamDBPersistence() {
        return createPersistenceBean();
    }
}
