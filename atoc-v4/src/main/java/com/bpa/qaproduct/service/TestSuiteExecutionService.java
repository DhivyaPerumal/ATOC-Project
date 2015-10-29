package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.TestSuiteExecutionDAO;

@Service("TestSuiteExecutionService")
@Transactional
public class TestSuiteExecutionService {

	@Autowired
	public TestSuiteExecutionDAO testSuiteExecutionDAO;

	public int getTestSuiteExecutionFilterCount(
			TestSuiteExecution testSuiteExecution) {
		return testSuiteExecutionDAO
				.getTestSuiteExecutionFilterCount(testSuiteExecution);
	}

	public List getTestSuiteExecutionListPagination(
			TestSuiteExecution testSuiteExecution, int start, int limit) {
		return testSuiteExecutionDAO.getTestSuiteExecutionListPagination(
				testSuiteExecution, start, limit);
	}

	public List getAllTestSuiteExecutionList(
			TestSuiteExecution testSuiteExecution) {
		return testSuiteExecutionDAO
				.getAllTestSuiteExecutionList(testSuiteExecution);
	}

	public List getTestSuiteExecution() {
		return testSuiteExecutionDAO.getTestSuiteExecution();

	}

	public void saveTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		testSuiteExecutionDAO.saveTestSuiteExecution(testSuiteExecution);
	}

	public TestSuiteExecution saveTestSuiteExecutionwithReturn(
			TestSuiteExecution testSuiteExecution) {
		return (TestSuiteExecution) testSuiteExecutionDAO
				.saveTestSuiteExecutionwithReturn(testSuiteExecution);
	}

	public void removeTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		testSuiteExecutionDAO.removeTestSuiteExecution(testSuiteExecution);
	}

	public TestSuiteExecution getTestSuiteExecutionById(Integer Id) {
		return testSuiteExecutionDAO.getTestSuiteExecutionById(Id);
	}

	public List getTotalCountExecution(Organization organization) {
		return testSuiteExecutionDAO.getTotalCountExecution(organization);
	}

	public TestSuiteExecution getTestSuiteExecutionBySearchParam(
			TestSuiteExecution testSuiteExecution) {
		return testSuiteExecutionDAO.getTestSuiteExecutionBySearchParam(testSuiteExecution);
	}
	public List getTotalCountOfNotofication(Notification notification) {
		return testSuiteExecutionDAO.getTotalCountNotification(notification);
	}
	
	public Notification saveOrUpdateNotification(Notification notification)
	{
		return testSuiteExecutionDAO.saveOrUpdateNotification(notification);
	}
}
