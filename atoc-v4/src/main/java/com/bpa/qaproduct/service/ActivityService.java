package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.Activity;
import com.bpa.qaproduct.repository.ActivityDAO;

@Service("ActivityService")
@Transactional
public class ActivityService {
	@Autowired
	public ActivityDAO activityDAO;

	public int getActivityFilterCount(Activity activity) {
		return activityDAO.getActivityFilterCount(activity);
	}

	public List getActivityList(Activity activity, int start, int limit) {
		return activityDAO.getActivityList(activity,start,limit);
	}

	public List getAllActivityList(Activity activity) {
		return activityDAO.getAllActivityList(activity);
	}

	public void saveActivity(Activity activity) {
		activityDAO.saveActivity(activity);
	}

	public void removeActivity(Activity activity) {
		activityDAO.removeActivity(activity);
	}

	public Activity getActivityById(Integer Id) {
		return activityDAO.getActivityById(Id);
	}

}
