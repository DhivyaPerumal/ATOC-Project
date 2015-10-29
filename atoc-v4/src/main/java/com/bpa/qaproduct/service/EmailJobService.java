package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.repository.EmailJobDAO;


@Service("EmailJobService")
@Transactional
public class EmailJobService {

	@Autowired
	private EmailJobDAO emailJobDAO;
	
	
	public List getAllEmailPendingStatus(EmailJob emailJob) {
		return emailJobDAO.getAllEmailPendingStatus(emailJob);
	}

	public List getAllEmailTypeJob(EmailJob emailJob)
	{
		return emailJobDAO.getAllEmailPendingStatusWithoutType(emailJob);
	}
	public void removeemailJob(EmailJob emailJob) {
		emailJobDAO.removeemailJobValue(emailJob);
	
	}
	public EmailJob getProjectnById(Integer emailjobid) {
		return emailJobDAO.getProjectnById(emailjobid);
	}
}
