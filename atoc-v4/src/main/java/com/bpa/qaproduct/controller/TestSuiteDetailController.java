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
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.service.TestSuiteDetailService;
import com.bpa.qaproduct.service.TestSuiteService;

@Controller
public class TestSuiteDetailController {

	@Autowired
	private TestSuiteDetailService testSuiteDetailService;

	@Autowired
	private TestSuiteService testSuiteService;

	protected final Log logger = LogFactory
			.getLog(TestSuiteDetailController.class);

	// List Service

	@RequestMapping(value = "/testSuiteDetail/viewTestSuiteDetailList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewTestSuiteDetailList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			TestSuiteDetail testSuiteDetail = new TestSuiteDetail();

			String operatingSystem = request.getParameter("operatingSystem");
			if (operatingSystem != null && operatingSystem.isEmpty() == false) {
				testSuiteDetail.setOperatingSystem(operatingSystem);
			}
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			int totalResults = testSuiteDetailService
					.getTestSuiteDetailFilterCount(testSuiteDetail);
			List<TestSuiteDetail> list = testSuiteDetailService
					.getAllTestSuiteDetailList(testSuiteDetail);

			// logger.info("Returned list size" + list.size());

			return getModelMapTestSuiteDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/testSuiteDetail/saveTestSuite", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveTestSuite(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			TestSuiteDetail testSuiteDetail = new TestSuiteDetail();
			String id_value = "";

			if ((StringUtils.isNotBlank(request
					.getParameter("testSuiteDetailId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiteDetailId")))) {
				id_value = request.getParameter("testSuiteDetailId");
				testSuiteDetail = testSuiteDetailService
						.getTestSuiteDetailById(Integer.parseInt(id_value));
				testSuiteDetail.setCreatedOn(todayDate);
				testSuiteDetail.setUpdatedOn(todayDate);
			} else {

				testSuiteDetail.setCreatedOn(todayDate);
				testSuiteDetail.setUpdatedOn(todayDate);
			}

			String operatingSystem = request.getParameter("operatingSystem");
			testSuiteDetail.setOperatingSystem(operatingSystem);

			String browser = request.getParameter("browser");
			testSuiteDetail.setBrowser(browser);

			String browserVersion = request.getParameter("browserVersion");
			testSuiteDetail.setBrowserVersion(browserVersion);

			String isActive = request.getParameter("isActive");
			testSuiteDetail.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			testSuiteDetail.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			testSuiteDetail.setUpdatedBy(Integer.parseInt(updatedBy));

			String testSuiteId_str = request.getParameter("testSuite");
			TestSuite testSuite = new TestSuite();
			testSuite = testSuiteService.getTestSuiteById((Integer
					.parseInt(testSuiteId_str)));
			testSuiteDetail.setTestSuite(testSuite);

			testSuiteDetailService.saveTestSuiteDetail(testSuiteDetail);

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
	@RequestMapping(value = "/testSuiteDetail/deleteTestSuiteDetail", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteTestSuiteDetail(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int testSuiteDetailId = Integer.parseInt(request
				.getParameter("testSuiteDetailId"));
		try {
			TestSuiteDetail testSuiteDetail = testSuiteDetailService
					.getTestSuiteDetailById(testSuiteDetailId);
			testSuiteDetailService.removeTestSuiteDetail(testSuiteDetail);
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
	private Map<String, Object> getModelMapTestSuiteDetailList(
			List<TestSuiteDetail> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuiteDetail.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuiteDetail)) {
							return new JSONObject(true);
						}

						TestSuiteDetail testSuiteDetail = (TestSuiteDetail) bean;
						return new JSONObject()
								.element("testSuiteDetailId",
										testSuiteDetail.getTestSuiteDetailId())
								.element("operatingSystem",
										testSuiteDetail.getOperatingSystem())
								.element("browser",
										testSuiteDetail.getBrowser())
								.element("browserVersion",
										testSuiteDetail.getBrowserVersion())
								.element("isActive",
										testSuiteDetail.getIsActive())
								.element("createdBy",
										testSuiteDetail.getCreatedBy())
								.element("createdOn",
										testSuiteDetail.getCreatedOn())
								.element("updatedBy",
										testSuiteDetail.getUpdatedBy())
								.element("updatedOn",
										testSuiteDetail.getUpdatedOn());
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
