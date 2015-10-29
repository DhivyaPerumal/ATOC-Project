package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestExecutionResult;

@Repository("ExecutionResultDetailDAO")
public class ExecutionResultDetailDAO extends QatAbstractDao {

	public ExecutionResultDetailDAO() {
		super();
	}

	private DetachedCriteria createExecutionResultDetailCriteria(
			ExecutionResultDetail executionResultDetail) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ExecutionResultDetail.class);
		if (executionResultDetail.getTestMethodName() != null) {
			criteria.add(Restrictions.like("testMethodName",
					"%" + executionResultDetail.getTestMethodName() + "%")
					.ignoreCase());
		}
		if (executionResultDetail.getDuration() != null) {
			criteria.add(Restrictions.like("duration",
					"%" + executionResultDetail.getDuration() + "%")
					.ignoreCase());
		}
		if (executionResultDetail.getStatus() != null) {
			criteria.add(Restrictions.like("status",
					"%" + executionResultDetail.getStatus() + "%").ignoreCase());
		}
		if (executionResultDetail.getStartedAt() != null) {
			criteria.add(Restrictions.like("startedAt",
					"%" + executionResultDetail.getStartedAt() + "%")
					.ignoreCase());
		}
		if (executionResultDetail.getFinishedAt() != null) {
			criteria.add(Restrictions.like("finishedAt",
					"%" + executionResultDetail.getFinishedAt() + "%")
					.ignoreCase());
		}
		if (executionResultDetail.getDescription() != null) {
			criteria.add(Restrictions.like("description",
					"%" + executionResultDetail.getDescription() + "%")
					.ignoreCase());
		}
		if (executionResultDetail.getSignature() != null) {
			criteria.add(Restrictions.like("signature",
					"%" + executionResultDetail.getSignature() + "%")
					.ignoreCase());
		}
		return criteria;

	}

	public int getExecutionResultDetailFilterCount(
			ExecutionResultDetail executionResultDetail) {
		DetachedCriteria criteria = createExecutionResultDetailCriteria(executionResultDetail);
		return super.getObjectListCount(ExecutionResultDetail.class, criteria);
	}

	public List getExecutionResultDetailList(
			ExecutionResultDetail executionResultDetail, int start, int limit) {
		DetachedCriteria criteria = createExecutionResultDetailCriteria(executionResultDetail);
		criteria.addOrder(Order.desc("executionResultDetailId"));
		return super.getObjectListByRangeByValue(ExecutionResultDetail.class, criteria,
				start, limit);		
	}

	public List getAllExecutionResultDetailList(
			ExecutionResultDetail executionResultDetail) {
		DetachedCriteria criteria = createExecutionResultDetailCriteria(executionResultDetail);
		criteria.addOrder(Order.desc("executionResultDetailId"));
		return super.getAllObjectList(ExecutionResultDetail.class, criteria);
	}

	public void saveExecutionResultDetail(
			ExecutionResultDetail executionResultDetail) {
		super.saveOrUpdatewithReturn(executionResultDetail);
	}

	public ExecutionResultDetail saveExecutionResultDetailWithReturn(
			ExecutionResultDetail executionResultDetail) {
		return (ExecutionResultDetail) super
				.saveOrUpdatewithReturn(executionResultDetail);
	}

	public ExecutionResultDetail saveExecutionResultDetailwithReturn(
			ExecutionResultDetail executionResultDetail) {
		return (ExecutionResultDetail) super
				.saveOrUpdatewithReturn(executionResultDetail);
	}

	public void removeExecutionResultDetail(
			ExecutionResultDetail executionResultDetail) {
		super.delete(executionResultDetail);
	}

	public ExecutionResultDetail getExecutionResultDetailById(Integer Id) {
		return (ExecutionResultDetail) super.find(ExecutionResultDetail.class,
				Id);
	}

}
