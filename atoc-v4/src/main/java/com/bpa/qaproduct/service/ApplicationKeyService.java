package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.ApplicationKey;
import com.bpa.qaproduct.repository.ApplicationKeyDAO;

@Service("ApplicationKeyService")
@Transactional
public class ApplicationKeyService {

	@Autowired
	public ApplicationKeyDAO apKeyDAO;

	public int getApplicationKeyFilterCount(ApplicationKey applicationKey) {
		return apKeyDAO.getApplicationKeyFilterCount(applicationKey);
	}

	public List getApplicationKeyList(ApplicationKey applicationKey, int start, int limit) {
		return apKeyDAO.getApplicationKeyList(applicationKey,start,limit);
	}

	public void saveApplicationKey(ApplicationKey applicationKey) {
		apKeyDAO.saveApplicationKey(applicationKey);

	}

	public void removeApplicationKey(ApplicationKey applicationKey) {
		apKeyDAO.removeApplicationKey(applicationKey);
	}

	public ApplicationKey getApplicationKeyById(Integer Id) {
		return apKeyDAO.getApplicationKeyById(Id);
	}

	public List getAllApplicationKeyList(ApplicationKey applicationKey) {
		return apKeyDAO.getAllApplicationKeyList(applicationKey);
	}

}
