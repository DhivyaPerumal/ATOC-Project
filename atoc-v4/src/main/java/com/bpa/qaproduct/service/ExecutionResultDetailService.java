package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.repository.ExecutionResultDetailDAO;

@Service("ExecutionResultDetailService")
@Transactional
public class ExecutionResultDetailService {

	@Autowired
	public ExecutionResultDetailDAO executionResultDetailDAO;

	public int getExecutionResultDetailFilterCount(
			ExecutionResultDetail executionResultDetail) {
		return executionResultDetailDAO
				.getExecutionResultDetailFilterCount(executionResultDetail);
	}

	public List getExecutionResultDetailList(
			ExecutionResultDetail executionResultDetail, int start, int limit) {
		return executionResultDetailDAO
				.getExecutionResultDetailList(executionResultDetail,start,limit);
	}

	public List getAllExecutionResultDetailList(
			ExecutionResultDetail executionResultDetail) {
		return executionResultDetailDAO
				.getAllExecutionResultDetailList(executionResultDetail);
	}

	public void saveExecutionResultDetail(
			ExecutionResultDetail executionResultDetail) {
		executionResultDetailDAO
				.saveExecutionResultDetail(executionResultDetail);
	}

	public ExecutionResultDetail saveExecutionResultDetailwithReturn(
			ExecutionResultDetail executionResultDetail) {

		return executionResultDetailDAO
				.saveExecutionResultDetailwithReturn(executionResultDetail);
	}

	public void removeExecutionResultDetail(
			ExecutionResultDetail executionResultDetail) {
		executionResultDetailDAO
				.removeExecutionResultDetail(executionResultDetail);
	}

	public ExecutionResultDetail getExecutionResultDetailById(Integer Id) {
		return executionResultDetailDAO.getExecutionResultDetailById(Id);
	}

}
