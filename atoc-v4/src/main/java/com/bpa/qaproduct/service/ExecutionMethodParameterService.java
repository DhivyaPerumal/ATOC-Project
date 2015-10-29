package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.ExecutionMethodParameter;
import com.bpa.qaproduct.repository.ExecutionMethodParameterDAO;

@Service("ExecutionMethodParameterService")
@Transactional
public class ExecutionMethodParameterService {

	@Autowired
	public ExecutionMethodParameterDAO executionMethodParameterDAO;

	public int getExecutionMethodParameterIdFilterCount(
			ExecutionMethodParameter executionMethodParameter) {
		return executionMethodParameterDAO
				.getExecutionMethodParameterIdFilterCount(executionMethodParameter);
	}

	/*public List getExecutionMethodParameterIdList(
			ExecutionMethodParameter executionMethodParameter) {
		return executionMethodParameterDAO
				.getExecutionMethodParameterIdList(executionMethodParameter);
	}*/

	public List getAllExecutionMethodParameterIdList(
			ExecutionMethodParameter executionMethodParameter) {
		return executionMethodParameterDAO
				.getAllExecutionMethodParameterIdList(executionMethodParameter);
	}

	public void saveExecutionMethodParameter(
			ExecutionMethodParameter executionMethodParameter) {
		executionMethodParameterDAO
				.saveExecutionMethodParameter(executionMethodParameter);

	}

	public void removeExecutionMethodParameter(
			ExecutionMethodParameter executionMethodParameter) {
		executionMethodParameterDAO
				.removeExecutionMethodParameter(executionMethodParameter);
	}

	public ExecutionMethodParameter getExecutionMethodParameterById(Integer Id) {
		return executionMethodParameterDAO.getExecutionMethodParameterById(Id);
	}

}
