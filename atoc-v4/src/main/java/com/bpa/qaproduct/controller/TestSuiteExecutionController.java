package com.bpa.qaproduct.controller;

import static org.quartz.JobBuilder.newJob;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.SchedulerExecution;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteDetail;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.job.JobScheduler;
import com.bpa.qaproduct.service.AmazonImagesService;
import com.bpa.qaproduct.service.ExecutionService;
import com.bpa.qaproduct.service.NotificationSaveService;
import com.bpa.qaproduct.service.ProjectService;
import com.bpa.qaproduct.service.SchedulerExecutionService;
import com.bpa.qaproduct.service.TestSuiteDetailService;
import com.bpa.qaproduct.service.TestSuiteExecutionService;
import com.bpa.qaproduct.service.TestSuiteService;
import com.bpa.qaproduct.service.UserService;

@Controller("TestSuiteExecutionController")
@Component
public class TestSuiteExecutionController {

	@Autowired
	private TestSuiteExecutionService testSuiteExecutionService;

	@Autowired
	private AmazonImagesService amazonImagesServices;
	
	@Autowired
	private TestSuiteDetailService testSuiteDetailService;

	@Autowired
	private TestSuiteService testSuiteService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ExecutionService executionService;

	@Autowired
	private SchedulerExecutionService schedulerExecutionService;

	@Autowired
	private AmazonImagesService amazonImagesService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationSaveService notificationService;

	/*
	 * @Autowired private Scheduler scheduler;
	 */

	protected final Log logger = LogFactory
			.getLog(TestSuiteExecutionController.class);

	AmazonImages amazonImages = null;

	// List Service

	@RequestMapping(value = "/testSuiteExecution/viewtestSuiteExecutionList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewtestSuiteExecutionList(HttpServletRequest request) {

		try {

			// Constructing CustomerRegistration Search Object
			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();

			TestSuite testSuite = new TestSuite();
			logger.info("Execution page");

			String executionType = request.getParameter("executionType");
			if (executionType != null && executionType.isEmpty() == false) {
				testSuiteExecution.setExecutionType(executionType);
			}

			String testSuiteId_str = request.getParameter("testSuiteId");
			if (testSuiteId_str != null) {

				testSuite = testSuiteService.getTestSuiteById((Integer
						.parseInt(testSuiteId_str)));
				testSuiteExecution.setTestSuite(testSuite);
			}
			testSuiteExecution.setExecutionStatus("COMPLETED");
			int totalResults = testSuiteExecutionService
					.getTestSuiteExecutionFilterCount(testSuiteExecution);
			List<TestSuiteExecution> list = testSuiteExecutionService
					.getAllTestSuiteExecutionList(testSuiteExecution);

			logger.info("Returned list size" + list.size());

			return getModelMaptestSuiteExecutionList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	//Amazon values getting dynamicaly
	@RequestMapping(value = "/testSuiteExecution/viewAmazonList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewAmazonList(HttpServletRequest request) {

		try {
			
			AmazonImages amazonImages = new AmazonImages();

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("amazonImagesId")))
					|| (StringUtils.isNotEmpty(request.getParameter("amazonImagesId")))) {
				id_value = request.getParameter("amazonImagesId");
				// id_value = request.getParameter("projectId");

				//amazonImages = amazonImagesServices.getAmazonImgById((Integer.parseInt(id_value)));
				logger.info("Amazon Images ID*********"+id_value);
			}

			int totalResults = amazonImagesServices.getAmazonFilterCount(amazonImages);
			List<AmazonImages> list = amazonImagesServices.getAllAmazonImgList(amazonImages);

			logger.info("Returned list size" + list.size());

			return getModelMapAmazonImgList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}
	
	
	
	// Pagination Service

