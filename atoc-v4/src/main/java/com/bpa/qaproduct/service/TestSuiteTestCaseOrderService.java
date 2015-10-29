package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.TestSuiteTestCaseOrder;
import com.bpa.qaproduct.repository.TestSuiteTestCaseOrderDAO;

@Service("TestSuiteTestCaseOrderService")
@Transactional
public class TestSuiteTestCaseOrderService {

	@Autowired
	public TestSuiteTestCaseOrderDAO tesCaseOrderDAO;

	public int getTestSuiteTestCaseOrderDetailFilterCount(
			TestSuiteTestCaseOrder testSuiteTestCaseOrder) {
		return tesCaseOrderDAO
				.getTestSuiteTestCaseOrderDetailFilterCount(testSuiteTestCaseOrder);
	}

	public List getTestSuiteTestCaseOrderList(
			TestSuiteTestCaseOrder testSuiteTestCaseOrder,int start,int limit) {
		return tesCaseOrderDAO
				.getTestSuiteTestCaseOrderList(testSuiteTestCaseOrder,start,limit);
	}

	public List getAllTestSuiteTestCaseOrderList(
			TestSuiteTestCaseOrder testSuiteTestCaseOrder) {
		return tesCaseOrderDAO
				.getAllTestSuiteTestCaseOrderList(testSuiteTestCaseOrder);
	}

	public void saveTestSuiteTestCaseOrder(
			TestSuiteTestCaseOrder testSuiteTestCaseOrder) {
		tesCaseOrderDAO.saveTestSuiteTestCaseOrder(testSuiteTestCaseOrder);
	}

	public void removeTestSuiteTestCaseOrder(
			TestSuiteTestCaseOrder testSuiteTestCaseOrder) {
		tesCaseOrderDAO.removeTestSuiteTestCaseOrder(testSuiteTestCaseOrder);
	}

	public TestSuiteTestCaseOrder getTestSuiteTestCaseOrderById(Integer Id) {
		return tesCaseOrderDAO.getTestSuiteTestCaseOrderById(Id);
	}
}
