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

import com.bpa.qaproduct.entity.ApplicationKey;
import com.bpa.qaproduct.service.ApplicationKeyService;

@Controller
public class ApplicationKeyController {

	@Autowired
	private ApplicationKeyService applicationKeyService;

	protected final Log logger = LogFactory
			.getLog(ApplicationKeyController.class);

	// List Service

	@RequestMapping(value = "/applicationKey/viewApplicationKeyList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewApplicationKeyList(HttpServletRequest request) {
		int start = 0;
		int limit = 10;

		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));
		try {

			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			// Constructing User Search Object
			ApplicationKey applicationKey = new ApplicationKey();

			String searchApplicationKeyName = request
					.getParameter("searchApplicationKeyName");
			if (searchApplicationKeyName != null
					&& searchApplicationKeyName.isEmpty() == false) {
				applicationKey.setApplicationKey(searchApplicationKeyName);
			}

			int totalResults = applicationKeyService
					.getApplicationKeyFilterCount(applicationKey);
			List<ApplicationKey> list = applicationKeyService
					.getApplicationKeyList(applicationKey,start,limit);

			// logger.info("Returned list size" + list.size());

			return getModelMapApplicationKeyList(list, totalResults);
		} catch (Exception e) {
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/applicationKey/saveApplicationKey", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveApplicationKey(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			ApplicationKey applicationKey = new ApplicationKey();
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			if ((StringUtils.isNotBlank(request
					.getParameter("applicationKeyId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("applicationKeyId")))) {
				id_value = request.getParameter("applicationKeyId");
				applicationKey = applicationKeyService
						.getApplicationKeyById(Integer.parseInt(id_value));
				// applicationKey.setUpdatedBy(1);; // need to change after
				// getting user from session
				applicationKey.setCreatedOn(todayDate);
				applicationKey.setUpdatedOn(todayDate);
			} else {
				// applicationKey.setCreatedBy(1); // need to change after
				// getting user from session
				applicationKey.setCreatedOn(todayDate);
				applicationKey.setUpdatedOn(todayDate);
				// logger.info("Insert Method Strarted., 3");

			}

			String applicationKey1 = request.getParameter("applicationKey");
			applicationKey.setApplicationKey(applicationKey1);

			String isActive = request.getParameter("isActive");
			applicationKey.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			applicationKey.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			applicationKey.setUpdatedBy(Integer.parseInt(updatedBy));

			String assignedStatus = request.getParameter("assignedStatus");
			applicationKey.setAssignedStatus(assignedStatus);

			// logger.info("Insert Method Strarted., 4");
			applicationKeyService.saveApplicationKey(applicationKey);
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
	@RequestMapping(value = "/applicationKey/deleteApplicationKey", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteApplicationKey(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		try {

			int applicationKeyId = Integer.parseInt(request
					.getParameter("applicationKeyId"));

			ApplicationKey applicationKey = applicationKeyService
					.getApplicationKeyById(applicationKeyId);

			applicationKeyService.removeApplicationKey(applicationKey);

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
	private Map<String, Object> getModelMapApplicationKeyList(
			List<ApplicationKey> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(ApplicationKey.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof ApplicationKey)) {
							return new JSONObject(true);
						}
						ApplicationKey applicationKey = (ApplicationKey) bean;

						return new JSONObject()
								.element("applicationKeyId",
										applicationKey.getApplicationKeyId())
								.element("applicationKey",
										applicationKey.getApplicationKey())
								.element("isActive",
										applicationKey.getIsActive())
								.element("createdBy",
										applicationKey.getCreatedBy())
								.element("createdOn",
										applicationKey.getCreatedOn())
								.element("updatedBy",
										applicationKey.getUpdatedBy())
								.element("updatedOn",
										applicationKey.getUpdatedOn())
								.element("assignedStatus",
										applicationKey.getAssignedStatus());
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
