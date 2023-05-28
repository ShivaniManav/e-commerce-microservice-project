package com.sm.gateway;

import com.sm.core.app.SMSpringApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
public class ApiGatewayServerApplication extends SMSpringApplication {

	public static void main(String[] args) {
		SMSpringApplication.run(ApiGatewayServerApplication.class, "/gateway", args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
