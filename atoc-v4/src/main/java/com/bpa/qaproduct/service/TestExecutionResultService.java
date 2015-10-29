package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestExecutionResult;
import com.bpa.qaproduct.repository.TestExecutionResultDAO;

@Service("TestExecutionResultService")
@Transactional
public class TestExecutionResultService {

	@Autowired
	public TestExecutionResultDAO testExecutionResultDAO;

	public int getTestExeResultIdFilterCount(
			TestExecutionResult testExecutionResult) {
		return testExecutionResultDAO
				.getTestExeResultIdFilterCount(testExecutionResult);
	}

	public List getTestExeResultIdList(TestExecutionResult testExecutionResult,int start, int limit) {
		return testExecutionResultDAO
				.getTestExeResultIdList(testExecutionResult,start,limit);
	}

	public List getAlltestExeResultIdList(
			TestExecutionResult testExecutionResult) {
		return testExecutionResultDAO
				.getAlltestExeResultIdList(testExecutionResult);
	}

	public void saveTestExecutionResult(TestExecutionResult testExecutionResult) {
		testExecutionResultDAO.saveTestExecutionResult(testExecutionResult);
	}

	public void removeTestExecutionResult(
			TestExecutionResult testExecutionResult) {
		testExecutionResultDAO.removeTestExecutionResult(testExecutionResult);

	}

	public TestExecutionResult getTestExecutionResultById(Integer Id) {
		return testExecutionResultDAO.getTestExecutionResultById(Id);
	}
}
