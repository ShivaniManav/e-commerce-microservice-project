package com.sm.iam;

import com.sm.core.app.SMSpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableEurekaClient
@ComponentScan(basePackageClasses = IdentityAndAccessManagementServiceApplication.class)
public class IdentityAndAccessManagementServiceApplication extends SMSpringApplication {

	public static void main(String[] args) {
		SMSpringApplication.run(IdentityAndAccessManagementServiceApplication.class, "/iam", args);
	}

}
