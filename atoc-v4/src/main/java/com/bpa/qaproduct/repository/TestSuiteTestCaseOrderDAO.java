package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestSuiteTestCaseOrder;

@Repository("TestSuiteTestCaseOrderDAO")
public class TestSuiteTestCaseOrderDAO extends QatAbstractDao {

	public TestSuiteTestCaseOrderDAO() {
		super();
	}

	private DetachedCriteria createTestSuiteTestCaseOrderCriteria(
			TestSuiteTestCaseOrder testCaseOrder) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TestSuiteTestCaseOrder.class);
		if (testCaseOrder.getTestCase() != null) {
			criteria.add(Restrictions.like("testCase",
					"%" + testCaseOrder.getTestCase() + "%").ignoreCase());
		}
		if (testCaseOrder.getOrderNo() != null) {
			criteria.add(Restrictions.like("orderNo",
					"%" + testCaseOrder.getOrderNo() + "%").ignoreCase());
		}

		return criteria;

	}

	public int getTestSuiteTestCaseOrderDetailFilterCount(
			TestSuiteTestCaseOrder testCaseOrder) {
		DetachedCriteria criteria = createTestSuiteTestCaseOrderCriteria(testCaseOrder);
		return super.getObjectListCount(TestSuiteTestCaseOrder.class, criteria);
	}

	public List getTestSuiteTestCaseOrderList(
			TestSuiteTestCaseOrder testCaseOrder,int start,int limit) {
		DetachedCriteria criteria = createTestSuiteTestCaseOrderCriteria(testCaseOrder);
		criteria.addOrder(Order.desc("testSuiterDetail"));
		return super.getObjectListByRangeByValue(TestSuiteTestCaseOrder.class,
				criteria,start,limit);
	}

	public List getAllTestSuiteTestCaseOrderList(
			TestSuiteTestCaseOrder testCaseOrder) {
		DetachedCriteria criteria = createTestSuiteTestCaseOrderCriteria(testCaseOrder);
		criteria.addOrder(Order.desc("testSuiterDetail"));
		return super.getAllObjectList(TestSuiteTestCaseOrder.class, criteria);
	}

	public void saveTestSuiteTestCaseOrder(TestSuiteTestCaseOrder testCaseOrder) {
		super.saveOrUpdate(testCaseOrder);
	}

	public void removeTestSuiteTestCaseOrder(
			TestSuiteTestCaseOrder testCaseOrder) {
		super.delete(testCaseOrder);
	}

	public TestSuiteTestCaseOrder getTestSuiteTestCaseOrderById(Integer Id) {
		return (TestSuiteTestCaseOrder) super.find(
				TestSuiteTestCaseOrder.class, Id);
	}
}
