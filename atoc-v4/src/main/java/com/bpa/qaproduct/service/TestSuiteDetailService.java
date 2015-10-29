package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.repository.TestSuiteDetailDao;

@Service("TestSuiteDetailService")
@Transactional
public class TestSuiteDetailService {

	@Autowired
	public TestSuiteDetailDao testSuiteDetailDao;

	public int getTestSuiteDetailFilterCount(TestSuiteDetail testSuiteDetail) {
		return testSuiteDetailDao
				.getTestSuiteDetailFilterCount(testSuiteDetail);
	}

	public List getTestSuiteDetailList(TestSuiteDetail testSuiteDetail,int start,int limit) {
		return testSuiteDetailDao.getTestSuiteDetailList(testSuiteDetail,start,limit);
	}

	public List getAllTestSuiteDetailList(TestSuiteDetail testSuiteDetail) {
		return testSuiteDetailDao.getAllTestSuiteDetailList(testSuiteDetail);
	}

	public void saveTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		testSuiteDetailDao.saveTestSuiteDetail(testSuiteDetail);
	}

	public void removeTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		testSuiteDetailDao.removeTestSuiteDetail(testSuiteDetail);
	}

	public TestSuiteDetail getTestSuiteDetailById(Integer Id) {
		return testSuiteDetailDao.getTestSuiteDetailById(Id);
	}
}
