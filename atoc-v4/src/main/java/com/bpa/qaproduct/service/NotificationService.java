package com.bpa.qaproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.NotificationDAO;

@Service("NotificationService")
@Transactional
public class NotificationService {
	
	
	@Autowired
	protected NotificationDAO notificationDAO; 
		
	public List getNotificationListOfUser(Notification notification) {
		return notificationDAO.getNotificationListOfUser(notification);
	}
	
	
	public Notification saveNotificationWithReturn(Notification notification) {
		return notificationDAO.saveNotificationWithReturn(notification);
	}
	
	public Notification getNotificationById(Integer Id) {
		return notificationDAO.getNotificationById(Id);
	}

}
