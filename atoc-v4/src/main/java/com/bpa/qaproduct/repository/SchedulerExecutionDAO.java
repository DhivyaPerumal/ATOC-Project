package com.bpa.qaproduct.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.SchedulerExecution;

@Repository("SchedulerExecutionDAO")
public class SchedulerExecutionDAO extends QatAbstractDao {
	public SchedulerExecutionDAO() {
		super();
	}

	private DetachedCriteria createTestSuiteExecutionCriteria(
			SchedulerExecution schedulerExecution) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SchedulerExecution.class);

		return criteria;
	}

	public void saveSchedulerExecution(SchedulerExecution schedulerExecution) {
		super.saveOrUpdate(schedulerExecution);
	}

}
