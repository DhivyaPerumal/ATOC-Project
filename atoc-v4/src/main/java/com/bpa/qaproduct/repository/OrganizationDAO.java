package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;

@Repository("OrganizationDAO")
public class OrganizationDAO extends QatAbstractDao {

	public OrganizationDAO() {
		super();
	}

	private DetachedCriteria createOrganizationCriteria(
			Organization organization, String matchType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Organization.class);
		/*
		 * if (organization.getOrganizationName() != null) {
		 * criteria.add(Restrictions.like("organizationName",
		 * "%"+organization.getOrganizationName()+"%").ignoreCase()); }
		 */
		if (organization.getOrganizationName() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("organizationName", ""+organization.getOrganizationName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("organizationName",
						"%" + organization.getOrganizationName()+ "%").ignoreCase());
			}			
		}
		if (organization.getApplicationKey() != null) 
		{
			criteria.add(Restrictions.like("applicationKey",
					"%" + organization.getApplicationKey() + "%").ignoreCase());
		}
		if (organization.getPhone() != null) {
			criteria.add(Restrictions.like("phone",
					"%" + organization.getPhone() + "%").ignoreCase());
		}
		if (organization.getEmail() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("email", ""+organization.getEmail()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("email",
						"%" + organization.getEmail()+ "%").ignoreCase());
			}
			
		}
		if (organization.getFax() != null) {
			criteria.add(Restrictions.like("fax",
					"%" + organization.getFax() + "%").ignoreCase());
		}
		if (organization.getNotes() != null) {
			criteria.add(Restrictions.like("notes",
					"%" + organization.getNotes() + "%").ignoreCase());
		}
		if (organization.getParentOrganizatonId() != null) {
			criteria.add(Restrictions.like("parentOrganizatonId",
					"%" + organization.getParentOrganizatonId() + "%")
					.ignoreCase());
		}
		if (organization.getAddress() != null) {
			criteria.add(Restrictions.like("address",
					"%" + organization.getAddress() + "%").ignoreCase());
		}
		if (organization.getCity() != null) {
			criteria.add(Restrictions.like("city",
					"%" + organization.getCity() + "%").ignoreCase());
		}
		if (organization.getState() != null) {
			criteria.add(Restrictions.like("state",
					"%" + organization.getState() + "%").ignoreCase());
		}

		if (organization.getCountry() != null) {
			criteria.add(Restrictions.like("country",
					"%" + organization.getCountry() + "%").ignoreCase());
		}
		if (organization.getZipCode() != null) {
			criteria.add(Restrictions.like("zipCode",
					"%" + organization.getZipCode() + "%").ignoreCase());
		}
		if (organization.getIsActive() != null) {
			/*criteria.add(Restrictions.like("isActive",
					"%" + organization.getIsActive() + "%").ignoreCase());*/
			criteria.add(Restrictions.eq("isActive", "" + organization.getIsActive()
					+ ""));
			
			
		}
		if (organization.getOrganizationLogoFileName() != null) {
			criteria.add(Restrictions.like("organizationLogoFileName",
					"%" + organization.getOrganizationLogoFileName() + "%")
					.ignoreCase());
		}

		return criteria;

	}

	public int getOrganizationFilterCount(Organization organization) {
		DetachedCriteria criteria = createOrganizationCriteria(organization,"");
		return super.getObjectListCount(Organization.class, criteria);
	}

	public List getOrganizationList(Organization organization, int start, int limit) {
		DetachedCriteria criteria = createOrganizationCriteria(organization,"");
		criteria.addOrder(Order.desc("organizationId"));
		return super.getObjectListByRangeByValue(Organization.class, criteria, start, limit);
	}

	public List getAllOrganizationList(Organization organization) {
		DetachedCriteria criteria = createOrganizationCriteria(organization,"");
		criteria.addOrder(Order.desc("organizationId"));
		return super.getAllObjectList(Organization.class, criteria);
	}

	public Organization saveOrganization(Organization organization) {
		return (Organization) super.saveOrUpdatewithReturn(organization);
	}

	public void removeCorganization(Organization organization) {
		super.delete(organization);
	}

	public Organization getOrganizationById(Integer Id) {
		return (Organization) super.find(Organization.class, Id);
	}

	public Organization getOrganizationBySearchParam(Organization organization) {
		DetachedCriteria criteria = createOrganizationCriteria(organization,"exact");
		return (Organization) super.getUniqueObject(Organization.class,
				criteria);
	}	

}
