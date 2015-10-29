package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.repository.OrganizationDAO;

@Service("OrganizationService")
@Transactional
public class OrganizationService {

	@Autowired
	public OrganizationDAO organizationDAO;

	public int getOrganizationFilterCount(Organization organization) {
		return organizationDAO.getOrganizationFilterCount(organization);
	}

	public List getOrganizationList(Organization organization, int start, int limit) {
		return organizationDAO.getOrganizationList(organization, start, limit);
	}

	public List getAllOrganizationList(Organization organization) {
		return organizationDAO.getAllOrganizationList(organization);
	}

	public Organization saveOrganization(Organization organization) {
		return (Organization) organizationDAO.saveOrganization(organization);
	}

	public void removeCorganization(Organization organization) {
		organizationDAO.removeCorganization(organization);
	}

	public Organization getOrganizationById(Integer Id) {
		return organizationDAO.getOrganizationById(Id);
	}
	public Organization getOrganizationBySearchParam(Organization organization) {
		return organizationDAO.getOrganizationBySearchParam(organization);
	}

	/*public Organization getOrganizationByName(Organization organization) {
		return organizationDAO.getOrganizationByName(organization);
	}

	public Organization getOrgByName(String organizationName) {
		return organizationDAO.getOrgByName(organizationName);
	}
	
	public Organization validateOrgEmail(String email)
	{
		return organizationDAO.validateOrgEmail(email);
		
		
	}*/
}
