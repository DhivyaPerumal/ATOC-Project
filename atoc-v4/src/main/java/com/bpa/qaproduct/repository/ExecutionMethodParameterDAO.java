package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.bpa.qaproduct.entity.ExecutionMethodParameter;

@Repository("ExecutionMethodParameterDAO")
public class ExecutionMethodParameterDAO extends QatAbstractDao {

	public ExecutionMethodParameterDAO() {

		super();
	}

	private DetachedCriteria createExecutionMethodParameterCriteria(
			ExecutionMethodParameter executionMethodParameter) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ExecutionMethodParameter.class);
		if (executionMethodParameter.getParameterValue() != null) {
			criteria.add(Restrictions.like("parameterValue",
					"%" + executionMethodParameter.getParameterValue() + "%")
					.ignoreCase());
		}
		if (executionMethodParameter.getExecutionResultDetail() != null)
		{
			criteria.add(Restrictions.eq("executionResultDetail",
					executionMethodParameter.getExecutionResultDetail()));
		}

		return criteria;

	}

	public int getExecutionMethodParameterIdFilterCount(
			ExecutionMethodParameter executionMethodParameter) {
		DetachedCriteria criteria = createExecutionMethodParameterCriteria(executionMethodParameter);
		return super.getObjectListCount(ExecutionMethodParameter.class,
				criteria);
	}

	/*public List getExecutionMethodParameterIdList(
			ExecutionMethodParameter executionMethodParameter) {
		DetachedCriteria criteria = createExecutionMethodParameterCriteria(executionMethodParameter);
		criteria.addOrder(Order.desc("executionParameterId"));
		return super.getAllObjectList(ExecutionMethodParameter.class,
				criteria);
	}*/

	public List getAllExecutionMethodParameterIdList(
			ExecutionMethodParameter executionMethodParameter) {
		DetachedCriteria criteria = createExecutionMethodParameterCriteria(executionMethodParameter);
		criteria.addOrder(Order.desc("executionParameterId"));
		return super.getAllObjectList(ExecutionMethodParameter.class, criteria);
	}

	public void saveExecutionMethodParameter(
			ExecutionMethodParameter executionMethodParameter) {
		super.saveOrUpdatewithReturn(executionMethodParameter);
	}

	public ExecutionMethodParameter saveExecutionMethodParameterWithReturn(
			ExecutionMethodParameter executionMethodParameter) {
		return (ExecutionMethodParameter) super
				.saveOrUpdatewithReturn(executionMethodParameter);
	}

	public void removeExecutionMethodParameter(
			ExecutionMethodParameter executionMethodParameter) {
		super.delete(executionMethodParameter);
	}

	public ExecutionMethodParameter getExecutionMethodParameterById(Integer Id) {
		return (ExecutionMethodParameter) super.find(
				ExecutionMethodParameter.class, Id);
	}

}
