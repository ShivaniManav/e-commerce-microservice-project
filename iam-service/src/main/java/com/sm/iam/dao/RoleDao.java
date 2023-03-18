package com.sm.iam.dao;

import com.sm.iam.entity.Role;

public interface RoleDao {

	Role findRoleByName(String theRoleName);
	
}
