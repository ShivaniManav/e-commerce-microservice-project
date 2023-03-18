package com.sm.iam.service.impl;

import com.sm.iam.dao.TokenDao;
import com.sm.iam.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private TokenDao tokenDao;

	@Override
	public String getTokenFromBlacklist(String token) {
		return tokenDao.getTokenFromBlacklist(token);
	}

	@Override
	public void addTokenInBlacklist(String token, long ttl) {
		tokenDao.addTokenInBlacklist(token, ttl);
	}

	@Override
	public boolean isTokenInBlacklist(String token) {
		return getTokenFromBlacklist(token) != null;
	}

	@Override
	public void generatePasswordResetToken(String email, String token) {
		tokenDao.generatePasswordResetToken(email, token);
	}
	
	@Override
	public String getEmailByPasswordResetToken(String token) {
		return tokenDao.getEmailByPasswordResetToken(token);
	}

	@Override
	public void deletePasswordResetToken(String token) {
		tokenDao.deletePasswordResetToken(token);
	}
	
}
