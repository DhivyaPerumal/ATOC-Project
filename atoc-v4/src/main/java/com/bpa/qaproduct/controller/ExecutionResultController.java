package com.bpa.qaproduct.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.ExecutionMethodParameter;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.ExecutionResultException;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.ExecutionMethodParameterService;
import com.bpa.qaproduct.service.ExecutionResultDetailService;
import com.bpa.qaproduct.service.ExecutionResultService;
import com.bpa.qaproduct.service.TestSuiteExecutionService;
import com.bpa.qaproduct.service.TestSuiteService;
import com.bpa.qaproduct.service.UserService;
import com.bpa.qaproduct.util.CreateExcel;
import com.bpa.qaproduct.util.CreateExecutionExcel;
import com.bpa.qaproduct.util.CreatePDF;
import com.itextpdf.text.Document;

@Controller
public class ExecutionResultController {

	@Autowired
	private ExecutionResultService executionResultService;

	@Autowired
	private TestSuiteExecutionService testSuiteExecutionService;

	@Autowired
	private ExecutionResultDetailService executionResultDetailService;

	@Autowired
	private ExecutionMethodParameterService executionMethodParameterService;

	@Autowired
	private UserService userService;

	@Autowired
	private TestSuiteService testSuiteService;

	protected final Log logger = LogFactory
			.getLog(ExecutionResultController.class);