	@RequestMapping(value = "/testSuiteExecution/viewtestSuiteExecutionPaginationList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewtestSuiteExecutionPaginationList(
			HttpServletRequest request) {

		try {

			int start = 0;
			int limit = 10;

			start = Integer.parseInt(request.getParameter("iDisplayStart"));
			limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			logger.info("display length");

			String id_value = "";
			id_value = request.getParameter("userId");
			logger.info("Getting User Object based on Id :" + id_value);
			// Constructing CustomerRegistration Search Object
			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();

			logger.info("Execution page");

			if ((StringUtils.isNotBlank(request
					.getParameter("testSuiteExecName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiteExecName")))) {
				testSuiteExecution.setTestSuiteExecName(request
						.getParameter("testSuiteExecName"));
				logger.info("Search Testsuite name is :"
						+ request.getParameter("testSuiteExecName"));
			} 
			/*else {
				testSuiteExecution.setTestSuiteExecName(null);
			}*/

			if ((StringUtils.isNotBlank(request.getParameter("browser")))
					|| (StringUtils.isNotEmpty(request.getParameter("browser")))) {
				testSuiteExecution.setBrowser(request.getParameter("browser"));
				logger.info("Search browser based data is :"
						+ request.getParameter("browser"));
			} 
			/*else {
				testSuiteExecution.setBrowser(null);
			}*/

			if ((StringUtils
					.isNotBlank(request.getParameter("operatingSystem")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("operatingSystem")))) {
				testSuiteExecution.setOperatingSystem(request
						.getParameter("operatingSystem"));
				logger.info("Search browser based data is :"
						+ request.getParameter("operatingSystem"));
			} 
			/*else {
				testSuiteExecution.setOperatingSystem(null);
			}*/

			if ((StringUtils.isNotBlank(request.getParameter("execCompleteOn")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("execCompleteOn")))) {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date execCompleteOn = (Date) formatter.parse(request
						.getParameter("execCompleteOn"));

				logger.info("Search project JData table Created Date is :"
						+ execCompleteOn);
				testSuiteExecution.setExecCompleteOn(execCompleteOn);
				logger.info("Search project JData table executed On is :"
						+ request.getParameter("execCompleteOn"));
			} 
			/*else {
				testSuiteExecution.setExecCompleteOn(null);
			}*/

			String executionType = request.getParameter("executionType");
			if (executionType != null && executionType.isEmpty() == false) {
				testSuiteExecution.setExecutionType(executionType);
			}
			
			testSuiteExecution.setExecutionStatus("COMPLETED");
			
			TestSuite testSuite = new TestSuite();
			String testSuiteId_str = request.getParameter("testSuiteId");
			if (testSuiteId_str != null) {

				testSuite = testSuiteService.getTestSuiteById((Integer
						.parseInt(testSuiteId_str)));
				testSuiteExecution.setTestSuite(testSuite);
			}

			int totalResults = testSuiteExecutionService
					.getTestSuiteExecutionFilterCount(testSuiteExecution);
			List<TestSuiteExecution> list = testSuiteExecutionService
					.getTestSuiteExecutionListPagination(testSuiteExecution,
							start, limit);

			logger.info("Returned list size" + list.size());

			return getModelMaptestSuiteExecutionListPagination(list,
					totalResults);

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/testSuiteExecution/saveTestSuiteExecution", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveTestSuiteExecution(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			String id_value = "";

			if ((StringUtils
					.isNotBlank(request.getParameter("testSuiteExecId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiteExecId")))) {
				id_value = request.getParameter("testSuiteExecId");
				testSuiteExecution = testSuiteExecutionService
						.getTestSuiteExecutionById((Integer.parseInt(id_value)));
				testSuiteExecution.setCreatedOn(todayDate);
				testSuiteExecution.setUpdatedOn(todayDate);
			} else {
				testSuiteExecution.setCreatedOn(todayDate);
				testSuiteExecution.setUpdatedOn(todayDate);
			}

			testSuiteExecution.setScheduledOn(todayDate);

			testSuiteExecution.setExecCompleteOn(todayDate);

			String executionType = request.getParameter("executionType");
			testSuiteExecution.setExecutionType(executionType);

			String priority = request.getParameter("priority");
			testSuiteExecution.setPriority(priority);

			String logLevel = request.getParameter("logLevel");
			testSuiteExecution.setLogLevel(logLevel);

			String notificationEmail = request
					.getParameter("notificationEmail");
			testSuiteExecution.setNotificationEmail(notificationEmail);

			String fatalErrorNotificationMail = request
					.getParameter("fatalErrorNotificationMail");
			testSuiteExecution
					.setFatalErrorNotificationMail(fatalErrorNotificationMail);

			String status = request.getParameter("status");
			testSuiteExecution.setStatus(status);

			String isActive = request.getParameter("isActive");
			testSuiteExecution.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			testSuiteExecution.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			testSuiteExecution.setUpdatedBy(Integer.parseInt(updatedBy));

			String operatingSystem = request.getParameter("operatingSystem");
			testSuiteExecution.setOperatingSystem(operatingSystem);

			String browser = request.getParameter("browser");
			testSuiteExecution.setBrowser(browser);

			String browserVersion = request.getParameter("browserVersion");
			testSuiteExecution.setBrowserVersion(browserVersion);

			/*
			 * String testSuiteDetailId_str = request
			 * .getParameter("testSuiteDetail"); TestSuiteDetail testSuiteDetail
			 * = new TestSuiteDetail(); testSuiteDetail = testSuiteDetailService
			 * .getTestSuiteDetailById((Integer
			 * .parseInt(testSuiteDetailId_str)));
			 * testSuiteExecution.setTestSuiteDetail(testSuiteDetail);
			 */

			testSuiteExecutionService
					.saveTestSuiteExecution(testSuiteExecution);

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

	@RequestMapping(value = "/testSuiteExecution/getTestSuiteExecutionInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> testSuiteExecId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>(2);

		String testSuiteExecId_str = request.getParameter("testSuiteExecId");
		TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
		try {
			if (testSuiteExecId_str != null) {
				testSuiteExecution = testSuiteExecutionService
						.getTestSuiteExecutionById(Integer
								.parseInt(testSuiteExecId_str));
			} else {
				return getModelMapError("Failed to Load Data");
			}
			return getModelMapTestSuiteExecutionInfo(testSuiteExecution);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in loading data";
			map.put("success", false);
			map.put("message", msg);
			return map;
		}
	}

	// JSon Construction
	private Map<String, Object> getModelMapTestSuiteExecutionInfo(
			TestSuiteExecution testSuiteExecution) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuiteExecution.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuiteExecution)) {
							return new JSONObject(true);
						}

