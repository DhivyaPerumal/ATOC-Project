package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.Role;

@Repository("RoleDAO")
public class RoleDAO extends QatAbstractDao {
	public RoleDAO() {
		super();
	}

	private DetachedCriteria createRoleCriteria(Role role,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Role.class);
		if (role.getRoleDescription() != null) {
			criteria.add(Restrictions.like("roleDescription",
					"%" + role.getRoleDescription() + "%").ignoreCase());
		}
		if (role.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive", "" + role.getIsActive()
					+ ""));
		}
		if (role.getOrganization() != null) {
			criteria.add(Restrictions.like("organization",
					"%" + role.getOrganization() + "%").ignoreCase());
		}
		if (role.getRoleName() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("roleName", ""+role.getRoleName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("roleName",
						"%" + role.getRoleName() + "%").ignoreCase());
			}
			
		}

		return criteria;

	}

	public int getRoleFilterCount(Role role) {
		DetachedCriteria criteria = createRoleCriteria(role,"");
		return super.getObjectListCount(Role.class, criteria);
	}
	public List getRoleList(Role role,int start, int limit) {
		DetachedCriteria criteria = createRoleCriteria(role,"");
		criteria.addOrder(Order.desc("roleId"));
		return super.getObjectListByRangeByValue(Role.class, criteria,
				start, limit);		
	}

	public List getAllRoleList(Role role) {
		DetachedCriteria criteria = createRoleCriteria(role,"");
		criteria.addOrder(Order.desc("roleId"));
		return super.getAllObjectList(Role.class, criteria);
	}

	public void saveRole(Role role) {
		super.saveOrUpdate(role);
	}

	public void removeRole(Role role) {
		super.delete(role);
	}

	public Role getRoleById(Integer Id) {
		return (Role) super.find(Role.class, Id);
	}

	public Role getRoleBySearchParam(Role role) {
		DetachedCriteria criteria = createRoleCriteria(role,"exact");
		return (Role) super.getUniqueObject(Role.class, criteria);
	}

}
