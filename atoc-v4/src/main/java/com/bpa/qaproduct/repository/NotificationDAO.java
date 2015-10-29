package com.bpa.qaproduct.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuiteExecution;


@Repository("NotificationDAO")
public class NotificationDAO extends QatAbstractDao{

	public NotificationDAO() {
		super();
	}
	
	private DetachedCriteria createNotificationCriteria(Notification notification,String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
		
		
			if(notification.getUser().getUserId() != null)
			{
				criteria.createAlias("user","user").add(Restrictions.eq("user.userId", notification.getUser().getUserId()));
			}
			if (notification.getNotificationStatus() != null) {
				criteria.add(Restrictions.eq("notificationStatus",notification.getNotificationStatus() ).ignoreCase());
			}
			if (notification.getNotificationTitle() != null) {
				criteria.add(Restrictions.like("notificationTitle","%" + notification.getNotificationTitle() + "%").ignoreCase());
			}
			if (notification.getNotificationMessage() != null) {
				criteria.add(Restrictions.like("notificationMessage","%" + notification.getNotificationMessage() + "%").ignoreCase());
			}
			return criteria;

	}
	
	public List getNotificationListOfUser(Notification notification) {
		DetachedCriteria criteria = createNotificationCriteria(notification,"");
		criteria.addOrder(Order.desc("notificaitonId"));
		return super.getAllObjectList(Notification.class, criteria);
	}
	
	public Notification saveNotificationWithReturn(Notification notification) {
		return (Notification) super.saveOrUpdatewithReturn(notification);
	}
	
	public Notification getNotificationById(Integer Id) {
		return (Notification) super.find(Notification.class, Id);
	}


}