						TestSuiteExecution testSuiteExecution = (TestSuiteExecution) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");
						String sheduledOnString = "";
						if (testSuiteExecution.getScheduledOn() != null) {
							sheduledOnString = importDateFormat
									.format(testSuiteExecution.getScheduledOn());

						}
						String completedOnString = "";
						if (testSuiteExecution.getExecCompleteOn() != null) {
							completedOnString = importDateFormat
									.format(testSuiteExecution
											.getExecCompleteOn());
						}

						String createdOnString = "";
						if (testSuiteExecution.getCreatedOn() != null) {
							createdOnString = importDateFormat
									.format(testSuiteExecution.getCreatedOn());
						}
						String updatedOnString = "";
						if (testSuiteExecution.getUpdatedOn() != null) {
							updatedOnString = importDateFormat
									.format(testSuiteExecution.getUpdatedOn());
						}

						return new JSONObject()

								.element("testSuiteExecId",
										testSuiteExecution.getTestSuiteExecId())
								.element("scheduledOn", sheduledOnString)
								.element("executionType",
										testSuiteExecution.getExecutionType())
								.element("priority",
										testSuiteExecution.getPriority())
								.element("logLevel",
										testSuiteExecution.getLogLevel())
								.element(
										"notificationEmail",
										testSuiteExecution
												.getNotificationEmail())
								.element(
										"fatalErrorNotificationMail",
										testSuiteExecution
												.getFatalErrorNotificationMail())
								.element("status",
										testSuiteExecution.getStatus())
								.element("execCompleteOn", completedOnString)
								.element("createdBy",
										testSuiteExecution.getCreatedBy())
								.element("createdOn", createdOnString)
								.element("updatedBy",
										testSuiteExecution.getUpdatedBy())
								.element("updatedOn", updatedOnString)
								.element("browser",
										testSuiteExecution.getBrowser())
								.element("browserVersion",
										testSuiteExecution.getBrowserVersion())
								.element("operatingSystem",
										testSuiteExecution.getOperatingSystem())
								.element(
										"testSuiteExecName",
										testSuiteExecution
												.getTestSuiteExecName());
					}
				});

		JSON json = JSONSerializer.toJSON(testSuiteExecution, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// Delete Service
	@RequestMapping(value = "/testSuiteExecution/deleteTestSuiteExecution", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteTestSuiteExecution(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int testSuiteExecId = Integer.parseInt(request
				.getParameter("testSuiteExecId"));
		try {
			TestSuiteExecution testSuiteExecution = testSuiteExecutionService
					.getTestSuiteExecutionById(testSuiteExecId);
			testSuiteExecutionService
					.removeTestSuiteExecution(testSuiteExecution);
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
    
	
	@RequestMapping(value = "/testSuite/TestSuiteExecution", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getTestSuiteExecution(HttpServletRequest request) {
		logger.info("Test Suite execution Method Strarted..");
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {
			Project project = null;

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));
			String testSuiteId = request.getParameter("testSuiteId");
			String projectId = request.getParameter("projectId");

			TestSuiteExecution testSuiteExecution = new TestSuiteExecution();
			TestSuite testSuite = new TestSuite();
			if (testSuiteId != null) {

				testSuite = testSuiteService.getTestSuiteById(Integer
						.parseInt(testSuiteId));
			}

			if (projectId != null) {

				project = projectService.getProjectnById(Integer
						.parseInt(projectId));

			}

			String id_value2 = "";

			if ((StringUtils
					.isNotBlank(request.getParameter("testSuiteExecId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("testSuiteExecId")))) {
				id_value2 = request.getParameter("testSuiteExecId");
				testSuiteExecution = testSuiteExecutionService
						.getTestSuiteExecutionById((Integer.parseInt(id_value2)));
				testSuiteExecution.setCreatedOn(todayDate);
				testSuiteExecution.setUpdatedOn(todayDate);
			} else {
				testSuiteExecution.setCreatedOn(todayDate);
				testSuiteExecution.setUpdatedOn(todayDate);
			}

			String operatingSystem = request.getParameter("operatingSystem");
			if (operatingSystem != null) {
				testSuiteExecution.setOperatingSystem(operatingSystem);
			}

			String browser = request.getParameter("browser");
			if (browser != null) {
				testSuiteExecution.setBrowser(browser);
			}

			String browserVersion = request.getParameter("browserVersion");
			if (browserVersion != null) {
				testSuiteExecution.setBrowserVersion(browserVersion);
			}

			if (operatingSystem != null && browser != null
					&& browserVersion != null) {
				amazonImages = new AmazonImages();
				amazonImages.setBrowser(browser);
				amazonImages.setOSName(operatingSystem);
				amazonImages.setBrowserVersion(browserVersion);

				amazonImages = amazonImagesService
						.getAmiDetailsByName(amazonImages);
				if (amazonImages != null) {
					logger.info("Ami Name is :" + amazonImages.getAmiName());
					logger.info("Ami Id is :" + amazonImages.getAmiId());
				}
			}
			String status = request.getParameter("status");
			if (status != null) {
				testSuiteExecution.setStatus(status);
			}

			String notificationEmail = request
					.getParameter("notificationEmail");
			if (notificationEmail != null) {
				testSuiteExecution.setNotificationEmail(notificationEmail);
			}

			String fatalErrorNotificationMail = request
					.getParameter("fatalErrorNotificationMail");
			if (fatalErrorNotificationMail != null) {
				testSuiteExecution
						.setFatalErrorNotificationMail(fatalErrorNotificationMail);
			}

			String priority = request.getParameter("priority");
			if (priority != null) {
				testSuiteExecution.setPriority(priority);
			}

			String logLevel = request.getParameter("logLevel");
			if (logLevel != null) {
				testSuiteExecution.setLogLevel(logLevel);
			}

			String isActive = request.getParameter("isActive");
			if (isActive != null) {
				testSuiteExecution.setIsActive(isActive);
			}

			String createdBy = request.getParameter("createdBy");
			if (createdBy != null) {
				testSuiteExecution.setCreatedBy(Integer.parseInt(createdBy));
			}

			String updatedBy = request.getParameter("updatedBy");
			if (updatedBy != null) {
				testSuiteExecution.setUpdatedBy(Integer.parseInt(updatedBy));
			}

			String testSuiteExecName = request
					.getParameter("testSuiteExecName");
			testSuiteExecution.setTestSuiteExecName(testSuiteExecName);

			String testSuiteId_str = request.getParameter("testSuiteId");
			if (testSuiteId_str != null) {

				testSuite = testSuiteService.getTestSuiteById((Integer
						.parseInt(testSuiteId_str)));
				testSuiteExecution.setTestSuite(testSuite);
			}
			TestSuiteExecution testSuiteExecutionWithReturn = new TestSuiteExecution();
			ExecutionResult executionResultWithReturn = new ExecutionResult();
			String executionType = request.getParameter("executionType");
			if (executionType != null && executionType == "now"
					|| executionType.equalsIgnoreCase("now")) {
				testSuiteExecution.setExecutionType("N");
				testSuiteExecution.setExecutionStatus("PENDING");

				testSuiteExecution.setScheduledOn(new Date());

				testSuiteExecution.setExecCompleteOn(new Date());
                
				testSuiteExecutionWithReturn = testSuiteExecutionService
						.saveTestSuiteExecutionwithReturn(testSuiteExecution);
			//	notificationService.notificationMethod(testSuiteExecutionWithReturn);
				
				//notificationService.notificationMethod();
				/*Code for getting Notification table*/
								
				
				
				
				
				
				/*End of Code*/

				logger.info("***************** Before testSuiteAsyncCall ***********************");
				// testSuiteAsyncCall(testSuiteExecutionWithReturn, testSuite);
				scheduleJobNow(testSuiteExecutionWithReturn, testSuite,
						amazonImages);
				logger.info("executionResultWithReturn Id is :"
						+ executionResultWithReturn.getTestExecutionName());
				logger.info("******************* After testSuiteAsyncCall **********************");

				/* TEST SUITE EXECUTION ENDS HERE */
				logger.info("Insert Method Executed.,Iam @ TestSuiteExecutionController");
			}

			else if (executionType != null && executionType == "scheduleLater"
					|| executionType.equalsIgnoreCase("scheduleLater")) {
				SchedulerExecution scExecution = new SchedulerExecution();

				String scheduleTimeZone = "";

				Calendar calendar_timeZone = Calendar.getInstance();
				TimeZone timeZone = calendar_timeZone.getTimeZone();
				scheduleTimeZone = timeZone.getDisplayName();
				scExecution.setScheduleTimeZone(scheduleTimeZone);

				String scheduleDate = request.getParameter("scheduleDate");
				Date scheduleDate_obj = new Date();
				Date scheduleTime_obj = new Date();

				if (scheduleDate != null) {
					logger.info("scheduleDate is :" + scheduleDate);
					SimpleDateFormat sFormat = new SimpleDateFormat(
							"MM/dd/yyyy");
					String scheduleTime = request.getParameter("scheduleTime");

					scheduleDate_obj = sFormat.parse(scheduleDate + " "
							+ scheduleTime);

					SimpleDateFormat sFormat_Time = new SimpleDateFormat(
							"hh:mm");
					scheduleTime_obj = sFormat_Time.parse(scheduleTime);
					Calendar calendar_time = Calendar.getInstance();
					calendar_time.setTime(scheduleTime_obj);

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(scheduleDate_obj);
					calendar.add(Calendar.HOUR_OF_DAY,
							calendar_time.get(Calendar.HOUR_OF_DAY));
					calendar.add(Calendar.MINUTE,
							calendar_time.get(Calendar.MINUTE));
					calendar.add(Calendar.SECOND, 00);
					scheduleDate_obj = calendar.getTime();

					logger.info("scheduleDate Date & Time  :"
							+ scheduleDate_obj);
					scExecution.setScheduleDate(scheduleDate_obj);
					if (scheduleTime != null) {
						Time time = new Time(scheduleDate_obj.getTime());
						scExecution.setScheduleTime(time);
					}
				}

				String schedulerStatus = request
						.getParameter("schedulerStatus");
				if (schedulerStatus != null) {
					scExecution.setStatus(schedulerStatus);
				}
				testSuiteExecution.setExecutionType("S");
				testSuiteExecution.setExecutionStatus("SHEDULED");
				testSuiteExecution.setScheduledOn(scheduleDate_obj);
				testSuiteExecutionWithReturn = testSuiteExecutionService
						.saveTestSuiteExecutionwithReturn(testSuiteExecution);
				if (testSuiteExecutionWithReturn != null) {
					scExecution
							.setSchedulerTestSuiteExecution(testSuiteExecutionWithReturn);
					schedulerExecutionService
							.saveSchedulerExecution(scExecution);
					logger.info("******** Scheduled Date obj is :"
							+ scExecution.getScheduleDate());
					logger.info("******** Scheduled Time obj is :"
							+ scExecution.getScheduleTimeZone());
					scheduleJob(scExecution, testSuiteExecutionWithReturn,
							testSuite, amazonImages);
				}
			} else {
				testSuiteExecution.setExecutionType("");
			}

			logger.info("Insert Method Executed.,");
			modelMap.put("testSuiteExecId",
					testSuiteExecutionWithReturn.getTestSuiteExecId());
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}

	}

	public JobDataMap getJobDataMap(
			TestSuiteExecution testSuiteExecutionWithReturn,
			TestSuite testSuite, AmazonImages amazonImages) {

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("testSuiteExecutionWithReturn",
				testSuiteExecutionWithReturn);
		jobDataMap.put("testSuite", testSuite);
		jobDataMap.put("amazonImages", amazonImages);
		return jobDataMap;
	}

	
	public void notificationExample()
	{
		logger.info("In Notification Example");
	}
	public void scheduleJob(SchedulerExecution scheduleJobInfo,
			TestSuiteExecution testSuiteExecutionWithReturn,
			TestSuite testSuite, AmazonImages amazonImages)
			throws ParseException {
		try {

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();

			JobDetail job = newJob(JobScheduler.class)
					.withIdentity(
							scheduleJobInfo.getSchedulerTestSuiteExecution()
									.getTestSuiteExecId() + "_Job")
					.usingJobData(
							getJobDataMap(testSuiteExecutionWithReturn,
									testSuite, amazonImages)).build();

			Date startDateTime = null;
			if ((scheduleJobInfo.getScheduleDate() != null)
					&& (scheduleJobInfo.getScheduleTime() != null)) {
				logger.info("******** scheduleJobInfo Date is :"
						+ scheduleJobInfo.getScheduleDate());

				logger.info("******** scheduleJobInfo Date after Parsing is :"
						+ scheduleJobInfo.getScheduleDate());

				startDateTime = scheduleJobInfo.getScheduleDate();
				logger.info("******** scheduleJobInfo Date Obj is :"
						+ startDateTime);
			}

			/*Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(testSuiteExecutionWithReturn.getTestSuiteExecName(), 
							testSuiteExecutionWithReturn.getTestSuiteExecName())
					.startAt(startDateTime).build();*/
			
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(testSuiteExecutionWithReturn.getTestSuiteExecName(), 
							testSuiteExecutionWithReturn.getTestSuiteExecName())
					.startAt(startDateTime).build();
			
			
			scheduler.scheduleJob(job, trigger);
			scheduler.addJob(job, true);

		} catch (SchedulerException e) {
			logger.error("Scheduler Exception : " + e.getMessage());
		}

	}

	public void scheduleJobNow(TestSuiteExecution testSuiteExecutionWithReturn,
			TestSuite testSuite, AmazonImages amazonImages)
			throws ParseException {
		try {

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();

			JobDetail job = newJob(JobScheduler.class)
					.withIdentity(
							testSuiteExecutionWithReturn.getTestSuiteExecId()
									+ "_Job")
					.usingJobData(
							getJobDataMap(testSuiteExecutionWithReturn,
									testSuite, amazonImages)).build();

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(testSuiteExecutionWithReturn.getTestSuiteExecName(), 
							testSuiteExecutionWithReturn.getTestSuiteExecName()).startNow()
					.build();
			scheduler.scheduleJob(job, trigger);
			scheduler.addJob(job, true);

		} catch (SchedulerException e) {
			logger.error("Scheduler Exception : " + e.getMessage());
		}

	}
	
	// JSon Construction
		private Map<String, Object> getModelMapAmazonImgList(
				List<AmazonImages> list, int totalResults) {

			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("total", totalResults);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonBeanProcessor(AmazonImages.class,
					new JsonBeanProcessor() {
						public JSONObject processBean(Object bean,
								JsonConfig jsonConfig) {
							if (!(bean instanceof AmazonImages)) {
								return new JSONObject(true);
							}

							AmazonImages amazonImages = (AmazonImages) bean;
							SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
									"dd-MM-yy:HH:mm:SS");
							SimpleDateFormat importDateFormat = new SimpleDateFormat(
									"dd-MM-yy:HH:mm:SS");

							return new JSONObject()
									.element("amazonImagesId",
											amazonImages.getAmazonImagesId())
									.element("amiName",
											amazonImages.getAmiName())
									.element("amiId",
											amazonImages.getAmiId())
									.element("OSName",
											amazonImages.getOSName())
									.element("browser",
											amazonImages.getBrowser())
									.element("browserVersion",
											amazonImages.getBrowserVersion());
									

						}
					});

			JSON json = JSONSerializer.toJSON(list, jsonConfig);

			/*---*/
			modelMap.put("data", json);
			modelMap.put("success", true);

			return modelMap;
		}

	// JSon Construction
	private Map<String, Object> getModelMaptestSuiteExecutionListPagination(
			List<TestSuiteExecution> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuiteExecution.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuiteExecution)) {
							return new JSONObject(true);
						}

						TestSuiteExecution tSuiteExecution = (TestSuiteExecution) bean;
						SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
								"dd/MM/yy");
						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"dd/MM/yy");

						String execCompletedOnString = "";
						if (tSuiteExecution.getExecCompleteOn() != null) {
							execCompletedOnString = importDateFormat
									.format(tSuiteExecution.getExecCompleteOn());
						}

						return new JSONObject()
								.element("testSuiteExecId",
										tSuiteExecution.getTestSuiteExecId())
								.element("scheduledOn",
										tSuiteExecution.getScheduledOn())
								.element("executionType",
										tSuiteExecution.getExecutionType())
								.element("priority",
										tSuiteExecution.getPriority())
								.element("logLevel",
										tSuiteExecution.getLogLevel())
								.element("notificationEmail",
										tSuiteExecution.getNotificationEmail())
								.element(
										"fatalErrorNotificationMail",
										tSuiteExecution
												.getFatalErrorNotificationMail())
								.element("status", tSuiteExecution.getStatus())
								.element("execCompleteOn",
										execCompletedOnString)
								.element("createdBy",
										tSuiteExecution.getCreatedBy())
								.element("createdOn",
										tSuiteExecution.getCreatedOn())
								.element("updatedBy",
										tSuiteExecution.getUpdatedBy())
								.element("updatedOn",
										tSuiteExecution.getUpdatedOn())
								.element("browser",
										tSuiteExecution.getBrowser())
								.element("browserVersion",
										tSuiteExecution.getBrowserVersion())
								.element("operatingSystem",
										tSuiteExecution.getOperatingSystem())
								.element("testSuiteExecName",
										tSuiteExecution.getTestSuiteExecName());

					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	//
	// JSon Construction
	private Map<String, Object> getModelMaptestSuiteExecutionList(
			List<TestSuiteExecution> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuiteExecution.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof TestSuiteExecution)) {
							return new JSONObject(true);
						}

						TestSuiteExecution tSuiteExecution = (TestSuiteExecution) bean;
						SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
								"dd-MM-yy:HH:mm:SS");
						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"dd-MM-yy:HH:mm:SS");

						String execCompletedOnString = "";
						if (tSuiteExecution.getExecCompleteOn() != null) {
							execCompletedOnString = importDateFormat
									.format(tSuiteExecution.getExecCompleteOn());
						}

						return new JSONObject()
								.element("testSuiteExecId",
										tSuiteExecution.getTestSuiteExecId())
								.element("scheduledOn",
										tSuiteExecution.getScheduledOn())
								.element("executionType",
										tSuiteExecution.getExecutionType())
								.element("priority",
										tSuiteExecution.getPriority())
								.element("logLevel",
										tSuiteExecution.getLogLevel())
								.element("notificationEmail",
										tSuiteExecution.getNotificationEmail())
								.element(
										"fatalErrorNotificationMail",
										tSuiteExecution
												.getFatalErrorNotificationMail())
								.element("status", tSuiteExecution.getStatus())
								.element("execCompleteOn",
										execCompletedOnString)
								.element("createdBy",
										tSuiteExecution.getCreatedBy())
								.element("createdOn",
										tSuiteExecution.getCreatedOn())
								.element("updatedBy",
										tSuiteExecution.getUpdatedBy())
								.element("updatedOn",
										tSuiteExecution.getUpdatedOn())
								.element("browser",
										tSuiteExecution.getBrowser())
								.element("browserVersion",
										tSuiteExecution.getBrowserVersion())
								.element("operatingSystem",
										tSuiteExecution.getOperatingSystem())
								.element("testSuiteExecName",
										tSuiteExecution.getTestSuiteExecName());

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

	private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

	@Async
	public void testSuiteAsyncCall(
			TestSuiteExecution testSuiteExecutionWithReturn, TestSuite testSuite)
			throws InterruptedException {
		/* TEST SUITE EXECUTION STARTS HERE */
		try {

			logger.info("In TestSuiteExecutionThread Before Execution");
			executionService.testSuiteExecutionName(
					testSuiteExecutionWithReturn, testSuite.getProject()
							.getProjectJarPath(), testSuite.getProject()
							.getProjectName(), testSuite.getTestSuiteUrl(),
					amazonImages);
			logger.info("In TestSuiteExecutionThread After Execution");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* TEST SUITE EXECUTION ENDS HERE */

	}

}
