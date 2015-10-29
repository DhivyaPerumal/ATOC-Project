package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.repository.CustomerRegistrationDao;

@Service("customerRegistrationService")
@Transactional
public class CustomerRegistrationService {

	@Autowired
	public CustomerRegistrationDao customerRegistrationDao;

	public int getCustomerRegistrationFilterCount(
			CustomerRegistration customerRegistration) {
		return customerRegistrationDao
				.getCustomerRegistrationFilterCount(customerRegistration);
	}

	/*public List getCustomerRegistrationList(
			CustomerRegistration customerRegistration) {
		return customerRegistrationDao
				.getCustomerRegistrationList(customerRegistration);
	}*/

	public List getCustomerRegistrationListPagination(
			CustomerRegistration customerRegistration, int start, int limit) {
		return customerRegistrationDao.getCustomerRegistrationListPagination(
				customerRegistration, start, limit);
	}

	public List getAllCustomerRegistrationList(
			CustomerRegistration customerRegistration) {
		return customerRegistrationDao
				.getAllCustomerRegistrationList(customerRegistration);
	}

	public void saveCustomerRegistration(
			CustomerRegistration customerRegistration) {
		customerRegistrationDao.saveCustomerRegistration(customerRegistration);
	}

	public void updateCustomerRegistration(
			CustomerRegistration customerRegistration) {
		customerRegistrationDao
				.updateCustomerRegistration(customerRegistration);
	}

	public CustomerRegistration saveOrupdateCustomerRegistrationWithReturn(
			CustomerRegistration customerRegistration) {
		return customerRegistrationDao
				.updateCustomerRegistration(customerRegistration);
	}

	public void removeCustomerRegistration(
			CustomerRegistration customerRegistration) {
		customerRegistrationDao
				.removeCustomerRegistration(customerRegistration);
	}

	public CustomerRegistration getCustomerRegistrationById(Integer Id) {
		return customerRegistrationDao.getCustomerRegistrationById(Id);
	}
	
	public CustomerRegistration getCustomerRegistrationBySearchParameter(CustomerRegistration customerRegistration) {
		return customerRegistrationDao.getCustomerRegistrationBySearchParameter(customerRegistration);
	}
	
	public EmailJob saveEmailJobWithReturnRegisterforCron(EmailJob emailJob)
	{
		return customerRegistrationDao.saveEmailJobWithReturnforCron(emailJob);
	}
	


}
