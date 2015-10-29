package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserToken;

@Repository
public class UserTokenDAO extends QatAbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	public UserTokenDAO() {
		super();
	}

	public void saveUserToken(UserToken userToken) {
		super.saveOrUpdate(userToken);
	}

	@SuppressWarnings("unchecked")
	public UserToken getUserToken(UserToken userTokenValue) {

		Session session = sessionFactory.openSession();
		Criteria criteria = createCriteria(session, userTokenValue);

		List<UserToken> list = null;

		UserToken userToken = new UserToken();

		logger.info("Criteria: " + criteria.toString());

		try {

			list = criteria.list();
			logger.debug("list size : " + list.size());
			if (criteria.list().size() != 0) {
				userToken = (UserToken) list.get(0);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
				session = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return userToken;

	}

	private Criteria createCriteria(Session session, UserToken userToken) {

		Criteria criteria = session.createCriteria(UserToken.class);

		if (userToken.getUserTokenId() != null) {
			criteria.add(Restrictions.eq("userTokenId",
					userToken.getUserTokenId()));
		}

		if (userToken.getUserId() != null) {
			criteria.add(Restrictions.eq("userId", userToken.getUserId()));
		}
		if (userToken.getToken() != null) {
			criteria.add(Restrictions.eq("token", userToken.getToken()));
		}

		return criteria;

	}

	public void removeUserToken(UserToken userToken) {
		super.delete(userToken);
	}

}
