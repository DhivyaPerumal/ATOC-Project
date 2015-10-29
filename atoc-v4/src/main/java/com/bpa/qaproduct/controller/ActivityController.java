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
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.Activity;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.service.ActivityService;
import com.bpa.qaproduct.service.OrganizationService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private OrganizationService organizationService;

	protected final Log logger = LogFactory.getLog(ActivityController.class);

	// List Service

	@RequestMapping(value = "/activity/viewActivityList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewActivityList(HttpServletRequest request) {

		int start = 0;
		int limit = 10;

		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));
		try {
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			// Constructing User Search Object
			Activity activity = new Activity();

			String searchActivityName = request
					.getParameter("searchActivityName");
			if (searchActivityName != null
					&& searchActivityName.isEmpty() == false) {
				activity.setActivityName(searchActivityName);

			}

			int totalResults = activityService.getActivityFilterCount(activity);
			List<Activity> list = activityService.getActivityList(activity,start,limit);

			// logger.info("Returned list size" + list.size());

			return getModelMapActivityList(list, totalResults);
		} catch (Exception e) {
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/acitivity/saveActivity", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveActivity(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Activity activity = new Activity();
			String id_value = "";
			// logger.info("Insert Method Strarted 1");
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			if ((StringUtils.isNotBlank(request.getParameter("activityId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("activityId")))) {
				id_value = request.getParameter("activityId");
				activity = activityService.getActivityById(Integer
						.parseInt(id_value));
				// activity.setUpdatedBy(1);; // need to change after getting
				// user from session
				activity.setCreatedOn(todayDate);
				activity.setUpdatedOn(todayDate);

			} else {
				// activity.setCreatedBy(1); // need to change after getting
				// user from session
				activity.setCreatedOn(todayDate);
				activity.setUpdatedOn(todayDate);

			}

			if ((StringUtils.isNotBlank(request.getParameter("activityName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("activityName")))) {
				String activityName = request.getParameter("activityName");
				activity.setActivityName(activityName);
			}

			String activityDescription = request
					.getParameter("activityDescription");
			activity.setActivityDescription(activityDescription);

			String isActive = request.getParameter("isActive");
			activity.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			activity.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			activity.setUpdatedBy(Integer.parseInt(updatedBy));

			String organizationId_str = request.getParameter("organization");
			Organization organization = new Organization();
			organization = organizationService.getOrganizationById((Integer
					.parseInt(organizationId_str)));
			activity.setOrganization(organization);

			activityService.saveActivity(activity);
			// logger.info("Insert Method Executed.,");
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

	// Delete Service
	@RequestMapping(value = "/activity/deleteActivity", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteActivity(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int activityId = Integer.parseInt(request.getParameter("activityId"));
		try {
			Activity activity = activityService.getActivityById(activityId);
			activityService.removeActivity(activity);
			// logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}
	}

	// JSon Construction
	private Map<String, Object> getModelMapActivityList(List<Activity> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Organization.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Organization)) {
							return new JSONObject(true);
						}
						Activity activity = (Activity) bean;

						return new JSONObject()
								.element("activityId", activity.getActivityId())
								.element("activityName",
										activity.getActivityName())
								.element("activityDescription",
										activity.getActivityDescription())
								.element("isActive", activity.getIsActive())
								.element("createdBy", activity.getCreatedBy())
								.element("createdOn", activity.getCreatedOn())
								.element("updatedBy", activity.getUpdatedBy())
								.element("updatedOn", activity.getUpdatedOn())
								.element("organization",
										activity.getOrganization());
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Common json methods
	 */

	private ModelAndView getModelMap() {

		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		modelMap.put("success", true);
		return new ModelAndView("jsonView", modelMap);
	}

	private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

}
