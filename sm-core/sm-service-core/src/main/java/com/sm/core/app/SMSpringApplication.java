package com.sm.core.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@SpringBootApplication(scanBasePackages = {"com.sm.core.*"})
public abstract class SMSpringApplication extends SpringBootServletInitializer {

    private static String CONTEXT_PATH = "/";

    private static ConfigurableApplicationContext applicationContext;

    private static Class<?> AppClass;

    public static void run(Class<? extends SMSpringApplication> source, String contextPath, String[] args) {
        if(Objects.nonNull(contextPath) && !contextPath.trim().isEmpty()) {
            CONTEXT_PATH = contextPath;
        }
        AppClass = source;
        applicationContext = SpringApplication.run(source, args);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Class<?> getAppClass() {
        return AppClass;
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setContextPath(CONTEXT_PATH);
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AppClass);
    }
}
