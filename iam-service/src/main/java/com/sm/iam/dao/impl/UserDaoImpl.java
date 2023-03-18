package com.sm.iam.dao.impl;


import com.sm.iam.dao.UserDao;
import com.sm.iam.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public User findByUserName(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from User where username=:uName", User.class);
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
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from User where email=:uEmail", User.class);
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
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}
	
}
