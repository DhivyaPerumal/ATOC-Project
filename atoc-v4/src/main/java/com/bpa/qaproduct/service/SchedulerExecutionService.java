package com.bpa.qaproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.SchedulerExecution;
import com.bpa.qaproduct.repository.SchedulerExecutionDAO;

@Service("SchedulerExecutionService")
@Transactional
public class SchedulerExecutionService {

	@Autowired
	public SchedulerExecutionDAO schedulerExecutionDAO;

	public void saveSchedulerExecution(SchedulerExecution schedulerExecution) {
		schedulerExecutionDAO.saveSchedulerExecution(schedulerExecution);
	}

}
