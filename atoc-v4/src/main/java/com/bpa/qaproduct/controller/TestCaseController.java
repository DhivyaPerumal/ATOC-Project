package com.bpa.qaproduct.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.TestCaseService;
import com.bpa.qaproduct.service.TestSuiteDetailService;

@Controller
public class TestCaseController {

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestSuiteDetailService testSuiteDetailService;

	protected final Log logger = LogFactory.getLog(TestCaseController.class);

	// List Service

	@RequestMapping(value = "/testCase/viewTestCaseList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewTestCaseList(HttpServletRequest request) {

		
		
		
		try {
			

			// Constructing User Search Object
			TestCase testCase = new TestCase();

			String testCaseName = request.getParameter("testCaseName");
			if (testCaseName != null && testCaseName.isEmpty() == false) {
				testCase.setTestCaseName(testCaseName);
			}
			User user=  new User();
			String id_value = "";
			id_value = request.getParameter("userId");
			
			logger.info("Getting User Object based on Id :"+id_value);
			int totalResults = testCaseService.getTestCaseFilterCount(testCase);
			List<TestCase> list = testCaseService.getAllTestCaseList(testCase);

			//logger.info("Returned list size" + list.size());

			return getModelMapTestCaseList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/testCase/saveTestCase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveUser(HttpServletRequest request) {
		//logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			TestCase testCase = new TestCase();
			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("testCaseId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testCaseId")))) {
				id_value = request.getParameter("testCaseId");
				testCase = testCaseService.getTestCaseById(Integer
						.parseInt(id_value));
				testCase.setCreatedOn(todayDate);
				testCase.setUpdatedOn(todayDate);
			} else {
				testCase.setCreatedOn(todayDate);
				testCase.setUpdatedOn(todayDate);
			}

			String testCaseName = request.getParameter("testCaseName");
			testCase.setTestCaseName(testCaseName);

			String testCaseDetail = request.getParameter("testCaseDetail");
			testCase.setTestCaseDetail(testCaseDetail);

			String notes = request.getParameter("notes");
			testCase.setNotes(notes);

			String isActive = request.getParameter("isActive");
			testCase.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			testCase.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			testCase.setUpdatedBy(Integer.parseInt(updatedBy));

			String testSuiteDetailId_str = request
					.getParameter("testSuiteDetail");
			TestSuiteDetail testSuiteDetail = new TestSuiteDetail();
			testSuiteDetail = testSuiteDetailService
					.getTestSuiteDetailById((Integer
							.parseInt(testSuiteDetailId_str)));
			testCase.setTestSuiteDetail(testSuiteDetail);
			
			testCaseService.saveTestCase(testCase);

			//logger.info("Insert Method Executed.,");
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

	// uploadTestCase Service
	@RequestMapping(value = "/testcase/uploadTestCase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadTestCase(HttpServletRequest request) {
		//logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {

			TestCase testCase = new TestCase();
			TestSuiteDetail tSuiteDetail = new TestSuiteDetail();

			logger.info("*********** Req Context type :"
					+ request.getContentType());
			logger.info("*********** Req Context Path :"
					+ request.getContextPath());
			logger.info("*********** Req File :" + request.getPathInfo());
			logger.info("*********** testCaseName is :"
					+ request.getParameter("testCaseName"));
			logger.info("*********** notes is :"
					+ request.getParameter("notes"));
			logger.info("*********** testCaseUrl is :"
					+ request.getParameter("testCaseUrl"));
			logger.info("*********** testcaseFile is :"
					+ request.getParameter("testcaseFile"));

			logger.info("*********** File Stored Path is :"
					+ request.getParameter("filePath"));

			String testCaseName = request.getParameter("testCaseName");
			testCase.setTestCaseName(testCaseName);

			testCase.setTestCaseDetail("SampleStatic Test Case Detail");

			String notes = request.getParameter("notes");
			testCase.setNotes(notes);

			testCase.setIsActive("Y");
			testCase.setCreatedBy(100);
			testCase.setCreatedOn(new Date());
			testCase.setUpdatedBy(200);
			testCase.setUpdatedOn(new Date());
			testCase.setTestSuiteDetail(testSuiteDetailService
					.getTestSuiteDetailById(3)); // getting test suite detail object value by passing here

			String testCaseStored_filePath = "";
			if (request.getParameter("filePath") != null) {
				testCaseStored_filePath = request.getParameter("filePath");
			}
			File uploaded_File = new File(testCaseStored_filePath); // getting file path and storing in file variable
			int extIndex = uploaded_File.getName().lastIndexOf("."); // 

			testCase.setTestCaseFileName(uploaded_File.getName()); // getting only file name

			testCase.setTestCaseFileType(uploaded_File.getName().substring(
					extIndex)); //  getting extension by using sub string

			testCase.setTestCaseUploadedPath(testCaseStored_filePath);

			String user_testCaseUrl = request.getParameter("testcaseFile");
			testCase.setTestCaseUrl(user_testCaseUrl);

			testCaseService.saveTestCase(testCase);

			//logger.info("Insert Method Executed.,");
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

	@RequestMapping(value = "/testcase/uploadTestCaseFileUpload", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> uploadTestCaseFileUpload(HttpServletRequest request) {
		//logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {
			request.getParameter("testCaseId");
			TestCase testCase = new TestCase();
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy h-m-s-a");
			Date date = new Date();
			
			String dest = "E:\\TestCaseUploads\\TestCaseDemo" +formatter.format(date)+".txt";

			// File fin = new File(source);
			// FileInputStream fis = new FileInputStream(fin);
			BufferedReader bReader = new BufferedReader(request.getReader());

			FileWriter fstream = new FileWriter(dest, false);
			BufferedWriter bWriter = new BufferedWriter(fstream);

			String eachLine = null;
			while ((eachLine = bReader.readLine()) != null) {
				// Process each line and add output to Dest.txt file
				bWriter.write(eachLine);
				bWriter.newLine();
			}
			// do not forget to close the buffer reader
			bReader.close();
			// close buffer writer
			bWriter.close();

			//logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("filepath", dest);
			//logger.info("File Path Success " + dest);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("filepath", null);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	// Delete Service
	@RequestMapping(value = "/testCase/deleteTestCase", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteUser(HttpServletRequest request) {

		//logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int testCaseId = Integer.parseInt(request.getParameter("testCaseId"));
		try {
			TestCase testCase = testCaseService.getTestCaseById(testCaseId);
			testCaseService.removeTestCase(testCase);
			//logger.info("Delete Method Completed.,");
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
						Date datee = testCase.getUpdatedOn();
						SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
								"dd-MM-yy:HH:mm:SS");
						String updatedOn = DATE_FORMAT.format(datee);
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
								.element("createdOn", testCase.getCreatedOn())
								.element("updatedBy", testCase.getUpdatedBy())
								.element("updatedOn", updatedOn)
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
}
