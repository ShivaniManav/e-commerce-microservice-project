package com.sm.iam.dao.impl;

import com.sm.iam.dao.RoleDao;
import com.sm.iam.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext(unitName = "iamPersistanceUnit")
    private EntityManager entityManager;

	@Override
	public Role findRoleByName(String theRoleName) {
		Query query = entityManager.createQuery("from Role where name=:roleName", Role.class);
		query.setParameter("roleName", theRoleName);
		Role role = null;
		try {
			role = (Role) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return role;
	}
	
}
