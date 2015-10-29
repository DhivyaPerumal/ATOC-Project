package com.bpa.qaproduct.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.ProjectService;
import com.bpa.qaproduct.service.TestSuiteService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class TestSuiteController {

	@Autowired
	private TestSuiteService testSuiteService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	protected final Log logger = LogFactory.getLog(TestSuiteController.class);

	// List Service

	@RequestMapping(value = "/testSuite/viewTestSuiteList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewTestSuiteList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			TestSuite testSuite = new TestSuite();
			Project project = new Project();

			String suiteName = request.getParameter("suiteName");
			if (suiteName != null && suiteName.isEmpty() == false) {
				testSuite.setSuiteName(suiteName);
			}
			User user = new User();
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");

				logger.info("Getting User Object based on Id :" + id_value);
				user = userService.getUserById((Integer.parseInt(id_value)));
				// logger.info("********Returned list size*****" +
				// user.getOrganization());
				testSuite.setOrganization(user.getOrganization());
			}
			int totalResults = testSuiteService
					.getTestSuiteFilterCount(testSuite);
			List<TestSuite> list = testSuiteService
					.getAllTestSuiteList(testSuite);

			// logger.info("Returned list size" + list.size());

			return getModelMapTestSuiteList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Pagination Service

	@RequestMapping(value = "/testSuite/viewTestSuiteListPagination", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewTestSuiteListPagination(HttpServletRequest request) {

		int start = 0;
		int limit = 10;

		try {
			TestSuite testSuite = new TestSuite();
			Project project = new Project();

			start = Integer.parseInt(request.getParameter("iDisplayStart"));
			limit = Integer.parseInt(request.getParameter("iDisplayLength"));

			String suiteName = request.getParameter("suiteName");

			if (suiteName != null && suiteName.isEmpty() == false) {
				testSuite.setSuiteName(suiteName);
			}

			User user = new User();

			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById((Integer.parseInt(id_value)));
				testSuite.setOrganization(user.getOrganization());
			}

			if (StringUtils.isNotBlank(request.getParameter("projectName"))) {
				project.setProjectName(request.getParameter("projectName"));
				testSuite.setProject(project);
			}

			if (StringUtils.isNotBlank(request.getParameter("suiteName"))) {
				testSuite.setSuiteName(request.getParameter("suiteName"));
			}

			if (StringUtils.isNotBlank(request.getParameter("createdOn"))) {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date todayDate = (Date) formatter.parse(request
						.getParameter("createdOn"));
				testSuite.setCreatedOn(todayDate);

			}

			int totalResults = testSuiteService
					.getTestSuiteFilterCount(testSuite);
			List<TestSuite> list = testSuiteService.getTestSuiteListPagination(
					testSuite, start, limit);
			return getModelMapTestSuiteListpagination(list, totalResults);

		}

		catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}

	}

	// Save Service
	@RequestMapping(value = "/testSuite/saveTestSuite", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveTestSuite(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		TestSuite testSuite = new TestSuite();
		Project project = null;
		String projectId_str = request.getParameter("project");
		if (projectId_str != null || projectId_str == "") {
			project = new Project();
			project = projectService.getProjectnById(Integer
					.parseInt(projectId_str));
			testSuite.setProject(project);
		}
		Properties prop = new Properties();
		String propFileName = "/properties/environment.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String baseUrl = prop.getProperty("ftp_jar_location");
		File testFile = new File(baseUrl + "/" + project.getProjectName()
				+ ".jar");

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		if (!testFile.exists()) {
			modelMap.put("success", false);
			modelMap.put("message", "Project Jar file is not uploaded");
			return modelMap;
		}
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("testSuiteId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiteId")))) {
				id_value = request.getParameter("testSuiteId");
				testSuite = testSuiteService.getTestSuiteById(Integer
						.parseInt(id_value));
				testSuite.setCreatedOn(todayDate);
				testSuite.setUpdatedOn(todayDate);
			} else {
				testSuite.setCreatedOn(todayDate);
				testSuite.setUpdatedOn(todayDate);
			}

			String suiteName = request.getParameter("suiteName");
			if (suiteName != null || suiteName == "") {
				testSuite.setSuiteName(suiteName);
			}

			String notes = request.getParameter("notes");
			if (notes != null || notes == "") {
				testSuite.setNotes(notes);
			}

			String isActive = request.getParameter("isActive");
			if (isActive != null || isActive == "") {
				testSuite.setIsActive(testSuite.setIsActiveString(isActive));
			}

			String createdBy = request.getParameter("createdBy");
			if (createdBy != null) {
				testSuite.setCreatedBy(Integer.parseInt(createdBy));
			} else {
				testSuite.setCreatedBy(10);
			}

			String updatedBy = request.getParameter("updatedBy");
			if (updatedBy != null || updatedBy == "") {
				testSuite.setUpdatedBy(Integer.parseInt(updatedBy));
			} else {
				testSuite.setUpdatedBy(20);
			}

			String testSuiteUrl = request.getParameter("testSuiteUrl");
			if (testSuiteUrl != null || testSuiteUrl == "") {
				testSuite.setTestSuiteUrl(testSuiteUrl);
			}

			String testSuiteXmlPathInJar = request
					.getParameter("testSuiteXmlPathInJar");
			if (testSuiteXmlPathInJar != null || testSuiteXmlPathInJar == "") {
				testSuite.setTestSuiteXmlPathInJar((testSuiteXmlPathInJar));
			}

			// logger.info("Inside the option menu");

			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				User user = new User();
				id_value = request.getParameter("userId");
				user = userService.getUserById(Integer.parseInt(id_value));
				testSuite.setOrganization(user.getOrganization());
				// logger.info("*********"+testSuite.getOrganization());
			}

			testSuiteService.saveTestSuite(testSuite);

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
	@RequestMapping(value = "/testSuite/deleteTestSuite", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteTestSuite(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int testSuiteId = Integer.parseInt(request.getParameter("testSuiteId"));
		try {
			TestSuite testSuite = testSuiteService
					.getTestSuiteById(testSuiteId);
			testSuiteService.removeTestSuite(testSuite);
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

	@RequestMapping(value = "/testSuite/getTestSuiteInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> testSuiteId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>(2);

		String testSuiteId_str = request.getParameter("testSuiteId");
		TestSuite testSuite = new TestSuite();
		try {
			if (testSuiteId_str != null) {
				testSuite = testSuiteService.getTestSuiteById(Integer
						.parseInt(testSuiteId_str));
			} else {
				return getModelMapError("Failed to Load Data");
			}
			return getModelMapTestSuiteInfo(testSuite);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in loading data";
			map.put("success", false);
			map.put("message", msg);
			return map;
		}
	}

	// JSon Construction
	private Map<String, Object> getModelMapTestSuiteInfo(TestSuite testSuite) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuite.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuite)) {
							return new JSONObject(true);
						}

						TestSuite testSuite = (TestSuite) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String createdOnString = "";
						if (testSuite.getCreatedOn() != null) {
							createdOnString = importDateFormat.format(testSuite
									.getCreatedOn());
						}
						String updatedOnString = "";
						if (testSuite.getUpdatedOn() != null) {
							updatedOnString = importDateFormat.format(testSuite
									.getUpdatedOn());
						}

						return new JSONObject()

								.element("testSuiteId",
										testSuite.getTestSuiteId())
								.element("suiteName", testSuite.getSuiteName())
								.element("notes", testSuite.getNotes())
								.element(
										"isActive",
										testSuite.getIsActiveString(testSuite
												.getIsActive()))
								.element(
										"organizationName",
										testSuite.getOrganization()
												.getOrganizationName())
								.element("projectName",
										testSuite.getProject().getProjectName())
								.element("projectId",
										testSuite.getProject().getProjectId())
								.element("createdBy", testSuite.getCreatedBy())
								.element("createdOn", createdOnString)
								.element("updatedBy", testSuite.getUpdatedBy())
								.element("updatedOn", updatedOnString)
								.element("testSuiteUrl",
										testSuite.getTestSuiteUrl())

								.element("testSuiteXmlPathInJar",
										testSuite.getTestSuiteXmlPathInJar());
					}
				});

		JSON json = JSONSerializer.toJSON(testSuite, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction
	private Map<String, Object> getModelMapTestSuiteList(List<TestSuite> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuite.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuite)) {
							return new JSONObject(true);
						}

						TestSuite testSuite = (TestSuite) bean;

						Date datee = testSuite.getUpdatedOn();
						SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
								"dd-MM-yy:HH:mm:SS");
						String updatedOn = DATE_FORMAT.format(datee);

						return new JSONObject()

								.element("testSuiteId",
										testSuite.getTestSuiteId())
								.element("suiteName", testSuite.getSuiteName())
								.element("notes", testSuite.getNotes())
								.element(
										"isActive",
										testSuite.getIsActiveString(testSuite
												.getIsActive()))
								.element(
										"organizationName",
										testSuite.getOrganization()
												.getOrganizationName())
								.element("projectName",
										testSuite.getProject().getProjectName())
								.element("projectId",
										testSuite.getProject().getProjectId())
								.element("createdBy", testSuite.getCreatedBy())
								.element("createdOn", testSuite.getCreatedOn())
								.element("updatedBy", testSuite.getUpdatedBy())
								.element("updatedOn", updatedOn)
								.element("testSuiteUrl",
										testSuite.getTestSuiteUrl())

								.element("testSuiteXmlPathInJar",
										testSuite.getTestSuiteXmlPathInJar());
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapTestSuiteListpagination(
			List<TestSuite> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuite.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuite)) {
							return new JSONObject(true);
						}

						TestSuite testSuite = (TestSuite) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String createdOnString = "";
						if (testSuite.getCreatedOn() != null) {
							createdOnString = importDateFormat.format(testSuite
									.getCreatedOn());
						}
						String updatedOnString = "";
						if (testSuite.getUpdatedOn() != null) {
							updatedOnString = importDateFormat.format(testSuite
									.getUpdatedOn());
						}

						Date datee = testSuite.getUpdatedOn();
						SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
								"dd-MM-yy:HH:mm:SS");
						String updatedOn = DATE_FORMAT.format(datee);

						return new JSONObject()

								.element("testSuiteId",
										testSuite.getTestSuiteId())
								.element("suiteName", testSuite.getSuiteName())
								.element("notes", testSuite.getNotes())
								.element(
										"isActive",
										testSuite.getIsActiveString(testSuite
												.getIsActive()))
								.element(
										"organizationName",
										testSuite.getOrganization()
												.getOrganizationName())
								.element("projectName",
										testSuite.getProject().getProjectName())
								.element("projectId",
										testSuite.getProject().getProjectId())
								.element("createdBy", testSuite.getCreatedBy())
								.element("createdOn", createdOnString)
								.element("updatedBy", testSuite.getUpdatedBy())
								.element("updatedOn", updatedOnString)
								.element("testSuiteUrl",
										testSuite.getTestSuiteUrl())

								.element("testSuiteXmlPathInJar",
										testSuite.getTestSuiteXmlPathInJar());
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Common json methds
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
