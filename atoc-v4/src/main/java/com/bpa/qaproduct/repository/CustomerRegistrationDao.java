package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.User;

@Repository("CustomerRegistrationDao")
public class CustomerRegistrationDao extends QatAbstractDao {

	public CustomerRegistrationDao() {
		super();
	}

	private DetachedCriteria createCustomerRegistrationCriteria(
			CustomerRegistration customerRegistration,String matchType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(CustomerRegistration.class);
		if (customerRegistration.getFirstname() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("firstname", ""+customerRegistration.getFirstname()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("firstname",
						"%" + customerRegistration.getFirstname() + "%").ignoreCase());
			}			
		}
		if (customerRegistration.getLastname() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("lastname", ""+customerRegistration.getLastname()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("lastname",
						"%" + customerRegistration.getLastname() + "%").ignoreCase());
			}
			
		}
		if (customerRegistration.getNotes() != null) 
		{
			criteria.add(Restrictions.like("notes",
					"%" + customerRegistration.getNotes() + "%").ignoreCase());
		}
		if (customerRegistration.getAddress() != null) 
		{
			criteria.add(Restrictions.like("address",
					"%" + customerRegistration.getAddress() + "%").ignoreCase());
		}
		if (customerRegistration.getCity() != null) 
		{
			criteria.add(Restrictions.like("city",
					"%" + customerRegistration.getCity() + "%").ignoreCase());
		}
		if (customerRegistration.getState() != null) 
		{
			criteria.add(Restrictions.like("state",
					"%" + customerRegistration.getState() + "%").ignoreCase());
		}
		if (customerRegistration.getCountry() != null) 
		{
			criteria.add(Restrictions.like("country",
					"%" + customerRegistration.getCountry() + "%").ignoreCase());
		}
		if (customerRegistration.getZipCode() != null) 
		{
			criteria.add(Restrictions.like("zipCode",
					"%" + customerRegistration.getZipCode() + "%").ignoreCase());
		}
		if (customerRegistration.getContactName() != null) 
		{
			criteria.add(Restrictions.like("contactName",
					"%" + customerRegistration.getContactName() + "%")
					.ignoreCase());
		}
		if (customerRegistration.getContactEmail() != null) 
		{
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("contactEmail", ""+customerRegistration.getContactEmail()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("contactEmail",
						"%" + customerRegistration.getContactEmail() + "%")
						.ignoreCase());
			}
			
		}
		if (customerRegistration.getContactPhone() != null) 
		{
			criteria.add(Restrictions.like("contactPhone",
					"%" + customerRegistration.getContactPhone() + "%")
					.ignoreCase());
		}
		if (customerRegistration.getCustomerWebsite() != null) 
		{
			criteria.add(Restrictions.like("customerWebsite",
					"%" + customerRegistration.getCustomerWebsite() + "%")
					.ignoreCase());
		}
		if (customerRegistration.getApprovalStatus() != null) 
		{
			/*criteria.add(Restrictions.like("approvalStatus",
					"%" + customerRegistration.getApprovalStatus() + "%")
					.ignoreCase());*/
			criteria.add(Restrictions.eq("approvalStatus", "" + customerRegistration.getApprovalStatus()+ ""));
		}

		return criteria;

	}

	public int getCustomerRegistrationFilterCount(
			CustomerRegistration customerRegistration) {
		DetachedCriteria criteria = createCustomerRegistrationCriteria(customerRegistration,"");
		return super.getObjectListCount(CustomerRegistration.class, criteria);
	}

	/*public List getCustomerRegistrationList(
			CustomerRegistration customerRegistration) {
		DetachedCriteria criteria = createCustomerRegistrationCriteria(customerRegistration,"");
		criteria.addOrder(Order.desc("customerRegistrationId"));
		criteria.add(Restrictions.eq("approvalStatus", "PENDING"));
		return super.getAllObjectList(CustomerRegistration.class, criteria);
	}*/

	public List getCustomerRegistrationListPagination(
			CustomerRegistration customerRegistration, int start, int limit) {
		DetachedCriteria criteria = createCustomerRegistrationCriteria(customerRegistration,"");
		criteria.addOrder(Order.desc("customerRegistrationId"));
		return super.getObjectListByRangeByValue(CustomerRegistration.class,
				criteria, start, limit);
	}

	public List getAllCustomerRegistrationList(
			CustomerRegistration customerRegistration) {
		DetachedCriteria criteria = createCustomerRegistrationCriteria(customerRegistration,"");
		criteria.addOrder(Order.desc("customerRegistrationId"));
		return super.getAllObjectList(CustomerRegistration.class, criteria);
	}

	public void saveCustomerRegistration(
			CustomerRegistration customerRegistration) {
		super.saveOrUpdate(customerRegistration);
	}

	public CustomerRegistration updateCustomerRegistration(
			CustomerRegistration customerRegistration) {
		return (CustomerRegistration) super
				.saveOrUpdatewithReturn(customerRegistration);
	}

	public void removeCustomerRegistration(
			CustomerRegistration customerRegistration) {
		super.delete(customerRegistration);
	}

	public CustomerRegistration getCustomerRegistrationById(Integer Id) {
		return (CustomerRegistration) super
				.find(CustomerRegistration.class, Id);
	}
	
	public CustomerRegistration getCustomerRegistrationBySearchParameter(CustomerRegistration customerRegistration) {
		DetachedCriteria criteria = createCustomerRegistrationCriteria(customerRegistration,"exact");
		return (CustomerRegistration) super.getUniqueObject(CustomerRegistration.class, criteria);	
	}
	
	public EmailJob saveEmailJobWithReturnforCron(EmailJob emailJob)
	{
		return (EmailJob) super.saveOrUpdate(emailJob);
	}

}
