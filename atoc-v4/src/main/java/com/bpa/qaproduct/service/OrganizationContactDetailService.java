package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.OrganizationContactDetail;
import com.bpa.qaproduct.repository.OrganizationContactDetailDAO;

@Service("OrganizationContactDetailService")
@Transactional
public class OrganizationContactDetailService {

	@Autowired
	public OrganizationContactDetailDAO orgDetailDAO;

	public int getOrganizationContactDetailFilterCount(
			OrganizationContactDetail orgContactDetail) {
		return orgDetailDAO
				.getOrganizationContactDetailFilterCount(orgContactDetail);
	}

	public List getOrganizationContactDetailList(
			OrganizationContactDetail orgContactDetail,int start,int limit) {
		return orgDetailDAO.getOrganizationContactDetailList(orgContactDetail,start,limit);
	}

	public List getAllOrganizationContactDetailList(
			OrganizationContactDetail orgContactDetail) {
		return orgDetailDAO
				.getAllOrganizationContactDetailList(orgContactDetail);
	}

	public void saveOrganizationContactDetail(
			OrganizationContactDetail orgContactDetail) {
		orgDetailDAO.saveOrganizationContactDetail(orgContactDetail);
	}

	public void removeOrganizationContactDetail(
			OrganizationContactDetail orgContactDetail) {
		orgDetailDAO.removeOrganizationContactDetail(orgContactDetail);
	}

	public OrganizationContactDetail getOrganizationContactDetailById(Integer Id) {
		return orgDetailDAO.getOrganizationContactDetailById(Id);
	}
}
