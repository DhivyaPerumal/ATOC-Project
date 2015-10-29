package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.TestCaseStep;


@Repository("TestCaseStepDAO")
public class TestCaseStepDAO extends QatAbstractDao{

	
	public TestCaseStepDAO() {
        super();
    }
	
	private DetachedCriteria createTestCaseStepCriteria(TestCaseStep testCaseStep)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(TestCaseStep.class); 
		if (testCaseStep.getCommand() != null)
		{
				criteria.add(Restrictions.like("command", "%"+testCaseStep.getCommand()+"%").ignoreCase());
		}
		if (testCaseStep.getLocator() != null)
		{
				criteria.add(Restrictions.like("locator", "%"+testCaseStep.getLocator()+"%").ignoreCase());
		}
		if (testCaseStep.getValue() != null)
		{
				criteria.add(Restrictions.like("value", "%"+testCaseStep.getValue()+"%").ignoreCase());
		}
		if (testCaseStep.getOrder_no() != null)
		{
				criteria.add(Restrictions.like("order_no", "%"+testCaseStep.getOrder_no()+"%").ignoreCase());
		}
		if (testCaseStep.getTestCase() != null)
		{
				criteria.add(Restrictions.like("testCase", "%"+testCaseStep.getTestCase()+"%").ignoreCase());
		}
					
		return criteria;

	}
	
	public int getTestCaseStepFilterCount(TestCaseStep testCaseStep)
	{
		DetachedCriteria criteria = createTestCaseStepCriteria(testCaseStep);
		return super.getObjectListCount(TestCaseStep.class, criteria);
	}
	public List getTestCaseStepList(TestCaseStep testCaseStep,int start,int limit)
	{
		DetachedCriteria criteria = createTestCaseStepCriteria(testCaseStep);
		criteria.addOrder(Order.desc("testCaseStepId"));
		return super.getObjectListByRangeByValue(TestCaseStep.class, criteria,start,limit);
	}
	public List getAllTestCaseStepList(TestCaseStep testCaseStep)
	{
		DetachedCriteria criteria = createTestCaseStepCriteria(testCaseStep);
		criteria.addOrder(Order.desc("testCaseStepId"));
		return super.getAllObjectList(TestCaseStep.class, criteria);
	}
	public void saveTestCaseStep(TestCaseStep testCaseStep){
		super.saveOrUpdate(testCaseStep);
	}
	public void removeTestCaseStep(TestCaseStep testCaseSteps){
		super.delete(testCaseSteps);
	}
	public TestCaseStep getTestCaseStepById(Integer Id)
	{
		return (TestCaseStep) super.find(TestCaseStep.class, Id);		
	}
}
