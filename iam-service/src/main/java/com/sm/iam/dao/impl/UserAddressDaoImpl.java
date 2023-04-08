package com.sm.iam.dao.impl;

import com.sm.iam.dao.UserAddressDao;
import com.sm.iam.entity.UserAddress;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class UserAddressDaoImpl implements UserAddressDao {

    @PersistenceContext(unitName = "iamPersistanceUnit")
    private EntityManager entityManager;
	
	@Override
	public UserAddress findDefaultAddressByUserId(int userId) {
		Query<UserAddress> query = (Query<UserAddress>) entityManager.createQuery("from user_address where default=1 and user_id=:userId", UserAddress.class);
		query.setParameter("userId", userId);
		return query.getSingleResult();
	}

	@Override
	public Collection<UserAddress> getAllUserAddressesByUserId(int userId) {
		Query<UserAddress> query = (Query<UserAddress>) entityManager.createQuery("from user_address where user_id=:userId", UserAddress.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Override
	public void updateDefaultAddress(int userId, int addrId) {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("updateDefaultAddress")
											.registerStoredProcedureParameter("addr_id", Integer.class, ParameterMode.IN)
											.registerStoredProcedureParameter("user_id", Integer.class, ParameterMode.IN)
											.setParameter("addr_id", addrId)
											.setParameter("user_id", userId);
		query.execute();
	}

	@Override
	public void save(UserAddress userAddress) {
        entityManager.persist(userAddress);
	}
	
}
