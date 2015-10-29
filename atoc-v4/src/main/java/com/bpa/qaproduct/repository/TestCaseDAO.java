package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.User;


@Repository("TestCaseDAO")
public class TestCaseDAO extends QatAbstractDao{
	
	public TestCaseDAO() {
        super();
    }
	
	private DetachedCriteria createTestCaseCriteria(TestCase testCase)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(TestCase.class); 
		if (testCase.getTestCaseName() != null)
		{
				criteria.add(Restrictions.like("testCaseName", "%"+testCase.getTestCaseName()+"%").ignoreCase());
		}
		if (testCase.getTestCaseDetail() != null)
		{
				criteria.add(Restrictions.like("testCaseDetail", "%"+testCase.getTestCaseDetail()+"%").ignoreCase());
		}
		if (testCase.getNotes() != null)
		{
				criteria.add(Restrictions.like("notes", "%"+testCase.getNotes()+"%").ignoreCase());
		}
		if (testCase.getIsActive() != null)
		{
				//criteria.add(Restrictions.like("isActive", "%"+testCase.getIsActive()+"%").ignoreCase());
				criteria.add(Restrictions.eq("isActive", "" + testCase.getIsActive()
						+ ""));
		}
		if (testCase.getTestSuiteDetail() != null)
		{
				criteria.add(Restrictions.like("testSuiteDetail", "%"+testCase.getTestSuiteDetail()+"%").ignoreCase());
		}
		
					
		return criteria;

	}
	
	public int getTestCaseFilterCount(TestCase testCase)
	{
		DetachedCriteria criteria = createTestCaseCriteria(testCase);
		return super.getObjectListCount(TestCase.class, criteria);
	}
	public List getTestCaseList(TestCase testCase,int start, int limit)
	{
		DetachedCriteria criteria = createTestCaseCriteria(testCase);
		criteria.addOrder(Order.desc("testCaseId"));
		return super.getObjectListByRangeByValue(TestCase.class, criteria,
				start, limit);		
	}
	public List getAllTestCaseList(TestCase testCase)
	{
		DetachedCriteria criteria = createTestCaseCriteria(testCase);
		criteria.addOrder(Order.desc("testCaseId"));
		return super.getAllObjectList(TestCase.class, criteria);
	}
	public void saveTestCase(TestCase testCase){
		super.saveOrUpdate(testCase);
	}
	public void removeTestCase(TestCase testCase){
		super.delete(testCase);
	}
	public TestCase getTestCaseById(Integer Id)
	{
		return (TestCase) super.find(TestCase.class, Id);		
	}
	

}
