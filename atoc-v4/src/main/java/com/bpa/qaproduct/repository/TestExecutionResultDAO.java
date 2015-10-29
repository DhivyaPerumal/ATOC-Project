package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestExecutionResult;

@Repository("TestExecutionResultDAO")
public class TestExecutionResultDAO extends QatAbstractDao {

	public TestExecutionResultDAO() {
		super();
	}

	private DetachedCriteria createTestExecutionResultCriteria(
			TestExecutionResult testExecutionResult) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TestExecutionResultDAO.class);
		if (testExecutionResult.getTestSuiteExecution() != null) {
			criteria.add(Restrictions.like("testSuiteExecution",
					"%" + testExecutionResult.getTestSuiteExecution() + "%")
					.ignoreCase());
		}

		return criteria;

	}

	public int getTestExeResultIdFilterCount(
			TestExecutionResult testExecutionResult) {
		DetachedCriteria criteria = createTestExecutionResultCriteria(testExecutionResult);
		return super.getObjectListCount(TestExecutionResult.class, criteria);
	}

	public List getTestExeResultIdList(TestExecutionResult testExecutionResults,int start, int limit) {
		DetachedCriteria criteria = createTestExecutionResultCriteria(testExecutionResults);
		criteria.addOrder(Order.desc("testExeResultId"));
		return super.getObjectListByRangeByValue(TestExecutionResult.class, criteria, start, limit);
	}

	public List getAlltestExeResultIdList(
			TestExecutionResult testExecutionResults) {
		DetachedCriteria criteria = createTestExecutionResultCriteria(testExecutionResults);
		criteria.addOrder(Order.desc("testExeResultId"));
		return super.getAllObjectList(TestExecutionResult.class, criteria);
	}

	public void saveTestExecutionResult(TestExecutionResult testExecutionResult) {
		super.saveOrUpdate(testExecutionResult);
	}

	public void removeTestExecutionResult(
			TestExecutionResult testExecutionResult) {
		super.delete(testExecutionResult);
	}

	public TestExecutionResult getTestExecutionResultById(Integer Id) {
		return (TestExecutionResult) super.find(TestExecutionResult.class, Id);
	}

}