	// List Service
	@RequestMapping(value = "/executionResult/getExecutionResultList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getExecutionResultList(HttpServletRequest request) {

		try {

			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			// Constructing execution name Object
			ExecutionResult executionResult = new ExecutionResult();

			String testExecutionName = request
					.getParameter("testExecutionName");
			if (testExecutionName != null
					&& testExecutionName.isEmpty() == false) {
				executionResult.setTestExecutionName(testExecutionName);
			}
			int totalResults = executionResultService
					.getExecutionResultCount(executionResult);
			List<ExecutionResult> list = executionResultService
					.getAllExecutionResultIdList(executionResult);

			// logger.info("Returned list size" + list.size());

			return getModelMapExecutionResultList(list, totalResults);

		} catch (Exception e) {

			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Get Object Service
	@RequestMapping(value = "/executionResult/getExecutionResult", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getExecResByTestSuiteExec(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			String id_value = "";
			id_value = request.getParameter("userId");
			if (id_value != null) {
				User user = new User();
				logger.info("Logged on User Id :" + id_value);
				user = userService.getUserById(Integer.parseInt(id_value));
				logger.info("Logged in User Email is : :" + user.getUserEmail());

			}

			ExecutionResult executionResult = new ExecutionResult();
			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			String testSuiteExecId_str = request
					.getParameter("testSuiteExecId");
			if (testSuiteExecId_str != null) {

				testSuiteExecution = testSuiteExecutionService
						.getTestSuiteExecutionById(Integer
								.parseInt(testSuiteExecId_str));
				logger.info("testSuiteExecution Id id :"
						+ testSuiteExecution.getTestSuiteExecName());

			}

			executionResult.setTestSuiteExecution(testSuiteExecution);
			if (executionResult.getTestSuiteExecution() != null) {
				Integer testExecId = executionResult.getTestSuiteExecution()
						.getTestSuiteExecId();
				logger.info("testExecId is :" + testExecId);
				executionResult = executionResultService
						.getExecutionResultBySearchParameter(executionResult);
				logger.info("executionResult Id id :"
						+ executionResult.getExecutionResultId());
			}
			List<ExecutionResult> executionResultsList = new ArrayList<ExecutionResult>();
			if (executionResult != null) {
				executionResultsList.add(executionResult);
			}

			logger.info("getExecResByTestSuiteExec Method Executed.,");
			return getModelMapExecutionResultList(executionResultsList,
					executionResultsList.size());

		} catch (Exception ex) {
			String msg = "Sorry problem in Getting data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			ex.printStackTrace();
			return modelMap;
		}

	}

	// Save Service
	@RequestMapping(value = "/executionResult/saveExecutionResult", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveExecutionResult(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			ExecutionResult executionResult = new ExecutionResult();
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			if ((StringUtils.isNotBlank(request
					.getParameter("executionResultId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("executionResultId")))) {
				id_value = request.getParameter("executionResultId");
				executionResult = executionResultService
						.getExecutionResultById(Integer.parseInt(id_value));
				executionResult.setCreatedOn(todayDate);
				executionResult.setUpdatedOn(todayDate);
				executionResult.setStartTime(todayDate);
				executionResult.setEndTime(todayDate);

			} else {
				executionResult.setCreatedOn(todayDate);
				executionResult.setUpdatedOn(todayDate);
				executionResult.setStartTime(todayDate);
				executionResult.setEndTime(todayDate);

			}

			String testExecutionName = request
					.getParameter("testExecutionName");
			if (testExecutionName != null) {
				executionResult.setTestExecutionName(testExecutionName);
				// logger.info("***test EXecution name***" + testExecutionName);
			}

			String createdBy = request.getParameter("createdBy");
			if (createdBy != null) {
				executionResult.setCreatedBy(Integer.parseInt(createdBy));
				// logger.info("***created by***" + createdBy);
			}

			String updatedBy = request.getParameter("updatedBy");
			if (updatedBy != null) {

				executionResult.setUpdatedBy(Integer.parseInt(updatedBy));
				// logger.info("***updated by***" + updatedBy);

			}

			String duration = request.getParameter("duration");
			if (duration != null) {

				executionResult.setDuration(duration);
				// logger.info("*****Duration***" + duration);
			}

			String total = request.getParameter("total");
			if (total != null) {

				executionResult.setTotal(Integer.parseInt(total));
				// logger.info("***total values***" + total);
			}

			String passed = request.getParameter("passed");
			if (passed != null) {

				executionResult.setPassed(Integer.parseInt(passed));
				// logger.info("***passed values***" + passed);
			}

			String failed = request.getParameter("failed");
			if (failed != null) {
				executionResult.setFailed(Integer.parseInt(failed));
				// logger.info("***failed values***" + failed);
			}
			String skipped = request.getParameter("skipped");
			if (skipped != null) {
				executionResult.setSkipped(Integer.parseInt(skipped));
				// logger.info("***skipped values***" + skipped);
			}
			String executionStatus = request.getParameter("executionStatus");
			if (executionStatus != null) {

				executionResult.setExecutionStatus(executionStatus);
				// logger.info("***execution status***" + executionStatus);
			}

			String testSuiteExecutionId_str = request
					.getParameter("testSuiteExecution");
			TestSuiteExecution testSuiteExecution = testSuiteExecutionService
					.getTestSuiteExecutionById(Integer
							.parseInt(testSuiteExecutionId_str));
			if (testSuiteExecutionId_str != null) {
				executionResult.setTestSuiteExecution(testSuiteExecution);
				logger.info("***test suite execution id***"
						+ testSuiteExecutionId_str);

			}
			Set<ExecutionResultDetail> executionResultDetailSet = new HashSet<ExecutionResultDetail>();
			Set<ExecutionMethodParameter> executionMethodParameterSet = new HashSet<ExecutionMethodParameter>();
			ExecutionMethodParameter executionMethodParameter = new ExecutionMethodParameter();
			ExecutionResultDetail executionResultDetail = new ExecutionResultDetail();
			String testMethodName = request.getParameter("testMethodName");
			executionResultDetail.setTestMethodName(testMethodName);

			executionResultDetail.setStartedAt(todayDate);
			executionResultDetail.setFinishedAt(todayDate);

			executionResultDetail.setCreatedBy(executionResult.getCreatedBy());
			executionResultDetail.setUpdatedBy(executionResult.getUpdatedBy());

			executionResultDetail.setDuration(executionResult.getDuration());

			String status = request.getParameter("status");
			executionResultDetail.setStatus(status);

			String description = request.getParameter("description");
			executionResultDetail.setDescription(description);

			String signature = request.getParameter("signature");
			executionResultDetail.setSignature(signature);

			String parameterIndex = request.getParameter("parameterIndex");
			executionMethodParameter.setParameterIndex(Integer
					.parseInt(parameterIndex));

			String parameterValue = request.getParameter("parameterValue");
			executionMethodParameter.setParameterValue(parameterValue);

			executionMethodParameter.setCreatedBy(executionResult
					.getCreatedBy());
			executionMethodParameter.setUpdatedBy(executionResult
					.getUpdatedBy());

			executionResultDetail
					.setExecutionMethodParameters(executionMethodParameterSet);

			executionResult.setExecutionResultDetails(executionResultDetailSet);

			executionResultService.saveExecutionResult(executionResult);

			// logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			ex.printStackTrace();
			return modelMap;
		}

	}

	// Report service
	@RequestMapping(value = "/executionResult/getExecMethodParamList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getExecMethodParamList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);

		try {
			ExecutionResultDetail executionResultDetail = new ExecutionResultDetail();
			ExecutionMethodParameter executionMethodParameter = new ExecutionMethodParameter();
			String executionResultDetailId_Str = request
					.getParameter("executionResultDetailId");
			if (executionResultDetailId_Str != null) {
				logger.info("ExecutionResultDetail Id is :"
						+ executionResultDetailId_Str);
				executionResultDetail = executionResultDetailService
						.getExecutionResultDetailById(Integer
								.parseInt(executionResultDetailId_Str));
				executionMethodParameter
						.setExecutionResultDetail(executionResultDetail);
			}

			int totalResults = executionMethodParameterService
					.getExecutionMethodParameterIdFilterCount(executionMethodParameter);
			List<ExecutionMethodParameter> list = executionMethodParameterService
					.getAllExecutionMethodParameterIdList(executionMethodParameter);

			logger.info("ExecutionMethodParameter List size is : "
					+ list.size());

			return getModelMapExecMParamList(list, totalResults);

		} catch (Exception e) {

			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Report service
	@RequestMapping(value = "/executionResult/getReportList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getReportList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);

		try {

			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			TestSuiteExecution testSuiteExecution_Side = new TestSuiteExecution();
			String testSuiteExecName_side = "";
			String testSuiteExecName = "";

			TestSuite testSuite = new TestSuite();

			String testSuite_Id = request.getParameter("testSuiteId");
			if (testSuite_Id != null) {
				testSuiteExecName = request.getParameter("testSuiteExecName");
				if (testSuiteExecName != null
						&& testSuiteExecName.isEmpty() == false) {
					testSuiteExecution.setTestSuiteExecName(testSuiteExecName);
					logger.info("Test Suite Execution Name is:"
							+ testSuiteExecName);
				}

				testSuite = testSuiteService.getTestSuiteById(Integer
						.parseInt(testSuite_Id));
				logger.info("Test Suite Id" + testSuite_Id);

				testSuiteExecution.setTestSuite(testSuite);
				testSuiteExecution.setTestSuiteExecName(testSuiteExecName);

				testSuiteExecution = testSuiteExecutionService
						.getTestSuiteExecutionBySearchParam(testSuiteExecution);
			}
			TestSuite testSuite_side = new TestSuite();

			String testSuite_SideId = request.getParameter("testSuite_SideId");
			if (testSuite_SideId != null) {

				testSuiteExecName_side = request
						.getParameter("testSuiteExecName_side");
				if (testSuiteExecName_side != null) {
					testSuiteExecution
							.setTestSuiteExecName(testSuiteExecName_side);
					logger.info("Test Suite Execution Name Side is:"
							+ testSuiteExecName_side);
				}

				testSuite_side = testSuiteService.getTestSuiteById(Integer
						.parseInt(testSuite_SideId));
				logger.info("Test Suite Id" + testSuite_SideId);

				testSuiteExecution_Side.setTestSuite(testSuite_side);
				testSuiteExecution_Side
						.setTestSuiteExecName(testSuiteExecName_side);

				testSuiteExecution_Side = testSuiteExecutionService
						.getTestSuiteExecutionBySearchParam(testSuiteExecution_Side);

			}

			ExecutionResult executionResult = new ExecutionResult();

			if (testSuiteExecution.getTestSuiteExecId() != null) {

				executionResult.setTestSuiteExecution(testSuiteExecution);
				executionResult.setTestExecutionName(testSuiteExecName);

				executionResult = executionResultService.getExecutionResultBySearchParameter(executionResult);
				logger.info("executionResult is :"
						+ executionResult.getExecutionResultId());
				logger.info("Execution result is :"
						+ executionResult.getTotal() + " : "
						+ executionResult.getPassed() + " : "
						+ executionResult.getFailed() + " : "
						+ executionResult.getSkipped());
				logger.info("report List");
			}

			ExecutionResult executionResult_Side = new ExecutionResult();
			if (testSuiteExecution_Side.getTestSuiteExecId() != null) {

				executionResult_Side
						.setTestSuiteExecution(testSuiteExecution_Side);
				executionResult_Side.setTestExecutionName(testSuiteExecName_side);
				// executionResult_Side.setTestExecutionName(testSuiteExecution_Side.getTestSuiteExecName());

				executionResult_Side = executionResultService.getExecutionResultBySearchParameter(executionResult_Side);
				logger.info("Execution Result_Side is :"
						+ executionResult_Side.getExecutionResultId());
				logger.info("Execution result is :"
						+ executionResult_Side.getTotal() + " : "
						+ executionResult_Side.getPassed() + " : "
						+ executionResult_Side.getFailed() + " : "
						+ executionResult_Side.getSkipped());

			}
			
			List<ExecutionResult> executionResults = new ArrayList<ExecutionResult>();
			if (executionResult != null && executionResult_Side != null) {
				executionResults.add(executionResult);
				executionResults.add(executionResult_Side);
			}
			
			
			return getModelMapExecutionResultList(executionResults,
					executionResults.size());

		} catch (Exception e) {
			
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}
	
	// Report service
		@RequestMapping(value = "/executionResult/getDownloadReport", method = RequestMethod.GET)
		public void getDownloadReport(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			Map<String, Object> modelMap = new HashMap<String, Object>(4);

			try {
				ExecutionResult executionResult  =  null;
				ExecutionResult executionResult_Side = null;
				
				logger.info("### Getting execution Result Id :" + request.getParameter("exeResId"));
				logger.info("### Getting execution Result Side Id :" + request.getParameter("exeResId_Side"));
				if ((StringUtils.isNotBlank(request
						.getParameter("exeResId")))
						|| (StringUtils.isNotEmpty(request
								.getParameter("exeResId")))) {
					executionResult = new ExecutionResult();
					String exeResultId_str = request.getParameter("exeResId");
					executionResult = executionResultService.getExecutionResultById(Integer.parseInt(exeResultId_str));
					logger.info("executionResult is :"
							+ executionResult.getExecutionResultId());
					logger.info("Execution result is :"
							+ executionResult.getTotal() + " : "
							+ executionResult.getPassed() + " : "
							+ executionResult.getFailed() + " : "
							+ executionResult.getSkipped());
					logger.info("report List");
				}
				if ((StringUtils.isNotBlank(request
						.getParameter("exeResId_Side")))
						|| (StringUtils.isNotEmpty(request
								.getParameter("exeResId_Side")))) {
					executionResult_Side = new ExecutionResult();
					String exeResultId_Side_str = request.getParameter("exeResId_Side");
					executionResult_Side = executionResultService.getExecutionResultById(Integer.parseInt(exeResultId_Side_str));
					logger.info("Execution Result_Side is :"
							+ executionResult_Side.getExecutionResultId());
					logger.info("Execution result is :"
							+ executionResult_Side.getTotal() + " : "
							+ executionResult_Side.getPassed() + " : "
							+ executionResult_Side.getFailed() + " : "
							+ executionResult_Side.getSkipped());
					logger.info("report List Side");

				}
				
				String downloadFormat = request.getParameter("downloadFormat");
				if(downloadFormat != null || downloadFormat == ""){
					if(downloadFormat.equalsIgnoreCase("pdf")){
						logger.info("**** ### Going to Create PDF doc Report");
						String fileName_pdf = executionResult.getExecutionResultId()+"_Compare_"+executionResult_Side.getExecutionResultId()+".pdf";
						logger.info("FileName is : "+fileName_pdf);
						
						final ServletContext servletContext = request.getSession().getServletContext();
					    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
					    final String temperoryFilePath = tempDirectory.getAbsolutePath();
						
					    logger.info("Temp Directory is : "+tempDirectory);
					    logger.info("Temperory FilePath: is : "+temperoryFilePath);
					    
					 // set headers for the response
						    response.setContentType("application/pdf");
						    response.setHeader("Content-disposition", "attachment; filename="+fileName_pdf);
						    try {
					CreatePDF.createPDF(temperoryFilePath+"\\"+fileName_pdf,executionResult,executionResult_Side);
					 ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        baos = convertPDFToByteArrayOutputStream(temperoryFilePath+"\\"+fileName_pdf);
				        OutputStream os = response.getOutputStream();
				        baos.writeTo(os);
				        os.flush();
						  } catch (Exception e1) {
						        e1.printStackTrace();
						    }
					
						    File file = new File(temperoryFilePath+"\\"+fileName_pdf);
							if(file.exists()){
							boolean success = file.delete();
							logger.info("File Deleted Successfully"+success +" : "+temperoryFilePath+"\\"+fileName_pdf);
							}
					
					
					}
					else if (downloadFormat.equalsIgnoreCase("excel")) {
						logger.info("**** ### Going to Create Excel Sheet Report");
						String fileName_excel = executionResult.getExecutionResultId()+"_Compare_"+executionResult_Side.getExecutionResultId()+".xlsx";
						logger.info("FileName is : "+fileName_excel);
						
						final ServletContext servletContext = request.getSession().getServletContext();
					    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
					    final String temperoryFilePath = tempDirectory.getAbsolutePath();
						
					    logger.info("Temp Directory is : "+tempDirectory);
					    logger.info("Temperory FilePath: is : "+temperoryFilePath);
					    
					 // set headers for the response
						    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						    response.setHeader("Content-disposition", "attachment; filename="+fileName_excel);
						    try {
					CreateExcel createExcel = new CreateExcel();
					createExcel.createExecutionReport_Excel(temperoryFilePath+"\\"+fileName_excel,executionResult,executionResult_Side);
					
					 ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        baos = convertPDFToByteArrayOutputStream(temperoryFilePath+"\\"+fileName_excel);
				        OutputStream os = response.getOutputStream();
				        baos.writeTo(os);
				        os.flush();
						  } catch (Exception e1) {
						        e1.printStackTrace();
						    }
					
						    File file = new File(temperoryFilePath+"\\"+fileName_excel);
							if(file.exists()){
							boolean success = file.delete();
							logger.info("File Deleted Successfully"+success +" : "+temperoryFilePath+"\\"+fileName_excel);
							}
					}else {
						logger.info("**** ### ERROR. No report generated. ");
					}
					
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Execution result Report service
				@RequestMapping(value = "/executionResult/getExecutionDownloadReport", method = RequestMethod.GET)
				public void getExecutionDownloadReport(HttpServletRequest request,
						HttpServletResponse response) throws IOException {
					Map<String, Object> modelMap = new HashMap<String, Object>(4);

					try {
						ExecutionResult executionResult  =  null;
						
						logger.info("### Getting execution Result Id :" + request.getParameter("exeResId"));
						if ((StringUtils.isNotBlank(request
								.getParameter("exeResId")))
								|| (StringUtils.isNotEmpty(request
										.getParameter("exeResId")))) {
							executionResult = new ExecutionResult();
							String exeResultId_str = request.getParameter("exeResId");
							executionResult = executionResultService.getExecutionResultById(Integer.parseInt(exeResultId_str));
							logger.info("executionResult is :"
									+ executionResult.getExecutionResultId());
							logger.info("Execution result is :"
									+ executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName()+ " : "
									+ executionResult.getTestSuiteExecution().getTestSuite().getSuiteName() + " : "
									+ executionResult.getStartTime() + " : "
									+ executionResult.getEndTime());
							logger.info("Execution report List");
						}
												
						String downloadFormat = request.getParameter("downloadFormat");
						if(downloadFormat != null || downloadFormat == ""){
							if(downloadFormat.equalsIgnoreCase("pdf")){
								logger.info("**** ### Going to Create PDF doc Report");
								String fileName_pdf = executionResult.getExecutionResultId()+".pdf";
								logger.info("FileName is : "+fileName_pdf);
								
								final ServletContext servletContext = request.getSession().getServletContext();
							    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
							    final String temperoryFilePath = tempDirectory.getAbsolutePath();
								
							    logger.info("Temp Directory is : "+tempDirectory);
							    logger.info("Temperory FilePath: is : "+temperoryFilePath);
							    
							 // set headers for the response
								    response.setContentType("application/pdf");
								    response.setHeader("Content-disposition", "attachment; filename="+fileName_pdf);
								    try {
							CreatePDF.createExecutionResultPDF(temperoryFilePath+"\\"+fileName_pdf,executionResult);
							 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						        baos = convertPDFToByteArrayOutputStream(temperoryFilePath+"\\"+fileName_pdf);
						        OutputStream os = response.getOutputStream();
						        baos.writeTo(os);
						        os.flush();
								  } catch (Exception e1) {
								        e1.printStackTrace();
								    }
							
								    File file = new File(temperoryFilePath+"\\"+fileName_pdf);
									if(file.exists()){
									boolean success = file.delete();
									logger.info("File Deleted Successfully"+success +" : "+temperoryFilePath+"\\"+fileName_pdf);
									}
							
							
							}
							else if (downloadFormat.equalsIgnoreCase("excel")) {
								logger.info("**** ### Going to Create Excel Sheet execution Report");
								String fileName_excel = executionResult.getExecutionResultId()+".xlsx";
								logger.info("File Name is : "+fileName_excel);
								
								final ServletContext servletContext = request.getSession().getServletContext();
							    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
							    final String temperoryFilePath = tempDirectory.getAbsolutePath();
								
							    logger.info("Temp Directory is : "+tempDirectory);
							    logger.info("Temperory FilePath: is : "+temperoryFilePath);
							    
							 // set headers for the response
								    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
								    response.setHeader("Content-disposition", "attachment; filename="+fileName_excel);
								    try {
							CreateExecutionExcel createExeExcel = new CreateExecutionExcel();
							createExeExcel.createExecutionExcel(temperoryFilePath+"\\"+fileName_excel,executionResult);
							
							 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						        baos = convertPDFToByteArrayOutputStream(temperoryFilePath+"\\"+fileName_excel);
						        OutputStream os = response.getOutputStream();
						        baos.writeTo(os);
						        os.flush();
								  } catch (Exception e1) {
								        e1.printStackTrace();
								    }
							
								    File file = new File(temperoryFilePath+"\\"+fileName_excel);
									if(file.exists()){
									boolean success = file.delete();
									logger.info("File Deleted Successfully"+success +" : "+temperoryFilePath+"\\"+fileName_excel);
									}
							}else {
								logger.info("**** ### ERROR. No report generated. ");
							}
							
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

	// JSon Construction
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
												
												
												/* Changes done for execution result exception in Services STARTS HERE */
												
												
												Set<ExecutionResultException> executionResultExceptions = executionResultDetail.getExecutionResultExceptions();
												/*if(executionResultExceptions)*/
												JsonConfig jsonExecutionResultExceptionConfig = new JsonConfig();
												if(executionResultExceptions == null || executionResultExceptions.equals(null)){
												jsonExecutionResultExceptionConfig
												.registerJsonBeanProcessor(
														ExecutionResultException.class,
														new JsonBeanProcessor() {
															
															@Override
															public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
																if (!(bean instanceof ExecutionResultException)) {
																	return new JSONObject(true);
																}
																return new JSONObject()
																.element(
																		"executionResultExceptionId", "")
																.element(
																		"executionResultExceptionMethodName", "")
																.element(
																		"executionResultExceptionFilePath","") 
																.element("executionResultExceptionFileContent","");
															}
														});
												}
												else if(executionResultExceptions != null){
													//logger.info("In Json ExeRes Exception Name is : "+executionResultExceptions.size());
													/*logger.info("In Json ExeRes Exception val is : "+executionResultException.getExecutionResultExceptionFilePath());*/
												jsonExecutionResultExceptionConfig
														.registerJsonBeanProcessor(
																ExecutionResultException.class,
																new JsonBeanProcessor() {

																	public JSONObject processBean(
																			Object bean,
																			JsonConfig jsonConfig) {
																		if (!(bean instanceof ExecutionResultException)) {
																			return new JSONObject(
																					true);
																		}
																		
																		ExecutionResultException executionResultException = (ExecutionResultException) bean;
																		
																		return new JSONObject()
																				.element(
																						"executionResultExceptionId",
																						executionResultException
																								.getExecutionResultExceptionId())
																				.element(
																						"executionResultExceptionMethodName",
																						executionResultException
																								.getExecutionResultExceptionMethodName())
																				.element(
																						"executionResultExceptionFilePath",
																						executionResultException
																								.getExecutionResultExceptionFilePath()) 
																				.element("executionResultExceptionFileContent",readFileContenttoString(executionResultException
																								.getExecutionResultExceptionFilePath()) );
																	}
																});
												}
												//logger.info("Exception Json is : "+jsonExecutionResultExceptionConfig.toString());
												
												/* Changes done for execution result exception in Services STARTS HERE */

												JSON jsonExecutionResultException = JSONSerializer
														.toJSON(executionResultExceptions,
																jsonExecutionResultExceptionConfig);

												SimpleDateFormat formatter = new SimpleDateFormat(
														"MM/dd/yyyy HH:mm:ss");

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
														.element(
																"signature",
																executionResultDetail
																		.getSignature())
														.element("startedAt",
																startedAt)
														.element("finishedAt",
																finishedAt)
														.element(
																"status",
																executionResultDetail
																		.getStatus())
														.element("executionResultException", jsonExecutionResultException)
														.element(
																"executionMethodParameters",
																jsonExecutionMethodParameter);

											}
										});

						JSON jsonExecutionResultDetail = JSONSerializer.toJSON(
								executionResultDetails,
								jsonExecutionResultDetailConfig);

						SimpleDateFormat formatter = new SimpleDateFormat(
								"MM/dd/yyyy HH:mm:ss");
						String startTime = formatter.format(executionResult
								.getStartTime());
						String endTime = formatter.format(executionResult
								.getEndTime());

						return new JSONObject()
								.element("executionResultId",
										executionResult.getExecutionResultId())
								.element(
										"testSuiteExecId",
										executionResult.getTestSuiteExecution()
												.getTestSuiteExecId())
								.element("testExecutionName",
										executionResult.getTestExecutionName())
								.element("startTime", startTime)
								.element("endTime", endTime)
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
								.element(
										"browser",
										executionResult.getTestSuiteExecution()
												.getBrowser())
								.element(
										"browserVersion",
										executionResult.getTestSuiteExecution()
												.getBrowserVersion())
								.element(
										"operatingSystem",
										executionResult.getTestSuiteExecution()
												.getOperatingSystem())
								.element(
										"testSuiteExecName",
										executionResult.getTestSuiteExecution()
												.getTestSuite().getSuiteName())
								.element("executionStatus",executionResult.getExecutionStatus())

								.element(
										"projectName",
										executionResult.getTestSuiteExecution()
												.getTestSuite().getProject()
												.getProjectName())

								.element("executionResultDetails",
										jsonExecutionResultDetail);
						
						
						// .element("executionMethodParameters",jsonExecutionMethodParameter);
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}
	
	// JSon Construction
	private Map<String, Object> getModelMapExecMParamList(
			List<ExecutionMethodParameter> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(ExecutionMethodParameter.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof ExecutionMethodParameter)) {
							return new JSONObject(true);
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
	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[4096];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}
	
	/* Changes done for execution result exception in Services STARTS HERE */
	
	private String readFileContenttoString(String filePath) {
		String fileContent = null;
		int filelength; FileInputStream fStream = null;
		try{
		 fStream = new FileInputStream(new File(filePath));
		
		
		while ((filelength = fStream.read()) != -1) {
			char c = (char)filelength;
			fileContent = fileContent + c;
			
		}
		fStream.close();
		}catch(FileNotFoundException fnfe){fnfe.printStackTrace();}
		catch(IOException ioe){ioe.printStackTrace();}
		catch (Exception e) {e.printStackTrace();}
		
		
		return fileContent;
	}
	
	/* Changes done for execution result exception in Services STARTS HERE */
	
}
