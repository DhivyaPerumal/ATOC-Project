package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.Activity;
import com.bpa.qaproduct.entity.Project;

@Repository("ActivityDAO")
public class ActivityDAO extends QatAbstractDao {

	public ActivityDAO() {
		super();
	}

	private DetachedCriteria createActivityCriteria(Activity activity,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Activity.class);
		if (activity.getActivityName() != null) {
			if (matchType.equalsIgnoreCase("exact"))
			{
				criteria.add(Restrictions.eq("activityName", ""+activity.getActivityName() +"").ignoreCase());
			}
			else
			{
				criteria.add(Restrictions.like("activityName",
						"%" + activity.getActivityName() + "%").ignoreCase());
			}
			
		}
		if (activity.getActivityDescription() != null) {
			criteria.add(Restrictions.like("activityDescription",
					"%" + activity.getActivityDescription() + "%").ignoreCase());
		}
		if (activity.getIsActive() != null) {
			/*criteria.add(Restrictions.like("isActive",
					"%" + activity.getIsActive() + "%").ignoreCase());*/
			criteria.add(Restrictions.eq("isActive", "" + activity.getIsActive()
					+ ""));
		}
		if (activity.getOrganization() != null) {
			criteria.add(Restrictions.eq("organization", activity.getOrganization()));
		}

		return criteria;

	}

	public int getActivityFilterCount(Activity activity) {
		DetachedCriteria criteria = createActivityCriteria(activity,"");
		return super.getObjectListCount(Activity.class, criteria);
	}

	public List getActivityList(Activity activity, int start, int limit) {
		DetachedCriteria criteria = createActivityCriteria(activity,"");
		criteria.addOrder(Order.desc("activityId"));
		return super.getObjectListByRangeByValue(Activity.class, criteria,
				start, limit);		
	}

	public List getAllActivityList(Activity activity) {
		DetachedCriteria criteria = createActivityCriteria(activity,"");
		criteria.addOrder(Order.desc("activityId"));
		return super.getAllObjectList(Activity.class, criteria);
	}

	public void saveActivity(Activity activity) {
		super.saveOrUpdate(activity);
	}

	public void removeActivity(Activity activity) {
		super.delete(activity);
	}

	public Activity getActivityById(Integer Id) {
		return (Activity) super.find(Activity.class, Id);
	}
	public Activity getActivityBySearchParameter(Activity searchActivity) {
		DetachedCriteria criteria = createActivityCriteria(searchActivity,"exact");
		return (Activity) super.getUniqueObject(Activity.class, criteria);	
	}

}
