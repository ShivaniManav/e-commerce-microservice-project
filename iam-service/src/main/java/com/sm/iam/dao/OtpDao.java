package com.sm.iam.dao;

public interface OtpDao {

	public String getOTP(String email, String otp);

	public int generateOTP(String email);
	
	public void invalidateOTP(String email, String otp);
	
}
