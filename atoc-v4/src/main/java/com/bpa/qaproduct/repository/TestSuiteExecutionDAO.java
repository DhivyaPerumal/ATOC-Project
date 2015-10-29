package com.bpa.qaproduct.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;

@Repository("TestSuiteExecutionDAO")
public class TestSuiteExecutionDAO extends QatAbstractDao {

	public TestSuiteExecutionDAO() {
		super();
	}

	private DetachedCriteria createTestSuiteExecutionCriteria(
			TestSuiteExecution testSuiteExecution,String matchType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TestSuiteExecution.class);
		if (testSuiteExecution.getScheduledOn() != null) {
			criteria.add(Restrictions.like("scheduledOn",
					"%" + testSuiteExecution.getScheduledOn() + "%")
					.ignoreCase());
		}
		if (testSuiteExecution.getBrowser() != null) {
			criteria.add(Restrictions.like("browser",
					"%" + testSuiteExecution.getBrowser() + "%").ignoreCase());
		}
		if (testSuiteExecution.getOperatingSystem() != null) {
			criteria.add(Restrictions.like("operatingSystem",
					"%" + testSuiteExecution.getOperatingSystem() + "%")
					.ignoreCase());
		}
		if (testSuiteExecution.getExecutionType() != null) {
			criteria.add(Restrictions.like("executionType",
					"%" + testSuiteExecution.getExecutionType() + "%")
					.ignoreCase());
		}
		if (testSuiteExecution.getPriority() != null) {
			criteria.add(Restrictions.like("priority",
					"%" + testSuiteExecution.getPriority() + "%").ignoreCase());
		}
		if (testSuiteExecution.getLogLevel() != null) {
			criteria.add(Restrictions.like("logLevel",
					"%" + testSuiteExecution.getLogLevel() + "%").ignoreCase());
		}
		if (testSuiteExecution.getNotificationEmail() != null) {
			criteria.add(Restrictions.like("notificationEmail",
					"%" + testSuiteExecution.getNotificationEmail() + "%")
					.ignoreCase());
		}
		if (testSuiteExecution.getFatalErrorNotificationMail() != null) {
			criteria.add(Restrictions.like(
					"fatalErrorNotificationMail",
					"%" + testSuiteExecution.getFatalErrorNotificationMail()
							+ "%").ignoreCase());
		}
		if (testSuiteExecution.getStatus() != null) {
			criteria.add(Restrictions.like("status",
					"%" + testSuiteExecution.getStatus() + "%").ignoreCase());
		}
		/*if (testSuiteExecution.getTestSuiteDetail() != null) {
			criteria.add(Restrictions.eq("testSuiteDetail", testSuiteExecution.getTestSuiteDetail()));
		}*/
		if (testSuiteExecution.getExecCompleteOn() != null) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(testSuiteExecution.getExecCompleteOn());
			cal.add(Calendar.DATE, 1);
			Date higherDate = cal.getTime();

			criteria.add(Restrictions.between("execCompleteOn",
					testSuiteExecution.getExecCompleteOn(), higherDate));

		}

		if (testSuiteExecution.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive", "" + testSuiteExecution.getIsActive()
					+ ""));
		}
		if (testSuiteExecution.getTestSuite() != null) {
			criteria.add(Restrictions.eq("testSuite",
					testSuiteExecution.getTestSuite()));
		}
		if (testSuiteExecution.getTestSuiteExecName() != null) {
			
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("testSuiteExecName", ""+testSuiteExecution.getTestSuiteExecName()+"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("testSuiteExecName",
						"%" + testSuiteExecution.getTestSuiteExecName() + "%")
						.ignoreCase());
			}
			
			
		}

		return criteria;

	}
	private DetachedCriteria createTestSuiteExecutionCriteriaNotification(Notification notification,String matchType)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		if (notification.getNotificationStatus() != null) {
			criteria.add(Restrictions.eq("notificationStatus",
					"%" + notification.getNotificationStatus() + "%")
					.ignoreCase());
		}
		return null;
		
		
	}

	public int getTestSuiteExecutionFilterCount(
			TestSuiteExecution testSuiteExecution) {
		DetachedCriteria criteria = createTestSuiteExecutionCriteria(testSuiteExecution,"");
		criteria.add(Restrictions.eq("executionStatus", testSuiteExecution.getExecutionStatus()).ignoreCase());

		return super.getObjectListCount(TestSuiteExecution.class, criteria);
	}

	public List getTestSuiteExecutionListPagination(
			TestSuiteExecution testSuiteExecution, int start, int limit) {
		DetachedCriteria criteria = createTestSuiteExecutionCriteria(testSuiteExecution,"");
		criteria.addOrder(Order.desc("testSuiteExecId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("testSuite", FetchMode.SELECT);
		criteria.add(Restrictions.eq("executionStatus", testSuiteExecution.getExecutionStatus()).ignoreCase());
//		criteria.setFetchMode("schedulerTestSuiteExecution", FetchMode.JOIN);
		return super.getObjectListByRangeByValue(TestSuiteExecution.class,
				criteria, start, limit);
	}

	public List getAllTestSuiteExecutionList(
			TestSuiteExecution testSuiteExecution) {
		DetachedCriteria criteria = createTestSuiteExecutionCriteria(testSuiteExecution,"");
		criteria.addOrder(Order.desc("testSuiteExecId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("executionResult", FetchMode.SELECT);
		criteria.setFetchMode("schedulerTestSuiteExecution", FetchMode.SELECT);
		criteria.add(Restrictions.eq("executionStatus", testSuiteExecution.getExecutionStatus()).ignoreCase());
		return super.getAllObjectList(TestSuiteExecution.class, criteria);
	}

	
	public TestSuiteExecution getTestSuiteExecutionBySearchParam(
			TestSuiteExecution testSuiteExecution) {
		DetachedCriteria criteria = createTestSuiteExecutionCriteria(testSuiteExecution,"exact");
		criteria.addOrder(Order.desc("testSuiteExecId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("executionResult", FetchMode.SELECT);
		criteria.setFetchMode("schedulerTestSuiteExecution", FetchMode.SELECT);
		return (TestSuiteExecution) super.getUniqueObject(TestSuiteExecution.class,
				criteria);
	}

	public void saveTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		super.saveOrUpdate(testSuiteExecution);
	}

	public TestSuiteExecution saveTestSuiteExecutionwithReturn(
			TestSuiteExecution testSuiteExecution) {
		return (TestSuiteExecution) super
				.saveOrUpdatewithReturn(testSuiteExecution);
	}

	public List getTestSuiteExecution() {
		return super.getTestSuiteExecution();

	}

	public void removeTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		super.delete(testSuiteExecution);
	}

	public TestSuiteExecution getTestSuiteExecutionById(Integer Id) {
		return (TestSuiteExecution) super.find(TestSuiteExecution.class, Id);
	}

	public List getTotalCountExecution(Organization organization) {
		return super.getTotalCountExecution(organization);

	}
	
	public List getTotalCountNotification(Notification notification) {
		
		DetachedCriteria criteria = createTestSuiteExecutionCriteriaNotification(notification,"");
		criteria.addOrder(Order.desc("notificationStatus"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return super.getAllObjectList(Notification.class, criteria);
		

	}
	
	public Notification saveOrUpdateNotification(Notification notification)
	{
		return (Notification) super.saveOrUpdate(notification);
	}
}
