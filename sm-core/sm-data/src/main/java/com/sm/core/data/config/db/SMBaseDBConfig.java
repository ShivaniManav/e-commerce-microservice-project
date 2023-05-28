package com.sm.core.data.config.db;

import com.sm.core.data.builder.DataSourceBuilder;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@EnableTransactionManagement
public abstract class SMBaseDBConfig {

    private static final String DATA_SOURCE_TRANSACTION_MANAGER = "dataSourceTransactionManager";
    private static final String SERVICE_DEFAULT_DATA_SOURCE = "serviceDefaultDataSource";
    private static final String DATA_SOURCE = "dataSource";

    private String packagesToScan;

    private String serviceName;

    private String dataSourceName;

    private String transactionManagerName;

    private String emfName;

    private String dataSourceBeanName;

    private String vendor;

    private boolean setupPersistanceContext;

    public SMBaseDBConfig(String dataSourceType, String dataSourceBeanName, String vendor) {
        this.dataSourceName = dataSourceType;
        this.dataSourceBeanName = dataSourceBeanName;
        this.vendor = vendor;

        this.setupPersistanceContext = false;
    }

    public SMBaseDBConfig(String serviceName, String packagesToScan, String dataSourceName,
                          String transactionManagerName, String persistenceUnitName, String vendor) {
        this.serviceName = serviceName;
        this.packagesToScan = packagesToScan;
        this.dataSourceName = dataSourceName;
        this.transactionManagerName = transactionManagerName;
        this.emfName = persistenceUnitName;
        this.vendor = vendor;

        this.setupPersistanceContext = true;
    }

    public DataSource createDataSource() {
        return DataSourceBuilder.builder().vendor(vendor).dataSourceName(dataSourceName).build()
                .buildDataSource(serviceName);
    }

    public BeanFactoryPostProcessor createPersistenceBean() {
        return configurableListableBeanFactory -> {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) configurableListableBeanFactory;
            dataSourceBeanName = dataSourceBeanName == null ? dataSourceName + "_DATASOURCE" : dataSourceBeanName;
            registry.registerBeanDefinition(dataSourceBeanName,
                    BeanDefinitionBuilder
                            .genericBeanDefinition(DataSource.class, this::createDataSource).getBeanDefinition());
            if(setupPersistanceContext) {
                registry.registerBeanDefinition(emfName,
                        BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class,
                                        () -> createEntityManagerFactory(packagesToScan))
                                .addPropertyReference(DATA_SOURCE, dataSourceBeanName)
                                .addDependsOn(dataSourceBeanName)
                                .getBeanDefinition()
                );
                registry.registerBeanDefinition(transactionManagerName,
                        BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class,
                                        this::jpaTransactionManager)
                                .addPropertyReference("entityManagerFactory", emfName)
                                .addPropertyValue("persistenceUnitName", emfName)
                                .getBeanDefinition()
                );
            } else {
                registry.registerBeanDefinition(DATA_SOURCE_TRANSACTION_MANAGER,
                        BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class,
                                        this::dataSourceTransactionManager)
                                .addPropertyReference(DATA_SOURCE, DATA_SOURCE)
                                .getBeanDefinition()
                );
            }
        };
    }

    private HibernateJpaVendorAdapter vendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    private LocalContainerEntityManagerFactoryBean createEntityManagerFactory(String packagesToScan) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(packagesToScan);
        entityManagerFactoryBean.setJpaProperties(jpaProperties());
        return entityManagerFactoryBean;
    }

    private Properties jpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
        jpaProperties.put("hibernate.show_sql", true);
        jpaProperties.put("hibernate.cache.use_query_cache", false);
        return jpaProperties;
    }

    private PlatformTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager();
    }

    private PlatformTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager();
    }
}
