package com.sm.iam.service.impl;

import com.sm.iam.dao.RoleDao;
import com.sm.iam.dao.UserAddressDao;
import com.sm.iam.dao.UserDao;
import com.sm.iam.entity.Role;
import com.sm.iam.entity.User;
import com.sm.iam.entity.UserAddress;
import com.sm.iam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private static final String IAM_TXN = "iamTransactionManager";

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserAddressDao userAddressDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(IAM_TXN)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional(IAM_TXN)
	public User findByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	@Override
	@Transactional(IAM_TXN)
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	@Transactional(IAM_TXN)
	public void updateDefaultAddress(int userId, int addrId) {
		userAddressDao.updateDefaultAddress(userId, addrId);
	}

	@Override
	@Transactional(IAM_TXN)
	public void save(User user) {
		userDao.save(user);
	}

	@Override
	@Transactional(IAM_TXN)
	public void saveUserAddress(UserAddress userAddress) {
		userAddressDao.save(userAddress);
	}
	
}
