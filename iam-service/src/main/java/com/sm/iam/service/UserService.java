package com.sm.iam.service;


import com.sm.iam.entity.User;
import com.sm.iam.entity.UserAddress;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	
	User findByUserName(String userName);
	
	User findByEmail(String email);
	
	void updateDefaultAddress(int userId, int addrId);
	
	void saveUserAddress(UserAddress userAddress);
	
	void save(User user);
}
