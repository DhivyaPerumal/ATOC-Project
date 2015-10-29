package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestExecutionResult;
import com.bpa.qaproduct.entity.TestExecutionResultDetail;

@Repository("TestExecutionResultDetailDAO")
public class TestExecutionResultDetailDAO extends QatAbstractDao {

	public TestExecutionResultDetailDAO() {
		super();
	}

	private DetachedCriteria createTestExecutionResultDetailCriteria(
			TestExecutionResultDetail testExecutionResultDetail) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TestExecutionResult.class);
		if (testExecutionResultDetail.getParameterName() != null) {
			criteria.add(Restrictions.like("parameterName",
					"%" + testExecutionResultDetail.getParameterName() + "%")
					.ignoreCase());
		}
		if (testExecutionResultDetail.getParameterValue() != null) {
			criteria.add(Restrictions.like("parameterValue",
					"%" + testExecutionResultDetail.getParameterValue() + "%")
					.ignoreCase());
		}
		if (testExecutionResultDetail.getTestExecutionResult() != null) {
			/*criteria.add(Restrictions.like(
					"testExecutionResult",
					"%" + testExecutionResultDetail.getTestExecutionResult()
							+ "%").ignoreCase());*/
			criteria.add(Restrictions.eq("testExecutionResult", testExecutionResultDetail.getTestExecutionResult()));
		}
		return criteria;

	}

	public int getTestExecutionResultDetailFilterCount(
			TestExecutionResultDetail testExecutionResultDetail) {
		DetachedCriteria criteria = createTestExecutionResultDetailCriteria(testExecutionResultDetail);
		return super.getObjectListCount(TestExecutionResultDetail.class,
				criteria);
	}

	public List getTestExecutionResultDetailList(
			TestExecutionResultDetail testExecutionResultDetail,int start,int limit) {
		DetachedCriteria criteria = createTestExecutionResultDetailCriteria(testExecutionResultDetail);
		criteria.addOrder(Order.desc("testExeResultDetailId"));
		return super.getObjectListByRangeByValue(TestExecutionResultDetail.class, criteria, start, limit);
	}

	public List getAllTestExecutionResultDetailList(
			TestExecutionResultDetail testExecutionResultDetail) {
		DetachedCriteria criteria = createTestExecutionResultDetailCriteria(testExecutionResultDetail);
		criteria.addOrder(Order.desc("testExeResultDetailId"));
		return super.getAllObjectList(TestExecutionResultDetail.class, criteria);
	}

	public void saveTestExecutionResultDetail(
			TestExecutionResultDetail testExecutionResultDetail) {
		super.saveOrUpdate(testExecutionResultDetail);
	}

	public TestExecutionResultDetail saveTestExecutionResultDetailwithReturn(
			TestExecutionResultDetail testExecutionResultDetail) {
		return (TestExecutionResultDetail) super
				.saveOrUpdatewithReturn(testExecutionResultDetail);
	}

	public void removeTestExecutionResultDetail(
			TestExecutionResultDetail testExecutionResultDetail) {
		super.delete(testExecutionResultDetail);
	}

	public TestExecutionResultDetail getTestExecutionResultDetailById(Integer Id) {
		return (TestExecutionResultDetail) super.find(
				TestExecutionResultDetail.class, Id);
	}

}
