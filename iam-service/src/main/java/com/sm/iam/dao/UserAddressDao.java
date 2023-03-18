package com.sm.iam.dao;

import com.sm.iam.entity.UserAddress;

import java.util.Collection;

public interface UserAddressDao {
	
	UserAddress findDefaultAddressByUserId(int userId);
	
	Collection<UserAddress> getAllUserAddressesByUserId(int userId);
	
	void updateDefaultAddress(int userId, int addrId);
	
	void save(UserAddress userAddress);
	
}
