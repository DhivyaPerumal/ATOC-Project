package com.bpa.qaproduct.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.bpa.qaproduct.entity.DashBoardView;
import com.bpa.qaproduct.entity.ExecutionMethodParameter;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.DashBoardViewService;
import com.bpa.qaproduct.service.ExecutionResultService;
import com.bpa.qaproduct.service.ProjectService;
import com.bpa.qaproduct.service.TestSuiteExecutionService;
import com.bpa.qaproduct.service.TestSuiteService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class DashBoardViewController {
	@Autowired
	private DashBoardViewService dashBoardViewService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private TestSuiteService testSuiteService;

	@Autowired
	private TestSuiteExecutionService testSuiteExecutionService;

	@Autowired
	private ExecutionResultService executionResultService;

	protected final Log logger = LogFactory
			.getLog(DashBoardViewController.class);

	@RequestMapping(value = "/dashboardview/viewDashBoardViewList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewDashBoardViewList(HttpServletRequest request) {

		try {
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);

			// Constructing CustomerRegistration Search Object
			DashBoardView dashBoardView = new DashBoardView();

			String searchProjectName = request.getParameter("projectName");
			if (searchProjectName != null
					&& searchProjectName.isEmpty() == false) {
				dashBoardView.setProjectName(searchProjectName);
			}

			int totalResults = dashBoardViewService
					.getDashBoardViewFilterCount(dashBoardView);
			List<DashBoardView> list = dashBoardViewService
					.getAllDashBoardViewList(dashBoardView);

			// logger.info("Returned list size" + list.size());

			return getModelMapDashBoardViewList(list, totalResults);

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Display Last Execution
	@RequestMapping(value = "/dashboardview/getLastExecutionResult", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getLastExecutionResult(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {

			User user = new User();
			ExecutionResult executionResult = new ExecutionResult();

			String userId = request.getParameter("userId");
			if (request.getParameter("userId") != null
					|| request.getParameter("userId") == "") {
				user = userService.getUserById(Integer.parseInt(userId));
			}

			List<ExecutionResult> executionResultList = new ArrayList<ExecutionResult>();
			executionResult.setUpdatedBy(user.getUserId());
			executionResult = executionResultService.getLastExecutionResult(executionResult);

			if (executionResult != null) {

				executionResultList.add(executionResult);

			}
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return getModelMapExecutionResultList(executionResultList,
					executionResultList.size());

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/dashboardview/getTotalCountExecution", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getTotalCountExecution(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {

			ExecutionResult executionResult = new ExecutionResult();
			Project project = new Project();
			project.setProjectId(57);
			Organization organization = new Organization();
			organization.setOrganizationId(2);

			User user = new User();
			user.setOrganization(organization);

			TestSuite testSuite = new TestSuite();
			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			String userId = request.getParameter("userId");
			if (request.getParameter("userId") != null
					|| request.getParameter("userId") == "") {
				user = userService.getUserById(Integer.parseInt(userId));
				project.setOrganization(user.getOrganization());

			}

			List countProjectlist = testSuiteExecutionService
					.getTotalCountExecution(user.getOrganization());

			logger.info("Returned list size" + countProjectlist.size());

			logger.info("getTotalCountExecution Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("Count", "Count Successfully");
			modelMap.put("list", countProjectlist);

			return modelMap;

		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem while getting data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	// JSON Construction for Project Result
	private Map<String, Object> getModelMapprojectList(List list,
			String totalResults) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		logger.info("TotalResults" + totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Project.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof List)) {
							return new JSONObject(true);
						}
						Project project = new Project();

						return new JSONObject().element("projectName",
								project.getProjectName());
					}
				});
		JSON json = JSONSerializer.toJSON(list, jsonConfig);
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction
	private Map<String, Object> getModelMapDashBoardViewList(
			List<DashBoardView> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(DashBoardView.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof DashBoardView)) {
							return new JSONObject(true);
						}

						DashBoardView dashBoardView = (DashBoardView) bean;
						return new JSONObject()
								.element("projectId",
										dashBoardView.getProjectId())
								.element("projectName",
										dashBoardView.getProjectName())

								.element("testSuiteCount",
										dashBoardView.getTestSuiteCount())

						;
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction for Execution Result
	private Map<String, Object> getModelMapExecutionResultList(
			List<ExecutionResult> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(ExecutionResult.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof ExecutionResult)) {
							return new JSONObject(true);
						}

						ExecutionResult executionResult = (ExecutionResult) bean;
						/* EXECUTION RESULT DETAILS MAPPING TO JSON */

						Set<ExecutionResultDetail> executionResultDetails = executionResult
								.getExecutionResultDetails();
						JsonConfig jsonExecutionResultDetailConfig = new JsonConfig();

						jsonExecutionResultDetailConfig
								.registerJsonBeanProcessor(
										ExecutionResultDetail.class,
										new JsonBeanProcessor() {

											public JSONObject processBean(
													Object bean,
													JsonConfig jsonConfig) {
												if (!(bean instanceof ExecutionResultDetail)) {
													return new JSONObject(true);
												}

												//

												/*
												 * EXECUTION METHOD PARAMETER
												 * MAPPING TO JSON
												 */

												ExecutionResultDetail executionResultDetail = (ExecutionResultDetail) bean;
												Set<ExecutionMethodParameter> executionMethodParameters = executionResultDetail
														.getExecutionMethodParameters();
												JsonConfig jsonExecutionMethodParameterDetailConfig = new JsonConfig();
												jsonExecutionMethodParameterDetailConfig
														.registerJsonBeanProcessor(
																ExecutionMethodParameter.class,
																new JsonBeanProcessor() {

																	public JSONObject processBean(
																			Object bean,
																			JsonConfig jsonConfig) {
																		if (!(bean instanceof ExecutionMethodParameter)) {
																			return new JSONObject(
																					true);
																		}
																		ExecutionMethodParameter executionMethodParameter = (ExecutionMethodParameter) bean;
																		return new JSONObject()
																				.element(
																						"executionParameterId",
																						executionMethodParameter
																								.getExecutionParameterId())
																				.element(
																						"parameterIndex",
																						executionMethodParameter
																								.getParameterIndex())
																				.element(
																						"parameterValue",
																						executionMethodParameter
																								.getParameterValue());
																	}
																});

												JSON jsonExecutionMethodParameter = JSONSerializer
														.toJSON(executionMethodParameters,
																jsonExecutionMethodParameterDetailConfig);

												SimpleDateFormat formatter = new SimpleDateFormat(
														"MM/dd/yyyy");
												String startedAt = formatter
														.format(executionResultDetail
																.getStartedAt());
												String finishedAt = formatter
														.format(executionResultDetail
																.getFinishedAt());

												return new JSONObject()
														.element(
																"executionResultDetailId",
																executionResultDetail
																		.getExecutionResultDetailId())
														.element(
																"testMethodName",
																executionResultDetail
																		.getTestMethodName())
														.element("startedAt",
																startedAt)
														.element("finishedAt",
																finishedAt)
														.element(
																"status",
																executionResultDetail
																		.getStatus())
														.element(
																"executionMethodParameters",
																jsonExecutionMethodParameter);

											}
										});

						JSON jsonExecutionResultDetail = JSONSerializer.toJSON(
								executionResultDetails,
								jsonExecutionResultDetailConfig);
						return new JSONObject()
								.element("executionResultId",
										executionResult.getExecutionResultId())
								.element("testExecutionName",
										executionResult.getTestExecutionName())
								.element("startTime",
										executionResult.getStartTime())
								.element("endTime",
										executionResult.getEndTime())
								.element("duration",
										executionResult.getDuration())
								.element("total", executionResult.getTotal())
								.element("passed", executionResult.getPassed())
								.element("failed", executionResult.getFailed())
								.element("skipped",
										executionResult.getSkipped())
								.element("executionStatus",
										executionResult.getExecutionStatus())
								.element("createdBy",
										executionResult.getCreatedBy())
								.element("createdOn",
										executionResult.getCreatedOn())
								.element("updatedBy",
										executionResult.getUpdatedBy())
								.element("updatedOn",
										executionResult.getUpdatedOn())
								.element("executionResultDetails",
										jsonExecutionResultDetail);
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

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
