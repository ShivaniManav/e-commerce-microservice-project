package com.sm.iam.dao.impl;


import com.sm.iam.dao.UserDao;
import com.sm.iam.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext(unitName = "iamPersistanceUnit")
	private EntityManager entityManager;

	@Override
	public User findByUserName(String userName) {
		Query query = entityManager.createQuery("from User where username=:uName", User.class);
		query.setParameter("uName", userName);
		User user = null;
		try {
			user = (User)query.getSingleResult();
			// Hibernate.initialize(user.getUserAddresses());
			// Hibernate.initialize(user.getRoles());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return user;
	}

	@Override
	public User findByEmail(String email) {
		Query query = entityManager.createQuery("from User where email=:uEmail", User.class);
		query.setParameter("uEmail", email);
		User user = null;
		try {
			user = (User) query.getSingleResult();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		return user;
	}

	@Override
	public void save(User user) {
		entityManager.persist(user);
	}
	
}
