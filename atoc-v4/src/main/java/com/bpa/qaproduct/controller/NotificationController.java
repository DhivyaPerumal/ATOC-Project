package com.bpa.qaproduct.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.NotificationService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class NotificationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	
	
	protected final Log logger = LogFactory.getLog(NotificationController.class);
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notificationView/getNotificationsOfUser", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getNotificationsOfUser(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		User user = null; Notification notification = null; List<Notification> notificationList = null;
		try {
			String userId = request.getParameter("userId");
			if (request.getParameter("userId") != null || request.getParameter("userId") == "") {
				user = new User(); 
				notification = new Notification();
				user = userService.getUserById(Integer.parseInt(userId));
				logger.info("Notifications for User : "+user.getUserId()+" : "+user.getFirstName());
				notification.setUserId(user);
				notification.setNotificationStatus("NOTVIEWED");
				notificationList = notificationService.getNotificationListOfUser(notification);
			}
			
			return getModelMapNotificationList(notificationList, notificationList.size());

		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem while getting data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}
	
		// Save Service
		@RequestMapping(value = "/notificationView/saveNotificationView", method = RequestMethod.POST)
		public @ResponseBody
		Map<String, Object> saveNotificationView(HttpServletRequest request) {
			logger.info("Insert Method Strarted.,");
			Map<String, Object> modelMap = new HashMap<String, Object>(2);
			try {
				Calendar currentDate = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date todayDate = (Date) formatter.parse(formatter
						.format(currentDate.getTime()));
				
				Notification notification = new Notification();
				
				if ((StringUtils.isNotBlank(request.getParameter("notificationId")))
						|| (StringUtils.isNotEmpty(request
								.getParameter("notificationId")))) {
					String id_value = request.getParameter("notificationId");
					notification = notificationService.getNotificationById((Integer.parseInt(id_value)));
					notification.setCreatedOn(todayDate);
					notification.setUpdatedOn(todayDate);
				}
					String userId = request.getParameter("userId");
					logger.info("user Id is :" + userId);
					User user = new User();
					if (userId != null) {
						logger.info("userId is :" + userId);
						user.setUserId(Integer.parseInt(userId));
						user = userService.getUserById(Integer.parseInt(userId));
						notification.setUserId(user);
					}
					
				String createdBy = request.getParameter("createdBy");
				notification.setCreatedBy(Integer.parseInt(createdBy));

				String updatedBy = request.getParameter("updatedBy");
				notification.setUpdatedBy(Integer.parseInt(updatedBy));
				
				String notificationStatus = request.getParameter("notificationStatus");
				if (notificationStatus != null) {
					notification.setNotificationStatus(notificationStatus);
					
				}
				
				String notificationMessage = request.getParameter("notificationMessage");
				if(notificationMessage != null){
				notification.setNotificationMessage(notificationMessage);
				}
				
				String notificationTitle = request.getParameter("notificationTitle");
				if(notificationTitle != null){
				notification.setNotificationTitle(notificationTitle);
				}
				notification = notificationService.saveNotificationWithReturn(notification);
				notification.getNotificationMessage();
				logger.info("Notification Message"+notification.getNotificationMessage());
				logger.info("Insert Method Executed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Saved Successfully");
				return modelMap;
			} catch (Exception ex) {
				String msg = "Sorry problem in saving data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return modelMap;
			}

		}
	
	
	// JSon for Notification list details
			private Map<String, Object> getModelMapNotificationList(
					List<Notification> list, int totalResults) {

				Map<String, Object> modelMap = new HashMap<String, Object>(3);
				modelMap.put("total", totalResults);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonBeanProcessor(Notification.class,
						new JsonBeanProcessor() {
							public JSONObject processBean(Object bean,
									JsonConfig jsonConfig) {
								if (!(bean instanceof Notification)) {
									return new JSONObject(true);
								}
								Notification notification = (Notification) bean;
									
								return new JSONObject()
										.element("notificationId",notification.getNotificaitonId())
										.element("notificationMessage",notification.getNotificationMessage())
										.element("notificationTitle",notification.getNotificationTitle())
										.element("notificationStatus",notification.getNotificationStatus())
										.element("createdBy",notification.getCreatedBy())
										.element("createdOn",notification.getCreatedOn())
										.element("updatedOn",notification.getUpdatedOn())
										.element("updatedBy",notification.getUpdatedBy())
										
										;
							}
						});

				JSON json = JSONSerializer.toJSON(list, jsonConfig);

				/*---*/
				modelMap.put("data", json);
				modelMap.put("success", true);

				return modelMap;
			}
}
