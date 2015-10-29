package com.bpa.qaproduct.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.JavaMailSender;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.ProjectService;
import com.bpa.qaproduct.service.TestSuiteService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TestSuiteService testSuiteService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	protected final Log logger = LogFactory.getLog(ProjectController.class);

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private MailService mailService;

	
	// List Service

	@RequestMapping(value = "/project/viewProjectList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewProjectList(HttpServletRequest request) {

		try {

			// Constructing User Search Object
			Project project = new Project();

			String searchProjectName = request
					.getParameter("searchProjectName");
			if (searchProjectName != null
					&& searchProjectName.isEmpty() == false) {
				project.setProjectName(searchProjectName);
			}
			User user = new User();
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				// id_value = request.getParameter("projectId");

				user = userService.getUserById((Integer.parseInt(id_value)));
				project.setOrganization(user.getOrganization());
			}

			int totalResults = projectService.getProjectFilterCount(project);

			List<Project> list = projectService.getAllProjectList(project);

			logger.info("Returned list size" + list.size());

			return getModelMapProjectList(list, totalResults);
		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	// Pagination Service

	@RequestMapping(value = "/project/viewProjectListPagination", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewPaginationService(HttpServletRequest request) {
		int start = 0;
		int limit = 10;

		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));
		try {
			Project project = new Project();

			User user = new User();
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				// id_value = request.getParameter("projectId");

				user = userService.getUserById((Integer.parseInt(id_value)));
				project.setOrganization(user.getOrganization());
			}

			if ((StringUtils.isNotBlank(request.getParameter("projectName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("projectName")))) {
				project.setProjectName(request.getParameter("projectName"));
				logger.info("Search project JData table Project Name is :"
						+ request.getParameter("projectName"));
			} else {
				project.setProjectName(null);
			}

			if ((StringUtils.isNotBlank(request.getParameter("projectPath")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("projectPath")))) {
				project.setProjectJarPath(request.getParameter("projectPath"));
				logger.info("Search project JData table project Path is :"
						+ request.getParameter("projectPath"));
			} else {
				project.setProjectJarPath(null);
			}

			if ((StringUtils.isNotBlank(request.getParameter("createdOn")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("createdOn")))) {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date createdOnDate = (Date) formatter.parse(request
						.getParameter("createdOn"));

				logger.info("Search project JData table Created Date is :"
						+ createdOnDate);
				project.setCreatedOn(createdOnDate);
				logger.info("Search project JData table created On is :"
						+ request.getParameter("createdOn"));
				
				
				
				
				
				
			} else {
				project.setCreatedOn(null);
			}

			int totalResults = projectService.getProjectFilterCount(project);
			List<Project> list = projectService.getPaginationList(project,
					start, limit);
			return getModelMapProjectListPagination(list, totalResults);
		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	// Save Service
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/project/saveProject", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveProject(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Project project = new Project();
			
			String id_value = "";
			boolean isEditing = true;

			
			User user = new User();
			
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById(Integer.parseInt(id_value));
				project.setOrganization(user.getOrganization());
				logger.info("*********" + project.getOrganization());
			}
			
			
			if ((StringUtils.isNotBlank(request.getParameter("projectId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("projectId")))) {
				id_value = request.getParameter("projectId");
				project = projectService.getProjectnById(Integer
						.parseInt(id_value));
				isEditing = false;
				// project.setUpdatedBy(1); // need to change after getting user
				// from session
				project.setUpdatedOn(todayDate);
				project.setStartDate(todayDate);
				project.setEndDate(todayDate);

			} 
			else 
			{
				// project.setCreatedBy(1); // need to change after getting user
				// from session
				project.setCreatedOn(todayDate);
				// project.setUpdatedBy(1);
				project.setUpdatedOn(todayDate);
				project.setStartDate(todayDate);
				project.setEndDate(todayDate);
			}
				String projectName = request.getParameter("projectName");
				
				if (projectName != null) {
					
					Project searchProject = new Project();
					searchProject.setProjectName(projectName);
					searchProject.setOrganization(user.getOrganization());
					
					searchProject = projectService.getProjectBySearchParam(searchProject);
					
					if (searchProject != null) 
					{
						logger.info("***************** Project is already Exists **************:");
						modelMap.put("success", false);
						modelMap.put("message", "Project is already exists");
						return modelMap;
					} 
					
				}

						
			if (projectName != null) {
				project.setProjectName(projectName);
			}

			
			String notes = request.getParameter("notes");
			if (notes != null) {
				project.setNotes(notes);
			}

			String projectType = request.getParameter("projectType");
			String projectJarName = "";
			if (projectType != null) {
				project.setProjectType(projectType);

				if (projectType.equalsIgnoreCase("java")
						|| projectType == "java") {
					projectJarName = request.getParameter("projectJarName");
					/* String staticJarPath = projectJarName+".jar"; */
					project.setProjectJarPath(projectJarName);
				} else if (projectType.equalsIgnoreCase("html")
						|| projectType == "html") {
					project.setProjectJarPath("");
				}
			}

			String isActive = request.getParameter("isActive");
			if (isActive != null) {
				project.setIsActive(project.setIsActiveString(isActive));
			}

			String createdBy = request.getParameter("createdBy");
			if (createdBy != null) {
				project.setCreatedBy(Integer.parseInt(createdBy));
			}

			String updatedBy = request.getParameter("updatedBy");
			if (updatedBy != null) {
				project.setUpdatedBy(Integer.parseInt(updatedBy));
			}

			project = projectService.saveProjectWithReturn(project);
			
			
			logger.info("Getting diff getProjectId" + project.getProjectId());
			
			
			EmailJob emailJob = new EmailJob();
			emailJob.setEmailType("PROJECT CREATION");
			String email_value = "";
			Properties properties_mail = new Properties();
			
				
		
			
	
		    
		    logger.info("Email Service uploaded");
			if (project != null && project.getProjectId() != null && isEditing) {
				Properties properties = new Properties();
				InputStream iStream = null;
				String propFileName = "properties/projectJar.properties";
				
		
				

				try {
					InputStream stream = getClass().getClassLoader()
							.getResourceAsStream(propFileName);
							logger.info("prpperty file"
							+ getClass().getClassLoader().getResourceAsStream(
							propFileName));
							properties.load(stream);
					
					InputStream iStream_mail = null;
					String propFileName_mail = "/properties/mail.properties";
					InputStream stream_mail = getClass().getClassLoader()
							.getResourceAsStream(propFileName_mail);
					properties_mail.load(stream_mail);
					
					
				
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("from", properties_mail
							.getProperty("javaMailSender.username"));
					map.put("to", user.getUserEmail());
					map.put("subject", "ATOC - Project Jar Info");

					map.put("user", user.getFirstName());
					map.put("projectName", project.getProjectName());
					map.put("user", user.getFirstName());
					map.put("projectjar", project.getProjectJarPath());
					map.put("ftpurl", properties.getProperty("ftp_Location"));
					map.put("portno", properties.getProperty("port_No"));
					map.put("templateName", "ProjectJarDetails.vm");
					map.put("username", properties.getProperty("Username"));
					map.put("password", properties.getProperty("Password"));
					map.put("content",
							"By Clicking download link your project will downloads Succesfully. Thank You");
					
					

						byte[] byteArray = null;
						byteArray = convertObjectToByteArray(map);
						emailJob.setJobDataByteArray(byteArray);
						userService.saveOrUpdateEmailJob(emailJob);
						
					
				} catch (Exception fileNotEx) {
					fileNotEx.printStackTrace();
				}

			}
			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
	}

	@RequestMapping(value = "/project/getProjectInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> userId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		
		String projectId_Str = request.getParameter("projectId");
		Project project = new Project();
		try {
			if (projectId_Str != null) {
				project = projectService.getProjectnById(Integer
						.parseInt(projectId_Str));
				
			}
			else
			{
				return getModelMapError("Failed to Load Data");
			}
			
			return getModelMapProjectInfo(project);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	
	}
	
	// JSon Construction
		private Map<String, Object> getModelMapProjectInfo(Project project) {

			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonBeanProcessor(Project.class,
					new JsonBeanProcessor() {
						public JSONObject processBean(Object bean,
								JsonConfig jsonConfig) {
							if (!(bean instanceof Project)) {
								return new JSONObject(true);
							}
							Project project = (Project) bean;
							SimpleDateFormat importDateFormat = new SimpleDateFormat(
									"MM/dd/yyyy");

							String startDateString = "";
							if (project.getStartDate() != null) {
								startDateString = importDateFormat.format(project
										.getStartDate());

							}
							String endDateString = "";
							if (project.getEndDate() != null) {
								endDateString = importDateFormat.format(project
										.getEndDate());
							}
							
							String createdOnString = "";
							if (project.getCreatedOn() != null) {
								createdOnString = importDateFormat.format(project
										.getCreatedOn());
							}
							String updatedOnString = "";
							if (project.getUpdatedOn() != null) {
								updatedOnString = importDateFormat.format(project
										.getUpdatedOn());
							}
							
							return new JSONObject()
							.element("projectId", project.getProjectId())
							.element("projectName",
									project.getProjectName())
							.element("notes", project.getNotes())
							.element("startDate", startDateString)
							.element("endDate", endDateString)
							.element(
									"isActive",
									project.getIsActiveString(project
											.getIsActive()))
							.element("createdBy", project.getCreatedBy())
							.element("createdOn", createdOnString)
							.element("updatedBy", project.getUpdatedBy())
							.element("updatedOn", updatedOnString)
							.element("projectType",
									project.getProjectType())
							.element("projectPath",
									project.getProjectJarPath())
							.element(
									"organization",
									project.getOrganization()
											.getOrganizationName())
							

					;

						}
					});

			JSON json = JSONSerializer.toJSON(project, jsonConfig);

			/*---*/
			modelMap.put("data", json);
			modelMap.put("success", true);

			return modelMap;
		}
	
	// Delete Service
	@RequestMapping(value = "/project/deleteProject", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteProject(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String projectId_Str = request.getParameter("projectId");
		try {
			if (projectId_Str != null) {
				Project project = projectService.getProjectnById(Integer
						.parseInt(projectId_Str));
				projectService.removeProject(project);
			}
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

	// JSon Construction
	private Map<String, Object> getModelMapProjectList(List<Project> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Project.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Project)) {
							return new JSONObject(true);
						}
						Project project = (Project) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String startDateString = "";
						if (project.getStartDate() != null) {
							startDateString = importDateFormat.format(project
									.getStartDate());

						}
						String endDateString = "";
						if (project.getEndDate() != null) {
							endDateString = importDateFormat.format(project
									.getEndDate());
						}
						
						
						
						/* TEST SUITE DETAILS MAPPING TO JSON */
						JsonConfig jsonTestSuiteConfig = new JsonConfig();
						Set<TestSuite> testSuiteSet = new HashSet<>();
						
						try
						{
							if (project.getTestSuites() != null)
							{
								testSuiteSet = new HashSet(project.getTestSuites());;
								
								jsonTestSuiteConfig.registerJsonBeanProcessor(
										TestSuite.class, new JsonBeanProcessor() {

											public JSONObject processBean(Object bean,
													JsonConfig jsonConfig) {
												if (!(bean instanceof TestSuite)) {
													return new JSONObject(true);
												}
												TestSuite testSuite = (TestSuite) bean;
												return new JSONObject()
														.element(
																"testSuiteId",
																testSuite
																		.getTestSuiteId())
														.element(
																"suiteName",
																testSuite
																		.getSuiteName())
														.element(
																"testSuiteUrl",
																testSuite
																		.getTestSuiteUrl())
														.element(
																"testSuiteXmlPathInJar",
																testSuite
																		.getTestSuiteXmlPathInJar());
											}
										});
							}
						}
						catch(Exception ex)
						{
							logger.info("Exception:"+ex.getMessage());
						}

						JSON jsontestSuite = JSONSerializer.toJSON(
								testSuiteSet, jsonTestSuiteConfig);

						return new JSONObject()
								.element("projectId", project.getProjectId())
								.element("projectName",
										project.getProjectName())
								.element("notes", project.getNotes())
								.element("startDate", startDateString)
								.element("endDate", endDateString)
								.element(
										"isActive",
										project.getIsActiveString(project
												.getIsActive()))
								.element("createdBy", project.getCreatedBy())
								.element("createdOn", project.getCreatedOn())
								.element("updatedBy", project.getUpdatedBy())
								.element("updatedOn", project.getUpdatedOn())
								.element("projectType",
										project.getProjectType())
								.element("projectPath",
										project.getProjectJarPath())
								.element("testSuiteSet", jsontestSuite)

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
	 * Common json methods
	 */

	// JSon Construction for pagination
	private Map<String, Object> getModelMapProjectListPagination(
			List<Project> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Project.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Project)) {
							return new JSONObject(true);
						}
						Project project = (Project) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String startDateString = "";
						if (project.getStartDate() != null) {
							startDateString = importDateFormat.format(project
									.getStartDate());

						}
						String endDateString = "";
						if (project.getEndDate() != null) {
							endDateString = importDateFormat.format(project
									.getEndDate());
						}

						String createdOnString = "";
						if (project.getCreatedOn() != null) {
							createdOnString = importDateFormat.format(project
									.getCreatedOn());
						}
						String updatedOnString = "";
						if (project.getUpdatedOn() != null) {
							updatedOnString = importDateFormat.format(project
									.getUpdatedOn());
						}

						/* TEST SUITE DETAILS MAPPING TO JSON */
						
						
						JsonConfig jsonTestSuiteConfig = new JsonConfig();
						Set<TestSuite> testSuiteSet = new HashSet<>();
						
						try
						{
							if (project.getTestSuites() != null)
							{
								testSuiteSet = new HashSet(project.getTestSuites());;
								
								jsonTestSuiteConfig.registerJsonBeanProcessor(
										TestSuite.class, new JsonBeanProcessor() {

											public JSONObject processBean(Object bean,
													JsonConfig jsonConfig) {
												if (!(bean instanceof TestSuite)) {
													return new JSONObject(true);
												}
												TestSuite testSuite = (TestSuite) bean;
												return new JSONObject()
														.element(
																"testSuiteId",
																testSuite
																		.getTestSuiteId())
														.element(
																"suiteName",
																testSuite
																		.getSuiteName())
														.element(
																"testSuiteUrl",
																testSuite
																		.getTestSuiteUrl())
														.element(
																"testSuiteXmlPathInJar",
																testSuite
																		.getTestSuiteXmlPathInJar());
											}
										});
							}
						}
						catch(Exception ex)
						{
							logger.info("Exception:"+ex.getMessage());
						}
						
						

						JSON jsontestSuite = JSONSerializer.toJSON(
								testSuiteSet, jsonTestSuiteConfig);

						return new JSONObject()
								.element("projectId", project.getProjectId())
								.element("projectName",
										project.getProjectName())
								.element("notes", project.getNotes())
								.element("startDate", startDateString)
								.element("endDate", endDateString)
								.element(
										"isActive",
										project.getIsActiveString(project
												.getIsActive()))
								.element("createdBy", project.getCreatedBy())
								.element("createdOn", createdOnString)
								.element("updatedBy", project.getUpdatedBy())
								.element("updatedOn", updatedOnString)
								.element("projectType",
										project.getProjectType())
								.element("projectPath",
										project.getProjectJarPath())
								.element(
										"organization",
										project.getOrganization()
												.getOrganizationName())
								.element("testSuiteSet", jsontestSuite)
								

						;
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
	private static byte[] convertObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
}

}
