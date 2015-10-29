package com.bpa.qaproduct.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.TestCaseService;
import com.bpa.qaproduct.service.TestSuiteDetailService;

@Controller
public class TestCaseExecutionController {

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestSuiteDetailService testSuiteDetailService;

	protected final Log logger = LogFactory.getLog(TestCaseController.class);

	@RequestMapping(value = "/testCase/getTestCaseExecution", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getTestCaseExecution(HttpServletRequest request) {
		//logger.info("Test Case execution Method Strarted..");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {

			String testCaseId = request.getParameter("testCaseId");
			//logger.info("Test Case Id" + testCaseId);
			if (testCaseId != null) {

				TestCase testCase = testCaseService.getTestCaseById(Integer
						.parseInt(testCaseId));
				String filePath = testCase.getTestCaseUploadedPath() + "/"
						+ testCase.getTestCaseFileName() + "."
						+ testCase.getTestCaseFileType();
				TestCaseExecutionController testExCon = new TestCaseExecutionController();
				testExCon.executeSelenium(filePath, testCase.getTestCaseUploadedPath());
			}
			User user=  new User();
			String id_value = "";
			id_value = request.getParameter("userId");
			
			logger.info("Getting User Object based on Id :"+id_value);
			// logger.info("get the test case ID***"+testcase);
			// int totalResults =
			// testCaseService.getTestCaseFilterCount(testCase);
			// List<TestCase> list = testCaseService.getTestCaseList(start,
			// limit, testCase);

			// logger.info("Returned list size" +testcase.getTestCaseName());

			/*
			 * String testCaseId = request.getParameter("testCaseId");
			 * //searchId.setTestCaseId(Integer.parseInt(testCaseId));
			 * logger.info("test Case Id ******"+testCaseId); if (testCaseId !=
			 * null && testCaseId.isEmpty() == false) {
			 * testCase.setTestCaseId(Integer.parseInt(testCaseId)); }
			 * 
			 * TestCase testcase =
			 * testCaseService.getTestCaseId(Integer.parseInt(testCaseId));
			 * logger.info("get the test case ID***"+testcase);
			 * 
			 * 
			 * 
			 * 
			 * String id_value = ""; id_value =
			 * request.getParameter("testCaseId");
			 * logger.info("Test Case Id***"+id_value);
			 * 
			 * 
			 * String testCaseUrl=request.getParameter("testCaseUrl");
			 * logger.info("Test case Url**"+testCaseUrl);
			 */

			return null;

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// JSon Construction
	private Map<String, Object> getModelMapTestCaseList(List<TestCase> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestCase.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestCase)) {
							return new JSONObject(true);
						}

						TestCase testCase = (TestCase) bean;
						
						
						String createdOn = null;
						SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSSSS");
						if (testCase.getCreatedOn()!= null) {
							createdOn = sdf.format(testCase.getCreatedOn());
						}
						
						String updatedON = null;
						if (testCase.getCreatedOn()!= null) {
							createdOn = sdf.format(testCase.getCreatedOn());
						}
						
						return new JSONObject()
								.element("testCaseId", testCase.getTestCaseId())
								.element("testCaseName",
										testCase.getTestCaseName())
								.element("testCaseDetail",
										testCase.getTestCaseDetail())
								.element("notes", testCase.getNotes())
								.element("isActive", testCase.getIsActive())
								.element(
										"testSuiteDetail",
										testCase.getTestSuiteDetail()
												.getTestSuiteDetailId())
								.element("createdBy", testCase.getCreatedBy())
								.element("createdOn", createdOn)
								.element("updatedBy", testCase.getUpdatedBy())
								.element("updatedOn", testCase.getUpdatedOn())
								.element("testCaseUrl",
										testCase.getTestCaseUrl());
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
	public void executeSelenium(String filePath, String basePath) {
		String command = "java -jar "+basePath+"/selenium-server-standalone-2.44.0.jar -htmlSuite \"*firefox C:/Program Files (x86)/Mozilla Firefox/firefox.exe\" \"https://www.matrixeservicesuat.com\" "+filePath+" \"results.html\"";
		//logger.info("**************command line command is******************"+command);
		/*String command1 =
				 "java -jar C:/TestCases/selenium-server-standalone-2.44.0.jar -htmlSuite \"*firefox C:/Program Files (x86)/Mozilla Firefox/firefox.exe\" \"https://www.matrixeservicesuat.com\" \"C:/TestCases/testsuitmatrix.html\" \"results.html\"";*/
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);

		builder.redirectErrorStream(true);
		Process p = null;
		try {
			p = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line = null;
		while (true) {
			try {
				line = r.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
	}
}
