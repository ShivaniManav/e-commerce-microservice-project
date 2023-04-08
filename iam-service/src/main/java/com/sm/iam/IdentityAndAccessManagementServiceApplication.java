package com.sm.iam;

import com.sm.core.app.SMSpringApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

public class IdentityAndAccessManagementServiceApplication extends SMSpringApplication {

	public static void main(String[] args) {
		SMSpringApplication.run(IdentityAndAccessManagementServiceApplication.class, "/iam", args);
	}

}
