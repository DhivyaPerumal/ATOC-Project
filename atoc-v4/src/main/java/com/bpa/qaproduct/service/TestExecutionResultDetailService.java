package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestExecutionResultDetail;
import com.bpa.qaproduct.repository.TestExecutionResultDetailDAO;

@Service("TestExecutionResultDetailService")
@Transactional
public class TestExecutionResultDetailService {

	@Autowired
	public TestExecutionResultDetailDAO testExecutionResultDetailDAO;

	public int getTestExecutionResultDetailFilterCount(
			TestExecutionResultDetail testExecutionResultDetail) {

		return testExecutionResultDetailDAO
				.getTestExecutionResultDetailFilterCount(testExecutionResultDetail);

	}

	public List getTestExecutionResultDetailList(
			TestExecutionResultDetail testExecutionResultDetail,int start,int limit) {

		return testExecutionResultDetailDAO
				.getTestExecutionResultDetailList(testExecutionResultDetail,start,limit);
	}

	public List getAllTestExecutionResultDetailList(
			TestExecutionResultDetail testExecutionResultDetail) {

		return testExecutionResultDetailDAO
				.getAllTestExecutionResultDetailList(testExecutionResultDetail);
	}

	public void saveTestExecutionResultDetail(
			TestExecutionResultDetail testExecutionResultDetail) {

		testExecutionResultDetailDAO
				.saveTestExecutionResultDetail(testExecutionResultDetail);
	}

	public TestExecutionResultDetail saveTestExecutionResultDetailwithReturn(
			TestExecutionResultDetail testExecutionResultDetail) {

		return testExecutionResultDetailDAO
				.saveTestExecutionResultDetailwithReturn(testExecutionResultDetail);
	}

	public void removeTestExecutionResultDetail(
			TestExecutionResultDetail testExecutionResultDetail) {
		testExecutionResultDetailDAO
				.removeTestExecutionResultDetail(testExecutionResultDetail);
	}

	public TestExecutionResultDetail getTestExecutionResultDetailById(Integer Id) {

		return testExecutionResultDetailDAO
				.getTestExecutionResultDetailById(Id);
	}
}
