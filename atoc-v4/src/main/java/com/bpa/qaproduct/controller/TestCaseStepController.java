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

import com.bpa.qaproduct.entity.TestCase;
import com.bpa.qaproduct.entity.TestCaseStep;
import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.service.TestCaseService;
import com.bpa.qaproduct.service.TestCaseStepService;

@Controller
public class TestCaseStepController {
	
	@Autowired
	private TestCaseStepService testCaseStepService;
	
	@Autowired
	private TestCaseService testCaseService;
	
	
	protected final Log logger = LogFactory.getLog(TestCaseStepController.class);
	
	
	// List Service
	
	@RequestMapping(value="/testCaseStep/viewTestCaseStepList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewTestCaseStepList(HttpServletRequest request) 
	{
		
		int start = 0;
		
		int limit = 15;
	
		
		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		try{
			
			
			// Constructing User Search Object
			TestCaseStep testCaseStep = new TestCaseStep();	
			
			String command = request.getParameter("command");
			if (command != null && command.isEmpty() == false) {
				testCaseStep.setCommand(command);
			}
			String id_value = "";
			id_value = request.getParameter("userId");
			
			logger.info("Getting User Object based on Id :"+id_value);
			int totalResults = testCaseStepService.getTestCaseStepFilterCount(testCaseStep);
			List<TestCaseStep> list = testCaseStepService.getTestCaseStepList(testCaseStep,start,limit);					
			
			
			//logger.info("Returned list size" + list.size());
			
			return getModelMapTestCaseStepList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List."+e.getMessage());
		}
	}
	
	
	// Save Service
	@RequestMapping(value="/testCaseStep/savetestCaseStep",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> savetestCaseStep(HttpServletRequest request) {
		//logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try
		{
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate
					.getTime()));

			TestCaseStep testCaseStep = new TestCaseStep();	
			String id_value = "";
			
			if ((StringUtils.isNotBlank(request.getParameter("testCaseStepId"))) || (StringUtils.isNotEmpty(request.getParameter("testCaseStepId"))))
			{
				id_value = request.getParameter("testCaseStepId");
				testCaseStep =  testCaseStepService.getTestCaseStepById(Integer.parseInt(id_value));
				testCaseStep.setCreatedOn(todayDate);
				testCaseStep.setUpdatedOn(todayDate);
			}
			else
			{
				testCaseStep.setCreatedOn(todayDate);
				testCaseStep.setUpdatedOn(todayDate);
			}
			
			String command = request.getParameter("command");
			testCaseStep.setCommand(command);
			
			String locator = request.getParameter("locator");
			testCaseStep.setLocator(locator);
			
			String value = request.getParameter("value");
			testCaseStep.setValue(value);
			
			String order_no = request.getParameter("order_no");
			testCaseStep.setOrder_no(Integer.parseInt(order_no));
			
			String createdBy=request.getParameter("createdBy");
			testCaseStep.setCreatedBy(Integer.parseInt(createdBy));
			
			String updatedBy=request.getParameter("updatedBy");
			testCaseStep.setUpdatedBy(Integer.parseInt(updatedBy));
			
			String testCaseId_str = request.getParameter("testCase");
			TestCase testCase = new TestCase();
			testCase = testCaseService.getTestCaseById((Integer.parseInt(testCaseId_str)));
			testCaseStep.setTestCase(testCase);
			
			
			testCaseStepService.saveTestCaseStep(testCaseStep);
			
			//logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		}
		catch(Exception ex)
		{
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
		
		

	}
	
	// Delete Service
	@RequestMapping(value="/testCaseStep/deleteTestCaseStep",method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteTestCaseStep(HttpServletRequest request) {
	

		//logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		
		int testCaseStepId = Integer.parseInt(request.getParameter("testCaseStepId"));
		try
		{
			TestCaseStep testCaseStep = testCaseStepService.getTestCaseStepById(testCaseStepId); 
			testCaseStepService.removeTestCaseStep(testCaseStep);
			//logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;
			
		}
		catch(Exception ex)
		{
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}
		

	}
	
	// JSon Construction
	private Map<String, Object> getModelMapTestCaseStepList(List<TestCaseStep> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal",    totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestCaseStep.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestCaseStep)) {
							return new JSONObject(true);
						}
						
						TestCaseStep testCaseStep = (TestCaseStep) bean;	
						return new JSONObject()
								.element("testCaseStepId",testCaseStep.getTestCaseStepId())
								.element("command", testCaseStep.getCommand())
								.element("locator",testCaseStep.getLocator())
								.element("value", testCaseStep.getValue())
								.element("order_no",testCaseStep.getOrder_no())
								.element("testCase",testCaseStep.getTestCase().getTestCaseId())
								.element("createdBy", testCaseStep.getCreatedBy())
								.element("createdOn", testCaseStep.getCreatedOn())
								.element("updatedBy", testCaseStep.getUpdatedBy())
								.element("updatedOn", testCaseStep.getUpdatedOn())
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
	 * Common json methds
	 */
	
	private ModelAndView getModelMap(){
		
		Map<String,Object> modelMap = new HashMap<String,Object>(1);
		modelMap.put("success", true);
		return new ModelAndView("jsonView", modelMap);
	}
	private Map<String, Object> getModelMapError(String msg){

		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}
}
