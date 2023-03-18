package com.sm.iam.dao;

import com.sm.iam.entity.User;

public interface UserDao {
	
	User findByUserName(String userName);
	
	User findByEmail(String email);
    
    void save(User user);
	
}
