package com.bpa.qaproduct.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.ExecutionResultDAO;

@Service("ExecutionResultService")
@Transactional
public class ExecutionResultService {

	@Autowired
	public ExecutionResultDAO executionResultDAO;

	public int getExecutionResultCount(ExecutionResult executionResult) {
		return executionResultDAO.getExecutionResultCount(executionResult);
	}
	
	public List getAllExecutionResultIdList(ExecutionResult executionResults) {
		return executionResultDAO.getAllExecutionResultIdList(executionResults);
	}

	public void saveExecutionResult(ExecutionResult executionResult) {
		executionResultDAO.saveExecutionResult(executionResult);
	}
	
	public ExecutionResult saveExecutionResultWithReturn(
			ExecutionResult executionResult) {
		return executionResultDAO.saveExecutionResultWithReturn(executionResult);
	}

	public void removeExecutionResult(ExecutionResult executionResult) {
		executionResultDAO.removeExecutionResult(executionResult);

	}

	public ExecutionResult getExecutionResultById(Integer Id) {
		return executionResultDAO.getExecutionResultById(Id);
	}
	public ExecutionResult getExecutionResultBySearchParameter(ExecutionResult executionResult) {
		return executionResultDAO.getExecutionResultBySearchParameter(executionResult);
	}
	public ExecutionResult getLastExecutionResult(ExecutionResult executionResult)
	{
		return executionResultDAO.getLastExecutionResult(executionResult);
	}
	

}
