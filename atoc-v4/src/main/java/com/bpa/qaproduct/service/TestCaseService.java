package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.repository.TestCaseDAO;

@Service("TestCaseService")
@Transactional
public class TestCaseService {

	@Autowired
	public TestCaseDAO testCaseDAO;

	public int getTestCaseFilterCount(TestCase testCase) {
		return testCaseDAO.getTestCaseFilterCount(testCase);
	}

	public List getTestCaseList(TestCase testCase,int start, int limit) {
		return testCaseDAO.getTestCaseList(testCase,start,limit);
	}

	public List getAllTestCaseList(TestCase testCase) {
		return testCaseDAO.getAllTestCaseList(testCase);
	}

	public void saveTestCase(TestCase testCase) {
		testCaseDAO.saveTestCase(testCase);
	}

	public void removeTestCase(TestCase testCase) {
		testCaseDAO.removeTestCase(testCase);
	}

	public TestCase getTestCaseById(Integer Id) {
		return testCaseDAO.getTestCaseById(Id);
	}
	

}
