package com.sm.config;

import com.sm.core.app.SMSpringApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@EnableConfigServer
public class ConfigurationServerApplication extends SMSpringApplication {

	public static void main(String[] args) {
		SMSpringApplication.run(ConfigurationServerApplication.class, "/config-server", args);
	}

}
