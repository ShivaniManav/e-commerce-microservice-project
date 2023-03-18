package com.sm.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class IdentityAndAccessManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityAndAccessManagementServiceApplication.class, args);
	}

}
