package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.entity.Role;

@Repository("UserDAO")
public class UserDAO extends QatAbstractDao {

	public UserDAO() {
		super();
	}
	
	private DetachedCriteria createUserRoleCriteria(UserRole userRole,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserRole.class);
		
		if (userRole.getUser() != null)
		{
			DetachedCriteria userCriteria = criteria.createAlias("user", "user");
						
			if (userRole.getUser().getFirstName() != null)
			{
				
				userCriteria.add(Restrictions.like("user.firstName", "%"+userRole.getUser().getFirstName()+"%").ignoreCase());
				
			}
			if (userRole.getUser().getLastName() != null)
			{
				userCriteria.add(Restrictions.like("user.lastName", "%"+userRole.getUser().getLastName()+"%").ignoreCase());
				
			}
			
			
			if(userRole.getRole().getRoleName().equalsIgnoreCase("Org Admin")){
			if (userRole.getUser().getOrganization() != null)
			{
				DetachedCriteria organizationCriteria = userCriteria.createAlias("user.organization", "organization");
				organizationCriteria.add(Restrictions.eq("organization.organizationName", ""+userRole.getUser().getOrganization().getOrganizationName()+"").ignoreCase());
			
			}
			}
		   if (userRole.getRole().getRoleName().equalsIgnoreCase("Master Admin")) {
				if (userRole.getUser().getOrganization() != null)
				{
					DetachedCriteria organizationCriteria = userCriteria.createAlias("user.organization", "organization");
					organizationCriteria.add(Restrictions.like("organization.organizationName", "%"+userRole.getUser().getOrganization().getOrganizationName()+"%").ignoreCase());
				}
			}
			
			
		}
				
		return criteria;
	}
	
	private DetachedCriteria createUserCriteria(User user,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		if (user.getFirstName() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("firstName", ""+user.getFirstName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("firstName",
						"%" + user.getFirstName() + "%").ignoreCase());
			}
		}
		if (user.getLastName() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("lastName", ""+user.getLastName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("lastName",
						"%" + user.getLastName() + "%").ignoreCase());
			}
			
		}
		if (user.getUserEmail() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("userEmail", ""+user.getUserEmail()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("userEmail",
						"%" + user.getUserEmail() + "%").ignoreCase());
			}
			
			
		}
		if (user.getUserPassword() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("userPassword", ""+user.getUserPassword()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("userPassword","%" + user.getUserPassword() + "%").ignoreCase());
			}			
		}
		if (user.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive", "" + user.getIsActive()+ ""));
		}
		if (user.getOrganization() != null) 
		{
			criteria.add(Restrictions.eq("organization", user.getOrganization()));
		}
       
		return criteria;

	}

	public int getUserFilterCount(User user) {
		DetachedCriteria criteria = createUserCriteria(user,"");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("userRoles", FetchMode.SELECT);
		return super.getObjectListCount(User.class, criteria);
	}

	public List getUserList(User user) {
		DetachedCriteria criteria = createUserCriteria(user,""); // match type is not set
		criteria.addOrder(Order.desc("userId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("userRoles", FetchMode.SELECT);
		return super.getAllObjectList(User.class, criteria);
	}

	public List getAllUserList(User user) {
		DetachedCriteria criteria = createUserCriteria(user,""); // match type is not set
		criteria.addOrder(Order.desc("userId"));
		return super.getAllObjectList(User.class, criteria);
	}

	public void saveUser(User user) {
		super.saveOrUpdate(user);
	}

	public User saveUserObjWithReturn(User user) {
		return (User) super.saveOrUpdatewithReturn(user);
	}

	public EmailJob saveEmailJobWithReturnForgot(EmailJob emailJob)
	{
		return (EmailJob) super.saveOrUpdate(emailJob);
	}
	
	public EmailJob saveOrUpdateEmailJob(EmailJob emailJob)
	{
		return (EmailJob) super.saveOrUpdateBlob(emailJob);
	}
	
	public User saveOnlyObj(User addUser) {
		return (User) super.saveOnlyObj(addUser);
	}

	public void removeUser(User user) {
		super.delete(user);
	}

	public User getUserById(Integer Id) {
		return (User) super.find(User.class, Id);
	}

	public User getUserBySearchParameter(User searchUser) {
		DetachedCriteria criteria = createUserCriteria(searchUser,"exact");
		return (User) super.getUniqueObject(User.class, criteria);	
	}
	
	public int getUserRoleFilterCount(UserRole userRole) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole,"");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getObjectListCount(UserRole.class, criteria);
	}

	public List getUserRoleList(UserRole userRole,int start,int limit) {
		DetachedCriteria criteria = createUserRoleCriteria(userRole,""); // match type is not set
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getAllObjectList1(UserRole.class, criteria,start,limit);
	}

}
