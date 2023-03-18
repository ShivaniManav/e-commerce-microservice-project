package com.sm.iam.service;

public interface TokenService {
	
	String getTokenFromBlacklist(String token);
	
	void addTokenInBlacklist(String token, long ttl);
	
	boolean isTokenInBlacklist(String token);

	public void generatePasswordResetToken(String email, String token);
	
	public String getEmailByPasswordResetToken(String token);

	public void deletePasswordResetToken(String token);
	
}
