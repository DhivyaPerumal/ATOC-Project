package com.bpa.qaproduct.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.RoleService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	protected final Log logger = LogFactory.getLog(RoleController.class);

	// List Service

	@RequestMapping(value = "/role/viewRoleList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewRoleList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			Role role = new Role();

			String searchRoleName = request.getParameter("searchRoleName");
			if (searchRoleName != null && searchRoleName.isEmpty() == false) {
				role.setRoleName(searchRoleName);
			}

			int totalResults = roleService.getRoleFilterCount(role);
			List<Role> list = roleService.getAllRoleList(role);

			logger.info("Returned list size" + list.size());

			return getModelMapRoleList(list, totalResults);
		} catch (Exception e) {
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	

	// Save Service
	@RequestMapping(value = "/role/saveRole", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveRole(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Role role = new Role();
			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("roleId")))
					|| (StringUtils.isNotEmpty(request.getParameter("roleId")))) {
				id_value = request.getParameter("roleId");
				role = roleService.getRoleById(Integer.parseInt(id_value));
				// role.setUpdatedBy(1);; // need to change after getting user
				// from session
				role.setUpdatedOn(todayDate);
				role.setCreatedOn(todayDate);

			} else {
				// role.setCreatedBy(1); // need to change after getting user
				// from session
				role.setUpdatedOn(todayDate);
				role.setCreatedOn(todayDate);

			}

			String roleName = request.getParameter("roleName");
			role.setRoleName(roleName);

			String roleDescription = request.getParameter("roleDescription");
			role.setRoleDescription(roleDescription);

			String isActive = request.getParameter("isActive");
			role.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			role.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			role.setUpdatedBy(Integer.parseInt(updatedBy));

			String organizationId_str = request.getParameter("organization");
			Organization organization = new Organization();
			organization = organizationService.getOrganizationById((Integer
					.parseInt(organizationId_str)));
			role.setOrganization(organization);

			roleService.saveRole(role);
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

	// Delete Service
	@RequestMapping(value = "/role/deleteRole", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteRole(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int roleId = Integer.parseInt(request.getParameter("roleId"));
		try {
			Role role = roleService.getRoleById(roleId);
			roleService.removeRole(role);
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}
	}

	// JSON Construction details by role
	private Map<String, Object> getModelMapDetailsByRoleList(List<User> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(User.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof User)) {
							return new JSONObject(true);
						}
						User user = (User) bean;

						return new JSONObject().element("userId",
								user.getUserId()).element("firstName",
								user.getFirstName());

					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction
	private Map<String, Object> getModelMapRoleList(List<Role> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Role.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Role)) {
							return new JSONObject(true);
						}
						Role role = (Role) bean;

						return new JSONObject()
								.element("roleId", role.getRoleId())
								.element("roleName", role.getRoleName())
								.element("roleDescription",
										role.getRoleDescription())
								.element("isActive", role.getIsActive())
								.element("createdBy", role.getCreatedBy())
								.element("createdOn", role.getCreatedOn())
								.element("updatedBy", role.getUpdatedBy())
								.element("updatedOn", role.getUpdatedOn());

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
