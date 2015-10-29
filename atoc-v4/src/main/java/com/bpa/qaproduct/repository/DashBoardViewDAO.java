package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.bpa.qaproduct.repository.QatAbstractDao;

import com.bpa.qaproduct.entity.DashBoardView;

@Repository("DashBoardViewDAO")
public class DashBoardViewDAO extends QatAbstractDao {

	public DashBoardViewDAO() {
		super();
	}

	private DetachedCriteria createDashBoardViewCriteria(
			DashBoardView dashBoardView) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(DashBoardView.class);
		if (dashBoardView.getProjectId() != null) {
			criteria.add(Restrictions.like("name",
					"%" + dashBoardView.getProjectId() + "%").ignoreCase());
		}
		if (dashBoardView.getProjectName() != null) {
			criteria.add(Restrictions.like("notes",
					"%" + dashBoardView.getProjectName() + "%").ignoreCase());
		}
		if (dashBoardView.getTestCaseCount() != null) {
			criteria.add(Restrictions.like("address",
					"%" + dashBoardView.getTestCaseCount() + "%").ignoreCase());
		}
		if (dashBoardView.getTestSuiteCount() != null) {
			criteria.add(Restrictions.like("city",
					"%" + dashBoardView.getTestSuiteCount() + "%").ignoreCase());
		}

		return criteria;

	}

	public int getDashBoardViewFilterCount(DashBoardView dashBoardView) {
		DetachedCriteria criteria = createDashBoardViewCriteria(dashBoardView);
		return super.getObjectListCount(DashBoardView.class, criteria);
	}

	public List getAllDashBoardViewList(DashBoardView dashBoardView) {
		DetachedCriteria criteria = createDashBoardViewCriteria(dashBoardView);
		criteria.addOrder(Order.desc("projectId"));
		return super.getAllObjectList(DashBoardView.class, criteria);
	}

	public DashBoardView getDashBoardViewById(Integer Id) {
		return (DashBoardView) super.find(DashBoardView.class, Id);
	}

}
