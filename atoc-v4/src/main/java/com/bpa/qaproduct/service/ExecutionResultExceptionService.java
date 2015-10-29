package com.bpa.qaproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.ExecutionResultException;
import com.bpa.qaproduct.repository.ExecutionResultExceptionDAO;


@Service("ExecutionResultExceptionService")
@Transactional
public class ExecutionResultExceptionService {
	/* Changes done for execution result exception in Services STARTS HERE */
	@Autowired
	public ExecutionResultExceptionDAO exResultExceptionDAO;
	
	public void saveExecutionResultException(ExecutionResultException exResultException) {
		exResultExceptionDAO.saveExecutionResultException(exResultException);
	}

	public ExecutionResultException saveExecutionResultExceptionWithReturn(ExecutionResultException exResultException) {
		return exResultExceptionDAO.saveExecutionResultExceptionWithReturn(exResultException);
	}
}
