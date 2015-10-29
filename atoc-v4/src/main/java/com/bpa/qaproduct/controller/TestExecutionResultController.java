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

import com.bpa.qaproduct.entity.TestExecutionResult;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.service.TestExecutionResultService;
import com.bpa.qaproduct.service.TestSuiteExecutionService;

@Controller
public class TestExecutionResultController {

	@Autowired
	private TestExecutionResultService testExecutionResultService;

	@Autowired
	private TestSuiteExecutionService testSuiteExecutionService;

	protected final Log logger = LogFactory
			.getLog(TestExecutionResultController.class);

	// List Service

	@RequestMapping(value = "/testExecutionResult/getTestExecResultList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getTestExecResultList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			TestExecutionResult testExecutionResult = new TestExecutionResult();

			String searchTestName = request.getParameter("searchTestName");
			if (searchTestName != null && searchTestName.isEmpty() == false) {
				// testExecutionResult.setTestName(searchTestName);
			}
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			int totalResults = testExecutionResultService
					.getTestExeResultIdFilterCount(testExecutionResult);
			List<TestExecutionResult> list = testExecutionResultService
					.getAlltestExeResultIdList(testExecutionResult);

			// logger.info("Returned list size" + list.size());

			return getModelMapTestExecutionResultList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/testExecutionResult/saveTestExecutionResult", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveTestExecutionResult(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			TestExecutionResult testExecutionResult = new TestExecutionResult();
			String id_value = "";

			if ((StringUtils
					.isNotBlank(request.getParameter("testExeResultId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testExeResultId")))) {
				id_value = request.getParameter("testExeResultId");
				testExecutionResult = testExecutionResultService
						.getTestExecutionResultById(Integer.parseInt(id_value));
				testExecutionResult.setCreatedOn(todayDate);
				testExecutionResult.setUpdatedOn(todayDate);
			} else {
				testExecutionResult.setCreatedOn(todayDate);
				testExecutionResult.setUpdatedOn(todayDate);
			}

			String testName = request.getParameter("testName");
			// testExecutionResult.setTestName(testName);

			String testSuiteMethod = request.getParameter("testSuiteMethod");
			// testExecutionResult.setTestSuiteMethod(testSuiteMethod);

			String createdBy = request.getParameter("createdBy");
			testExecutionResult.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			testExecutionResult.setUpdatedBy(Integer.parseInt(updatedBy));

			String testSuiteExecutionId_str = request
					.getParameter("testSuiteExecution");
			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			testSuiteExecution = testSuiteExecutionService
					.getTestSuiteExecutionById(Integer
							.parseInt(testSuiteExecutionId_str));
			testExecutionResult.setTestSuiteExecution(testSuiteExecution);
			;

			testExecutionResultService
					.saveTestExecutionResult(testExecutionResult);

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

	// JSon Construction
	private Map<String, Object> getModelMapTestExecutionResultList(
			List<TestExecutionResult> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestExecutionResult.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestExecutionResult)) {
							return new JSONObject(true);
						}

						TestExecutionResult testExecutionResult = (TestExecutionResult) bean;
						return new JSONObject()
								.element(
										"testExeResultId",
										testExecutionResult
												.getTestExeResultId())
								.element(
										"testSuiteExecution",
										testExecutionResult
												.getTestSuiteExecution()
												.getTestSuiteExecId())
								.element("createdBy",
										testExecutionResult.getCreatedBy())
								.element("createdOn",
										testExecutionResult.getCreatedOn())
								.element("updatedBy",
										testExecutionResult.getUpdatedBy())
								.element("updatedOn",
										testExecutionResult.getUpdatedOn());
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
