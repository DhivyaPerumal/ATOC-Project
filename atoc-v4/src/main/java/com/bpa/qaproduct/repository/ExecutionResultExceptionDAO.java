package com.bpa.qaproduct.repository;


import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.ExecutionResultException;

@Repository("ExecutionResultExceptionDAO")
public class ExecutionResultExceptionDAO extends QatAbstractDao{
	
	/* Changes done for execution result exception in Services STARTS HERE */
	
	private DetachedCriteria createExecutionResultExceptionCriteria(ExecutionResultException exResultFailedMethod,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ExecutionResultException.class);
		return criteria;
	}
	
	public void saveExecutionResultException(ExecutionResultException exResultException) {
		super.saveOrUpdate(exResultException);
	}

	public ExecutionResultException saveExecutionResultExceptionWithReturn(ExecutionResultException exResultException) {
		return (ExecutionResultException) super.saveOrUpdatewithReturn(exResultException);
	}
	
}
