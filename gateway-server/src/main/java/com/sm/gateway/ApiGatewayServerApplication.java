package com.sm.gateway;

import com.sm.core.app.SMSpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
public class ApiGatewayServerApplication extends SMSpringApplication {

	public static void main(String[] args) {
		SMSpringApplication.run(ApiGatewayServerApplication.class, "/gateway", args);
	}

}
