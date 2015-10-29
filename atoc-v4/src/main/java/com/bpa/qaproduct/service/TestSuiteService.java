package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.repository.TestSuiteDAO;

@Service("TestSuiteService")
@Transactional
public class TestSuiteService {

	@Autowired
	public TestSuiteDAO testSuiteDAO;

	public int getTestSuiteFilterCount(TestSuite testSuite) {
		return testSuiteDAO.getTestSuiteFilterCount(testSuite);
	}

	public List<TestSuite> getTestSuiteListPagination(TestSuite testSuite,
			int start, int limit) {
		return testSuiteDAO.getTestSuiteListPagination(testSuite, start, limit);
	}

	public List getAllTestSuiteList(TestSuite testSuite) {
		return testSuiteDAO.getAllTestSuiteList(testSuite);
	}

	public List getTestSuite(int projectId) {
		return testSuiteDAO.getTestSuite(projectId);

	}

	public void saveTestSuite(TestSuite testSuite) {
		testSuiteDAO.saveTestSuite(testSuite);
	}

	public void removeTestSuite(TestSuite testSuite) {
		testSuiteDAO.removeTestSuite(testSuite);
	}

	public TestSuite getTestSuiteById(Integer Id) {
		return testSuiteDAO.getTestSuiteById(Id);
	}
}
