package com.bpa.qaproduct.repository;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.SchedulerExecution;
import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.util.DataAccessLayerException;
import com.mysql.jdbc.PreparedStatement;

public abstract class QatAbstractDao {

	@Autowired
	protected SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final Log logger = LogFactory.getLog(QatAbstractDao.class);

	protected Object saveOnlyObj(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			session.save(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return obj;
	}

	protected Object saveOrUpdatewithReturn(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			session.saveOrUpdate(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return obj;
	}

	

	protected Object saveOrUpdate(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			logger.info("before save");
			session.saveOrUpdate(obj);
			logger.info("after save object");
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			handleException(e);
			logger.info("message");
		} finally {
			session.close();
		}
		return obj;
	}
	protected Object saveOrUpdateBlob(EmailJob emailJob) {
		Session session = sessionFactory.openSession();
		try {
			EmailJob emailObj = (EmailJob) emailJob;
			Blob blob = Hibernate.getLobCreator(session)
	                .createBlob(emailObj.getJobDataByteArray());
			emailObj.setJobDataMapObject(blob);
			logger.info("before save");
			session.saveOrUpdate(emailObj);
			logger.info("after save object");
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			handleException(e);
			logger.info("message");
		} finally {
			session.close();
		}
		return emailJob;
	}

	protected void delete(Object obj) {
		Session session = sessionFactory.openSession();
		try {
			session.delete(obj);
			session.flush();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
	}

	protected Object find(Class clazz, Integer id) {
		Object obj = null;
		Session session = sessionFactory.openSession();
		try {
			obj = session.get(clazz, id);

		} catch (HibernateException e) {
			handleException(e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return obj;
	}

	
	protected List findAll(Class clazz) {
		List objects = null;
		Session session = sessionFactory.openSession();
		try {

			Query query = session.createQuery("from " + clazz.getName());
			objects = query.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return objects;
	}

	

	protected int getObjectListCount(Class clazz,
			DetachedCriteria objectCriteria) {
		Integer resultTotal = null;
		List rowlist = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setProjection(Projections.rowCount());
			rowlist = crit.list();
			if (!rowlist.isEmpty()) {
				resultTotal = ((Long) rowlist.get(0)).intValue();
				logger.info("Total records: " + resultTotal);
			}

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return resultTotal.intValue();

	}

	protected List getObjectListByRangeByValue(Class clazz,
			DetachedCriteria objectCriteria, int start, int limit) {
		List list = null;

		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setFirstResult(start);
			crit.setMaxResults(limit);
			list = crit.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}

	protected List getAllObjectList(Class clazz, DetachedCriteria objectCriteria) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			list = crit.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}
	
	

	
	protected List getAllObjectList1(Class clazz, DetachedCriteria objectCriteria,int start,int limit) {
		List list = null;
		Session session = sessionFactory.openSession();
		try {

			Criteria crit = objectCriteria.getExecutableCriteria(session);
			crit.setFirstResult(start);
			crit.setMaxResults(limit);
			list = crit.list();

		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}
	
	protected Object getUniqueObject(Class clazz,DetachedCriteria objectCriteria) {
        Object obj = null;
       
        
   	 	Session session = sessionFactory.openSession();
        try {
           
       	 Criteria crit = objectCriteria.getExecutableCriteria(session);
       	 List list =  crit.list();
       	 if (crit.list().size() != 0)
       	 {
       		 obj = list.get(0);
       	 }
       	 
        } catch (HibernateException e) {
            handleException(e);
        } finally {
        	session.close();
        }       
        
        return obj;
    }
	
	
	
	
// I really dont no what is this method need to discuss with team - satya
	public List getTestSuiteExecution() {
		TestSuite testSuite = new TestSuite();
		List<TestSuite> testSuiteList = new ArrayList();
		logger.info("Inside Condition");
		Session session = sessionFactory.openSession();
		logger.info("Condition Checked");
		try {

			Query query = session
					.createQuery("from TestSuite group by project");
			logger.info("Test execution Method");
			testSuiteList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return testSuiteList;

	}
	
	// I really dont no what is this method need to discuss with team - satya
	public List getTotalCountExecution(Organization organization) {
		List list = null;

		Integer testExecutionCount = 0;
		String s = "";
		Session session = sessionFactory.openSession();

		try {
			logger.info("Before TestSuiteExecution Query");
			String queryString = "select count(*), p1.project_name from  test_suite_execution t1 INNER JOIN test_suite t2, project p1 where t1.EXECUTION_STATUS = 'COMPLETED' and t1.TEST_SUITE_ID = t2.TEST_SUITE_ID and t2.project_id = p1.project_id and p1.ORGANIZATION_ID = :orgId GROUP BY p1.PROJECT_ID";

			Query query = session.createSQLQuery(queryString).setParameter(
					"orgId", organization.getOrganizationId());
			list = query.list();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			session.close();
		}
		return list;

	}
	
	
	
	
	
	protected void handleException(HibernateException e)
			throws DataAccessLayerException {

		throw new DataAccessLayerException(e);
	}



}