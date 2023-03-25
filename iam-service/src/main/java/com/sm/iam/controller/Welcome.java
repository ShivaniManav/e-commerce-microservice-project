package com.sm.iam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class Welcome {
	
	@GetMapping("/")
	public String message() {
		return "Welcome to IAM service";
	}
	
}