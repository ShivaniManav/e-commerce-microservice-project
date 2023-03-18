package com.sm.iam.dao;

public interface TokenDao {
	
	String getTokenFromBlacklist(String token);
	
	void addTokenInBlacklist(String token, long ttl);
	
	public void generatePasswordResetToken(String email, String token);
	
	public String getEmailByPasswordResetToken(String token);

	public void deletePasswordResetToken(String token);
	
}
