package com.bpa.qaproduct.repository;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.util.DateHelper;

@Repository("TestSuiteDAO")
public class TestSuiteDAO extends QatAbstractDao {

	public TestSuiteDAO() {
		super();
	}

	private DetachedCriteria createTestSuiteCriteria(TestSuite testSuite) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TestSuite.class);

		if (testSuite.getProject() != null) 
		{

			if (testSuite.getProject().getProjectId() != null)
			{
				criteria.add(Restrictions.eq("project", testSuite.getProject()));
			}
			else
			{
				if (testSuite.getProject().getProjectName() != null) {

					criteria.createCriteria("project", "project").add(
							Restrictions.like(
									"project.projectName",
									"%" + testSuite.getProject().getProjectName()
											+ "%").ignoreCase());

				}
			}
			
			

		}

		logger.info("testSuitename" + testSuite.getSuiteName());
		if (testSuite.getSuiteName() != null) {
			logger.info("SuiteName");
			criteria.add(Restrictions.like("suiteName",
					"%" + testSuite.getSuiteName() + "%").ignoreCase());
		}

		if (testSuite.getCreatedOn() != null) {
			Date fromDate = DateHelper.getDateWithoutTime(testSuite.getCreatedOn());
			Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(testSuite.getCreatedOn()));
			criteria.add(Restrictions.ge("createdOn", fromDate));
			criteria.add(Restrictions.le("createdOn", toDate));
			
			
		//	criteria.add(Restrictions.eq("createdOn", testSuite.getCreatedOn()));
		}
		if (testSuite.getNotes() != null) {
			criteria.add(Restrictions.like("notes",
					"%" + testSuite.getNotes() + "%").ignoreCase());
		}
		if (testSuite.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive", "" + testSuite.getIsActive()
					+ ""));		
		}
		if (testSuite.getOrganization() != null)
		{
			criteria.add(Restrictions.eq("organization", testSuite.getOrganization()));
		}

		return criteria;

	}

	public int getTestSuiteFilterCount(TestSuite testSuite) {
		DetachedCriteria criteria = createTestSuiteCriteria(testSuite);
		return super.getObjectListCount(TestSuite.class, criteria);
	}

	public List<TestSuite> getTestSuiteListPagination(TestSuite testSuite,
			int start, int limit) {
		DetachedCriteria criteria = createTestSuiteCriteria(testSuite);
		criteria.addOrder(Order.desc("testSuiteId"));		
		return super.getObjectListByRangeByValue(TestSuite.class, criteria,
				start, limit);
	}

	public List getTestSuite(int projectId) {
		return null;

	}

	public List getAllTestSuiteList(TestSuite testSuite) {
		DetachedCriteria criteria = createTestSuiteCriteria(testSuite);
		criteria.addOrder(Order.desc("testSuiteId"));
		return super.getAllObjectList(TestSuite.class, criteria);
	}

	public void saveTestSuite(TestSuite testSuite) {
		super.saveOrUpdate(testSuite);
	}

	public void removeTestSuite(TestSuite testSuite) {
		super.delete(testSuite);
	}

	public TestSuite getTestSuiteById(Integer Id) {
		return (TestSuite) super.find(TestSuite.class, Id);
	}
}
