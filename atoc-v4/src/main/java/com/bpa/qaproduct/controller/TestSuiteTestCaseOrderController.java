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
import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.entity.TestSuiteTestCaseOrder;
import com.bpa.qaproduct.service.TestCaseService;
import com.bpa.qaproduct.service.TestSuiteDetailService;
import com.bpa.qaproduct.service.TestSuiteTestCaseOrderService;

@Controller
public class TestSuiteTestCaseOrderController {

	@Autowired
	private TestSuiteTestCaseOrderService testCaseOrderService;

	@Autowired
	private TestSuiteDetailService testSuiteDetailService;

	@Autowired
	private TestCaseService testCaseService;

	protected final Log logger = LogFactory
			.getLog(TestSuiteTestCaseOrderController.class);

	// List Service

	@RequestMapping(value = "/tSTestCaseOrder/viewtSTestCaseOrderList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewtSTestCaseOrderList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			TestSuiteTestCaseOrder testCaseOrder = new TestSuiteTestCaseOrder();

			String orderNo = request.getParameter("orderNo");
			if (orderNo != null && orderNo.isEmpty() == false) {
				testCaseOrder.setOrderNo((Integer.parseInt(orderNo)));
			}
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			int totalResults = testCaseOrderService
					.getTestSuiteTestCaseOrderDetailFilterCount(testCaseOrder);
			List<TestSuiteTestCaseOrder> list = testCaseOrderService
					.getAllTestSuiteTestCaseOrderList(testCaseOrder);

			// logger.info("Returned list size" + list.size());

			return getModelMapTestSuiteTestCaseOrderList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/tSTestCaseOrder/savetSTestCaseOrder", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveUser(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			TestSuiteTestCaseOrder testCaseOrder = new TestSuiteTestCaseOrder();
			String id_value = "";

			if ((StringUtils.isNotBlank(request
					.getParameter("testSuiterDetail")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiterDetail")))) {
				id_value = request.getParameter("testSuiterDetail");
				testCaseOrder = testCaseOrderService
						.getTestSuiteTestCaseOrderById(Integer
								.parseInt(id_value));

			} else {

			}

			String orderNo = request.getParameter("orderNo");
			testCaseOrder.setOrderNo(Integer.parseInt(orderNo));

			String testSuiteDetailId_str = request
					.getParameter("testSuiteDetail");
			TestSuiteDetail testSuiteDetail = new TestSuiteDetail();
			testSuiteDetail = testSuiteDetailService
					.getTestSuiteDetailById((Integer
							.parseInt(testSuiteDetailId_str)));
			testCaseOrder.setTestSuiterDetail(testSuiteDetail);

			String testCaseId_str = request.getParameter("testCase");
			TestCase testCase = new TestCase();
			testCase = testCaseService.getTestCaseById((Integer
					.parseInt(testCaseId_str)));
			testCaseOrder.setTestCase(testCase);

			testCaseOrderService.saveTestSuiteTestCaseOrder(testCaseOrder);

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
	@RequestMapping(value = "/tSTestCaseOrder/deletetSTestCaseOrder", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deletetSTestCaseOrder(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int userId = Integer.parseInt(request.getParameter("userId"));
		try {
			TestSuiteTestCaseOrder testCaseOrder = testCaseOrderService
					.getTestSuiteTestCaseOrderById(userId);
			testCaseOrderService.removeTestSuiteTestCaseOrder(testCaseOrder);
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
	private Map<String, Object> getModelMapTestSuiteTestCaseOrderList(
			List<TestSuiteTestCaseOrder> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuiteTestCaseOrder.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuiteTestCaseOrder)) {
							return new JSONObject(true);
						}

						TestSuiteTestCaseOrder testCaseOrder = (TestSuiteTestCaseOrder) bean;
						return new JSONObject()
								.element("orderNo", testCaseOrder.getOrderNo())
								.element(
										"testSuiterDetail",
										testCaseOrder.getTestSuiterDetail()
												.getTestSuiteDetailId())
								.element(
										"testCase",
										testCaseOrder.getTestCase()
												.getTestCaseId())

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
