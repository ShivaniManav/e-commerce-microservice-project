package com.sm.iam.service;

public interface MailService {
	
	public void sendSimpleMessage();
	
	public void sendOtpEmail(String otp, String toEmail) throws Exception;
	
}
