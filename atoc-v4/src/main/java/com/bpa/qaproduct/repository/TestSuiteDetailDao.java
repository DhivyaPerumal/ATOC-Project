package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestSuiteDetail;

@Repository("TestSuiteDetailDao")
public class TestSuiteDetailDao extends QatAbstractDao {

	public TestSuiteDetailDao() {
		super();
	}

	private DetachedCriteria createTestSuiteDetailCriteria(
			TestSuiteDetail testSuiteDetail) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TestSuiteDetail.class);
		if (testSuiteDetail.getOperatingSystem() != null) {
			criteria.add(Restrictions.like("operatingSystem",
					"%" + testSuiteDetail.getOperatingSystem() + "%")
					.ignoreCase());
		}
		if (testSuiteDetail.getBrowser() != null) {
			criteria.add(Restrictions.like("browser",
					"%" + testSuiteDetail.getBrowser() + "%").ignoreCase());
		}
		if (testSuiteDetail.getBrowserVersion() != null) {
			criteria.add(Restrictions.like("browserVersion",
					"%" + testSuiteDetail.getBrowserVersion() + "%")
					.ignoreCase());
		}
		if (testSuiteDetail.getTestSuite() != null) {
			criteria.add(Restrictions.eq("testSuite", testSuiteDetail.getTestSuite() ));
			
		}
		if (testSuiteDetail.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive", "" + testSuiteDetail.getIsActive()
					+ ""));
			
		}

		return criteria;

	}

	public int getTestSuiteDetailFilterCount(TestSuiteDetail testSuiteDetail) {
		DetachedCriteria criteria = createTestSuiteDetailCriteria(testSuiteDetail);
		return super.getObjectListCount(TestSuiteDetail.class, criteria);
	}

	public List getTestSuiteDetailList(TestSuiteDetail testSuiteDetail,int start,int limit) {
		DetachedCriteria criteria = createTestSuiteDetailCriteria(testSuiteDetail);
		criteria.addOrder(Order.desc("testSuiteDetailId"));
		return super.getObjectListByRangeByValue(TestSuiteDetail.class, criteria, start, limit);
	}

	public List getAllTestSuiteDetailList(TestSuiteDetail testSuiteDetail) {
		DetachedCriteria criteria = createTestSuiteDetailCriteria(testSuiteDetail);
		criteria.addOrder(Order.desc("testSuiteDetailId"));
		return super.getAllObjectList(TestSuiteDetail.class, criteria);
	}

	public void saveTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		super.saveOrUpdate(testSuiteDetail);
	}

	public void removeTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		super.delete(testSuiteDetail);
	}

	public TestSuiteDetail getTestSuiteDetailById(Integer Id) {
		return (TestSuiteDetail) super.find(TestSuiteDetail.class, Id);
	}

}
