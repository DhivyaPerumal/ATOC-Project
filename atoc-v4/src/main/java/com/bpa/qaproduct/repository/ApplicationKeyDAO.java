package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.ApplicationKey;
import com.bpa.qaproduct.entity.Project;

@Repository("ApplicationKeyDAO")
public class ApplicationKeyDAO extends QatAbstractDao {

	public ApplicationKeyDAO() {
		super();
	}

	private DetachedCriteria createApplicationKeyCriteria(
			ApplicationKey applicationKey) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ApplicationKey.class);
		if (applicationKey.getApplicationKey() != null) {
			criteria.add(Restrictions.like("applicationKey",
					"%" + applicationKey.getApplicationKey() + "%")
					.ignoreCase());
		}
		if (applicationKey.getAssignedStatus() != null) {
			criteria.add(Restrictions.eq("assignedStatus",
					"" + applicationKey.getAssignedStatus() + ""));
		}
		if (applicationKey.getIsActive() != null) {
			criteria.add(Restrictions.eq("isActive",
					"" + applicationKey.getIsActive() + ""));
		}

		return criteria;

	}

	public int getApplicationKeyFilterCount(ApplicationKey applicationKey) {
		DetachedCriteria criteria = createApplicationKeyCriteria(applicationKey);
		return super.getObjectListCount(ApplicationKey.class, criteria);
	}

	public List getApplicationKeyList(ApplicationKey applicationKey, int start, int limit) {
		DetachedCriteria criteria = createApplicationKeyCriteria(applicationKey);
		criteria.addOrder(Order.desc("applicationKeyId"));
		return super.getObjectListByRangeByValue(ApplicationKey.class, criteria,
				start, limit);		
	}

	public List getAllApplicationKeyList(ApplicationKey applicationKey) {
		DetachedCriteria criteria = createApplicationKeyCriteria(applicationKey);
		criteria.addOrder(Order.desc("applicationKeyId"));
		return super.getAllObjectList(ApplicationKey.class, criteria);
	}

	public void saveApplicationKey(ApplicationKey applicationKey) {
		super.saveOrUpdate(applicationKey);
	}

	public void removeApplicationKey(ApplicationKey applicationKey) {
		super.delete(applicationKey);
	}

	public ApplicationKey getApplicationKeyById(Integer Id) {
		return (ApplicationKey) super.find(ApplicationKey.class, Id);
	}

}
