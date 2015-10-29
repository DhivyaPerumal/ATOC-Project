package com.bpa.qaproduct.repository;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.ExecutionService;

@Repository("ExecutionResultDAO")
public class ExecutionResultDAO extends QatAbstractDao {

	protected final Log logger = LogFactory.getLog(ExecutionResultDAO.class);

	public ExecutionResultDAO() {
		super();
	}

	private DetachedCriteria createExecutionResultCriteria(
			ExecutionResult executionResult,String matchType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ExecutionResult.class);

		if (executionResult.getStartTime() != null) {
			criteria.add(Restrictions.like("startTime",
					"%" + executionResult.getStartTime() + "%").ignoreCase());
		}
		if (executionResult.getEndTime() != null) {
			criteria.add(Restrictions.like("endTime",
					"%" + executionResult.getEndTime() + "%").ignoreCase());
		}
		if (executionResult.getDuration() != null) {
			criteria.add(Restrictions.like("duration",
					"%" + executionResult.getDuration() + "%").ignoreCase());
		}
		if (executionResult.getUpdatedBy() != null) {
			criteria.add(Restrictions.eq("updatedBy",
					executionResult.getUpdatedBy()));
		}
		if (executionResult.getExecutionStatus() != null) {
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("executionStatus",
						""+executionResult.getExecutionStatus()+""));
			}
			else
			{
				criteria.add(Restrictions.like("executionStatus",
						"%" + executionResult.getExecutionStatus() + "%")
						.ignoreCase());
			}
			
		}
		if (executionResult.getTestSuiteExecution() != null) {
			
			criteria.add(Restrictions.eq("testSuiteExecution",
						executionResult.getTestSuiteExecution()));
			
			
		}
		if (executionResult.getTestExecutionName() != null) {
			
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("testExecutionName",
						""+executionResult.getTestExecutionName()+""));
			}
			else
			{
				criteria.add(Restrictions.like("testExecutionName",
						"%" + executionResult.getTestExecutionName() + "%")
						.ignoreCase());
			}
			
		}

		return criteria;

	}

	public int getExecutionResultCount(ExecutionResult executionResult) {
		DetachedCriteria criteria = createExecutionResultCriteria(executionResult,"");
		return super.getObjectListCount(ExecutionResult.class, criteria);
	}

	public List getAllExecutionResultIdList(ExecutionResult executionResults) {
		DetachedCriteria criteria = createExecutionResultCriteria(executionResults,"");
		criteria.addOrder(Order.desc("executionResultId"));
		return super.getAllObjectList(ExecutionResult.class, criteria);
	}

	/*public ExecutionResult ReportCount(Integer testSuiteExecId,
			String testExecutionName) {
		return (ExecutionResult) super.ReportCount(testSuiteExecId,
				testExecutionName);
	}*/

	public void saveExecutionResult(ExecutionResult executionResult) {
		super.saveOrUpdatewithReturn(executionResult);
	}

	public ExecutionResult saveExecutionResultWithReturn(
			ExecutionResult executionResult) {

		return (ExecutionResult) super.saveOrUpdatewithReturn(executionResult);
	}

	public void removeExecutionResult(ExecutionResult executionResult) {
		super.delete(executionResult);
	}

	public ExecutionResult getExecutionResultById(Integer Id) {
		return (ExecutionResult) super.find(ExecutionResult.class, Id);
	}
	
	public ExecutionResult getExecutionResultBySearchParameter(ExecutionResult executionResult) {
		DetachedCriteria criteria = createExecutionResultCriteria(executionResult,"exact");
		return (ExecutionResult) super.getUniqueObject(ExecutionResult.class, criteria);	
	}
	public ExecutionResult getLastExecutionResult(ExecutionResult executionResult)
	{
		ExecutionResult lastExecutionResult = null;
		DetachedCriteria criteria = createExecutionResultCriteria(executionResult,"exact");
		criteria.addOrder(Order.desc("executionResultId"));
		List executionResultList= super.getAllObjectList(ExecutionResult.class, criteria);
		if ((executionResultList != null) && (executionResultList.size() != 0))
		{
			lastExecutionResult = (ExecutionResult) executionResultList.get(0);
		}			
		return lastExecutionResult;
		
	}

	

}
