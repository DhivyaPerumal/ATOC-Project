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

import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.OrganizationContactDetail;
import com.bpa.qaproduct.service.OrganizationContactDetailService;
import com.bpa.qaproduct.service.OrganizationService;

@Controller
public class OrganizationContactController {

	@Autowired
	private OrganizationContactDetailService organizationContactDetailService;

	@Autowired
	private OrganizationService organizationService;

	protected final Log logger = LogFactory
			.getLog(OrganizationContactController.class);

	// List Service

	@RequestMapping(value = "/organizationContactDetail/viewOrganizationContactList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewOrganizationContactList(HttpServletRequest request) {

		try {
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);

			// Constructing User Search Object
			OrganizationContactDetail orgContactDetail = new OrganizationContactDetail();

			String searchContactName = request
					.getParameter("searchContactName");
			if (searchContactName != null
					&& searchContactName.isEmpty() == false) {
				orgContactDetail.setContactName(searchContactName);
			}

			int totalResults = organizationContactDetailService
					.getOrganizationContactDetailFilterCount(orgContactDetail);
			List<OrganizationContactDetail> list = organizationContactDetailService
					.getAllOrganizationContactDetailList(orgContactDetail);

			// logger.info("Returned list size" + list.size());

			return getModelMapOrganizationContactList(list, totalResults);
		} catch (Exception e) {
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/organizationContactDetail/saveOrganizationContact", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveActivity(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			OrganizationContactDetail orgContactDetail = new OrganizationContactDetail();
			String id_value = "";

			if ((StringUtils.isNotBlank(request
					.getParameter("OrgContactDetailId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("OrgContactDetailId")))) {
				id_value = request.getParameter("OrgContactDetailId");
				orgContactDetail = organizationContactDetailService
						.getOrganizationContactDetailById(Integer
								.parseInt(id_value));
				orgContactDetail.setUpdatedOn(todayDate);
				orgContactDetail.setCreatedOn(todayDate);
			} else {
				orgContactDetail.setCreatedOn(todayDate);
				orgContactDetail.setUpdatedOn(todayDate);

			}

			String contactName = request.getParameter("contactName");
			orgContactDetail.setContactName(contactName);

			String contactEmail = request.getParameter("contactEmail");
			orgContactDetail.setContactEmail(contactEmail);

			String contactPhone = request.getParameter("contactPhone");
			orgContactDetail.setContactPhone(contactPhone);

			String contactDesignation = request
					.getParameter("contactDesignation");
			orgContactDetail.setContactDesignation(contactDesignation);

			String isActive = request.getParameter("isActive");
			orgContactDetail.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			orgContactDetail.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			orgContactDetail.setUpdatedBy(Integer.parseInt(updatedBy));

			String organizationId_str = request.getParameter("organization");
			Organization organization = new Organization();
			organization = organizationService.getOrganizationById((Integer
					.parseInt(organizationId_str)));
			orgContactDetail.setOrganization(organization);

			organizationContactDetailService
					.saveOrganizationContactDetail(orgContactDetail);

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
	@RequestMapping(value = "/organizationContactDetail/deleteOrganizationContact", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteActivity(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int OrgContactDetailId = Integer.parseInt(request
				.getParameter("OrgContactDetailId"));
		try {
			OrganizationContactDetail orgContactDetail = organizationContactDetailService
					.getOrganizationContactDetailById(OrgContactDetailId);
			organizationContactDetailService
					.removeOrganizationContactDetail(orgContactDetail);
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
	private Map<String, Object> getModelMapOrganizationContactList(
			List<OrganizationContactDetail> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(OrganizationContactDetail.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof OrganizationContactDetail)) {
							return new JSONObject(true);
						}
						OrganizationContactDetail organizationContactDetail = (OrganizationContactDetail) bean;

						return new JSONObject()
								.element(
										"OrgContactDetailId",
										organizationContactDetail
												.getOrgContactDetailId())
								.element(
										"contactName",
										organizationContactDetail
												.getContactName())
								.element(
										"contactEmail",
										organizationContactDetail
												.getContactEmail())
								.element(
										"contactPhone",
										organizationContactDetail
												.getContactPhone())
								.element(
										"contactDesignation",
										organizationContactDetail
												.getContactDesignation())
								.element("isActive",
										organizationContactDetail.getIsActive())
								.element(
										"createdBy",
										organizationContactDetail
												.getCreatedBy())
								.element(
										"createdOn",
										organizationContactDetail
												.getCreatedOn())
								.element(
										"updatedBy",
										organizationContactDetail
												.getUpdatedBy())
								.element(
										"updatedOn",
										organizationContactDetail
												.getUpdatedOn())

						;
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
