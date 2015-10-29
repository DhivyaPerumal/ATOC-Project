package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;

@Repository("UserRoleDAO")
public class UserRoleDAO extends QatAbstractDao {

	public UserRoleDAO() {
		super();
	}

	private DetachedCriteria createUserRoleCriteria(UserRole userRole) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
		
		if (userRole.getRole() != null)
		{
			criteria.add(Restrictions.eq("role", userRole.getRole()));
		}
		if (userRole.getUser() != null)
		{
			criteria.add(Restrictions.eq("user", userRole.getUser()));
		}

		return criteria;

	}

	public int getUserRoleFilterCount(UserRole userRole) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole);
		return super.getObjectListCount(UserRole.class, criteria);
	}

	public List getUserRoleList(UserRole userRole,int start,int limit) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole);
		criteria.addOrder(Order.desc("userRoleId"));		
		return super.getObjectListByRangeByValue(UserRole.class, criteria,start,limit);
	}

	public List getAllUserRoleList(UserRole userRole) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole);
		criteria.addOrder(Order.desc("userRoleId"));
		return super.getAllObjectList(UserRole.class, criteria);
	}

	public UserRole saveUserRole(UserRole userRole) {

		return (UserRole) super.saveOrUpdate(userRole);
	}

	public UserRole saveUserRoleWithReturn(UserRole userRole) {
		return (UserRole) super.saveOnlyObj(userRole);
	}

	public UserRole searchbyUser(UserRole userRole) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole);
		return (UserRole) super.getUniqueObject(UserRole.class, criteria);
	}

	public void removeUserRole(UserRole userRole) {
		super.delete(userRole);
	}

	public UserRole getUserRoleById(Integer Id) {
		return (UserRole) super.find(UserRole.class, Id);
	}

}
