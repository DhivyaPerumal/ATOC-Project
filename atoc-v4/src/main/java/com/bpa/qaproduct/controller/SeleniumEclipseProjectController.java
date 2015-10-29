package com.bpa.qaproduct.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

























import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.SeleniumEclipseProject;
import com.bpa.qaproduct.entity.SeleniumProjectJar;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.SeleniumEclipseProjectService;
import com.bpa.qaproduct.service.UserService;


@Controller
public class SeleniumEclipseProjectController {

	protected final Log logger = LogFactory
			.getLog(SeleniumEclipseProjectController.class);
	public static final int BUFFER = 2048;
	@Autowired
	private SeleniumEclipseProjectService seleniumEclipseProjectService;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	private String uploadDirectory = File.separator + "webapps"
			+ File.separator + "upload" + File.separator;

	private String jarFileRespository = File.separator + "webapps"
			+ File.separator + "upload" + File.separator + "jarfile_repository";
	
	
	@RequestMapping(value = "/jarVersion/viewProjectJars" , method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProjectJarLlist(HttpServletRequest request)
	{
		
		SeleniumEclipseProject searchSeleniumEclipseProject = new SeleniumEclipseProject();
		int start = 0;
		int limit = 10;

		try
		{
			start = Integer.parseInt(request.getParameter("iDisplayStart"));
			limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			
			
			if ((StringUtils.isNotBlank(request.getParameter("projectName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("projectName")))) {
				searchSeleniumEclipseProject.setProjectName(request.getParameter("projectName"));
				logger.info("Search project JData table Project Name is :"
						+ request.getParameter("projectName"));
			} else {
				searchSeleniumEclipseProject.setProjectName(null);
			}

			if ((StringUtils.isNotBlank(request.getParameter("updatedOn")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("updatedOn")))) {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date updatedDate = (Date) formatter.parse(request
						.getParameter("updatedOn"));

				logger.info("Search project JData table Updated Date is :"
						+ updatedDate);
				searchSeleniumEclipseProject.setUpdatedOn(updatedDate);
				logger.info("Search project JData table created On is :"
						+ request.getParameter("createdOn"));
			
				
			} else {
				searchSeleniumEclipseProject.setUpdatedOn(null);
			}

			if ((StringUtils.isNotBlank(request.getParameter("createdOn")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("createdOn")))) {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date createdOnDate = (Date) formatter.parse(request
						.getParameter("createdOn"));

				logger.info("Search project JData table Created Date is :"
						+ createdOnDate);
				searchSeleniumEclipseProject.setCreatedOn(createdOnDate);
				logger.info("Search project JData table created On is :"
						+ request.getParameter("createdOn"));
			
				
			} else {
				searchSeleniumEclipseProject.setCreatedOn(null);
			}
			
			int totalResults = seleniumEclipseProjectService.getSeleniumEclipseProjectFilterCount(searchSeleniumEclipseProject);
			
			logger.info("total projects count----" +totalResults);
			List<SeleniumEclipseProject> list = seleniumEclipseProjectService.getSeleniumEclipseProjectList(start, limit, searchSeleniumEclipseProject);
			logger.info("Projects by Jar list size----" +list.size());
			
			return getModelMapSeleniumProjectList(list, totalResults);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return getModelMapError("Error trying to List." + ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/selenium/getSeleniumProjectInfo",method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> projectJarId(HttpServletRequest request) throws ParseException{
		Map<String, Object> map = new HashMap<String ,Object>(2);
		
		String projectJarId_str = request.getParameter("projectJarId");
		SeleniumEclipseProject seleniumEclipseProject = new SeleniumEclipseProject();
		try{
		if(projectJarId_str != null){
			seleniumEclipseProject = seleniumEclipseProjectService.getSeleniumEclipseProjectById(Integer.parseInt(projectJarId_str));
		}else {
			return getModelMapError("Failed to Load Data");
		}
		return getModelMapSeleniumEclipseProjectInfo(seleniumEclipseProject);
		}catch(Exception e){
			e.printStackTrace();
			String msg = "Sorry problem in loading data";
			map.put("success", false);
			map.put("message", msg);
			return map;
		}
	}
	
	
	// JSon Construction
				private Map<String, Object> getModelMapSeleniumEclipseProjectInfo(SeleniumEclipseProject seleniumEclipseProject) {

					Map<String, Object> modelMap = new HashMap<String, Object>(3);
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonBeanProcessor(SeleniumEclipseProject.class,
							new JsonBeanProcessor() {
						public JSONObject processBean(Object bean,
								JsonConfig jsonConfig) {
							if (!(bean instanceof SeleniumEclipseProject)) {
								return new JSONObject(true);
							}

							SeleniumEclipseProject seleniumEclipseProject = (SeleniumEclipseProject) bean;
							List<String> projectJarNamesList = new ArrayList<String>();
							List<String> projectJarIds = Arrays.asList(seleniumEclipseProject.getProjectJars().split(","));
							logger.info("projectJarIds list Size is :"+projectJarIds.size());
							SimpleDateFormat importDateFormat = new SimpleDateFormat(
									"MM/dd/yyyy");
							
							String createdOnString = "";
							if (seleniumEclipseProject.getCreatedOn() != null) {
								createdOnString = importDateFormat.format(seleniumEclipseProject
										.getCreatedOn());
							}
							String updatedOnString = "";
							if (seleniumEclipseProject.getUpdatedOn() != null) {
								updatedOnString = importDateFormat.format(seleniumEclipseProject
										.getUpdatedOn());
							}
							
							for(String projectjarId : projectJarIds)
							{
								if(projectjarId != null || projectjarId == ""){
								logger.info("projectjar Id is:"+projectjarId);
								SeleniumProjectJar seleniumProjectJar = seleniumEclipseProjectService.getSeleniumProjectJarById(Integer.parseInt(projectjarId));
								logger.info("Master jar Object in If :");
								projectJarNamesList.add(seleniumProjectJar.getJarName());
								}
								
							}
							return new JSONObject()

									.element("projectJarId",
											seleniumEclipseProject.getProjectJarId())
									.element("userId", seleniumEclipseProject.getUserId())
									.element("projectName", seleniumEclipseProject.getProjectName())
									.element("jarIds", seleniumEclipseProject.getProjectJars())
									.element("jarNames", StringUtils.join(projectJarNamesList, ","))
									.element("projectJars",seleniumEclipseProject.getProjectJars())
									.element("createdBy", seleniumEclipseProject.getCreatedBy())
									.element("createdOn", createdOnString)
									.element("updatedBy", seleniumEclipseProject.getUpdatedBy())
									.element("updatedOn", updatedOnString);
						}
							});

					JSON json = JSONSerializer.toJSON(seleniumEclipseProject, jsonConfig);

					/*---*/
					modelMap.put("data", json);
					modelMap.put("success", true);

					return modelMap;
				}
	
	

	private Map<String, Object> getModelMapSeleniumProjectList(
			List<SeleniumEclipseProject> list, int totalResults) 
	{
		
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(SeleniumEclipseProject.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof SeleniumEclipseProject)) {
							return new JSONObject(true);
						}

						SeleniumEclipseProject seleniumEclipseProject = (SeleniumEclipseProject) bean;
						List<String> projectJarNamesList = new ArrayList<String>();
						List<String> projectJarIds = Arrays.asList(seleniumEclipseProject.getProjectJars().split(","));
						logger.info("projectJarIds list Size is :"+projectJarIds.size());
						
						String createdOn = "";
						SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
						if (seleniumEclipseProject.getCreatedOn() != null) {
							createdOn = sdf.format(seleniumEclipseProject.getCreatedOn());
						}
						
						String updatedOn = "";
						if (seleniumEclipseProject.getUpdatedOn() != null) {
							updatedOn = sdf.format(seleniumEclipseProject.getUpdatedOn());
						}
						
						for(String projectjarId : projectJarIds)
						{
							if(projectjarId != null || projectjarId == ""){
							logger.info("projectjar Id is:"+projectjarId);
							SeleniumProjectJar seleniumProjectJar = seleniumEclipseProjectService.getSeleniumProjectJarById(Integer.parseInt(projectjarId));
							logger.info("Master jar Object in If :");
							projectJarNamesList.add(seleniumProjectJar.getJarName());
							}
							
						}
						
						
						return new JSONObject()
							.element("projectJarListId",    seleniumEclipseProject.getProjectJarId())
							.element("projectName", seleniumEclipseProject.getProjectName())
							.element("jarIds", seleniumEclipseProject.getProjectJars())
							.element("jarNames", StringUtils.join(projectJarNamesList, ","))
							.element("createdOn", createdOn)
							.element("updatedOn", updatedOn);
													}
				});
		
				JSON json = JSONSerializer.toJSON(list, jsonConfig);

		
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}
	
		
		@RequestMapping(value = "/jarVersion/deleteProjectJarId", method = RequestMethod.GET)
		public @ResponseBody
		Map<String, Object> deleteProjectByJar(HttpServletRequest request) {

			// logger.info("Delete Method Strarted.,");
			Map<String, Object> modelMap = new HashMap<String, Object>(2);

			
			try 
			{
				int projectJarId = Integer.parseInt(request.getParameter("projectJarId"));
				
				SeleniumEclipseProject seleniumEclipseProject = seleniumEclipseProjectService.getSeleniumEclipseProjectById(projectJarId);
				seleniumEclipseProjectService.removeSeleniumEclipseProject(seleniumEclipseProject);;
				logger.info("Delete Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Deleted Successfully");
				return modelMap;

			} 
			catch (Exception ex) 
			{
				modelMap.put("success", false);
				modelMap.put("message", "Error in deletion");
				return modelMap;
			}

		}
		
		@RequestMapping(value = "/jarVersion/sendProjectJarEmail", method = RequestMethod.GET)
		public @ResponseBody
		Map<String, Object> sendProjectByJarEmail(HttpServletRequest request) {

			// logger.info("Delete Method Strarted.,");
			Map<String, Object> modelMap = new HashMap<String, Object>(2);

			
			try 
			{

				User user = new User();

				String id_value = "", res = "";
				if ((StringUtils.isNotBlank(request.getParameter("userId")))
						|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {

					id_value = request.getParameter("userId");
					user = userService.getUserById(Integer.parseInt(id_value));
					logger.info("Getting User Object based on Id :" + id_value);

				}

				int projectJarId = Integer.parseInt(request.getParameter("projectJarId"));
				
				SeleniumEclipseProject seleniumEclipseProject = seleniumEclipseProjectService.getSeleniumEclipseProjectById(projectJarId);
				String compressedFileName = "" + seleniumEclipseProject.getProjectName() + ".zip";
				String projectDownloadPath = request.getScheme() + "://"
						+ request.getServerName() + ":" + request.getServerPort()
						+ "/upload/" + compressedFileName;

				// Sending mail to user with link to download
				// Reading From EMail ID from Properties File
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);
				
				EmailJob emailJob = new EmailJob();
				
				emailJob.setEmailType("SELENIUM_JAR_APPLICATION");

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", user.getUserEmail());
				map.put("subject", "ATOC - Selenium Project");
				map.put("user", user.getFirstName());
				map.put("projectName", seleniumEclipseProject.getProjectName());
				map.put("projectDownloadPath", projectDownloadPath);
				map.put("content",
						"By clicking download link your project will downloads succesfully. Thank You");
				
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);
				//res = mailService.sendDownloadLinkEmail(map);

				logger.info("sendProjectByJarEmail Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "An email sent Successfully");
				return modelMap;

			} 
			catch (Exception ex) 
			{
				modelMap.put("success", false);
				modelMap.put("message", "Error in sending an email");
				return modelMap;
			}

		}
	


	@RequestMapping(value = "/jarVersion/viewJarVersionList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewJarVersionList(HttpServletRequest request) {

		try {
			
			SeleniumProjectJar seleniumProjectJar = new SeleniumProjectJar();

			int totalResults = seleniumEclipseProjectService.getSeleniumProjectJarCount(seleniumProjectJar);
			
			List<SeleniumProjectJar> seleniumProjectJarList = seleniumEclipseProjectService.getAllSeleniumProjectJarList(seleniumProjectJar);
			
			logger.info("Size of Selenium Projects Jar -------" +seleniumProjectJarList.size());
			return getModelMapJarsList(seleniumProjectJarList, totalResults);
			
			

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}
	// Save Service
		@RequestMapping(value = "/jarVersion/saveConfigJar", method = RequestMethod.POST)
		public @ResponseBody
		Map<String, Object> saveConfigJar(HttpServletRequest request,MultipartFile file) {
			logger.info("Insert Method Strarted.,");
			Map<String, Object> modelMap = new HashMap<String, Object>(2);
			
			InputStream in 			= null;
			FileOutputStream out 	= null;
			
			SeleniumProjectJar seleniumProjectJar = new SeleniumProjectJar();
			String id_value = request.getParameter("userId");
			
			if(StringUtils.isNotBlank(request.getParameter("category")))
			{
				seleniumProjectJar.setCategory(request.getParameter("category"));
				logger.info("category.,"+request.getParameter("category"));
			}
			
			
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String jarFileRespository = uploadDirectoryBase + File.separator + "webapps"
					+ File.separator + "upload" + File.separator + "jarfile_repository";
			
			seleniumProjectJar.setStatus("TRUE");
			String jarName = file.getOriginalFilename();
			if (jarName != null) {
				SeleniumProjectJar selJarWithReturn = new SeleniumProjectJar();
				seleniumProjectJar.setJarName(file.getOriginalFilename());
				selJarWithReturn = seleniumEclipseProjectService.getJarBySearchParam(seleniumProjectJar);
				if(selJarWithReturn != null){
						seleniumProjectJar.setCategory(request.getParameter("category"));
						if (selJarWithReturn.getJarName().equalsIgnoreCase(seleniumProjectJar.getJarName())) 
							{
								logger.info("***************** Jar is already Exists **************:");
								modelMap.put("success", true);
								modelMap.put("response", false);
								modelMap.put("message", "Jar already exists");
								logger.info("model map false"+modelMap.get("success")+"response"+modelMap.get("response"));
								return modelMap;
							} 
						}
						else {
							try{
								
								SeleniumProjectJar existingProjectJar = null;
								if(StringUtils.isNotBlank(request.getParameter("jarId")))
								{
									existingProjectJar = seleniumEclipseProjectService.getSeleniumProjectJarById(new Integer(request.getParameter("jarId")));
									String oldJarName = existingProjectJar.getJarName();
									File oldJarFile = new File(jarFileRespository + File.separator
					                + oldJarName);
									try
									{
										oldJarFile.delete();
									}
									catch(Exception ex)
									{
										logger.info("Exception while deleting old jar:"+ex.getMessage());
									}
									
									
								}
								if (existingProjectJar != null)
								{
									seleniumProjectJar.setJarId(existingProjectJar.getJarId());
								}
								
								seleniumProjectJar.setJarName(jarName);
								seleniumProjectJar.setCategory(request.getParameter("category"));
							
								seleniumProjectJar = seleniumEclipseProjectService.saveConfigJar(seleniumProjectJar);
								
								
								in = file.getInputStream();
								out = new FileOutputStream(new File(jarFileRespository + File.separator
					                + file.getOriginalFilename()));
								int read = 0;
								final byte[] bytes = new byte[1024];
								while ((read = in.read(bytes)) != -1) {
					            out.write(bytes, 0, read);
								}
								logger.info("Insert Method Executed.,");
								modelMap.put("success", true);
								modelMap.put("response", true);
								modelMap.put("message", "Saved Successfully");
								return modelMap;
								} catch (Exception ex) {
									String msg = "Sorry problem in saving data";
									modelMap.put("success", true);
									modelMap.put("response", false);
									modelMap.put("message", msg);
									return modelMap;
									}
									finally {
										try {
											out.flush();
											in.close();
											out.close();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
								
										}
					
									}
								}
				return modelMap;
				}
				// JSon Construction
		private Map<String, Object> getModelMapJarsList(
				List<SeleniumProjectJar> list, int totalResults) {

			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("total", totalResults);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonBeanProcessor(SeleniumProjectJar.class,
					new JsonBeanProcessor() {
						public JSONObject processBean(Object bean,
								JsonConfig jsonConfig) {
							if (!(bean instanceof SeleniumProjectJar)) {
								return new JSONObject(true);
							}

							SeleniumProjectJar seleniumProjectJar = (SeleniumProjectJar) bean;
							
							return new JSONObject()
							.element("jarId",    seleniumProjectJar.getJarId())
							.element("jarName",  seleniumProjectJar.getJarName())
							.element("category", seleniumProjectJar.getCategory());
						}
					});
			
					JSON json = JSONSerializer.toJSON(list, jsonConfig);

			
			modelMap.put("data", json);
			modelMap.put("success", true);

			return modelMap;
		}

		private List<String> getProjectJarNames(List<Integer> selectedProjectJarIds) 
		{
			List<String> selectedProjectJarNames = new ArrayList<String>();
			logger.info("jar ids ----" +selectedProjectJarIds);
			
			for(Integer selectedProjectJarId : selectedProjectJarIds)
			{
				
				SeleniumProjectJar seleniumProjectJar = seleniumEclipseProjectService.getSeleniumProjectJarById(selectedProjectJarId);
				selectedProjectJarNames.add(seleniumProjectJar.getJarName());
			}
			
			return selectedProjectJarNames;
			
			
		}

	@RequestMapping(value = "/jarVersion/createProjectByJars", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> createProjectByJars(HttpServletRequest request,
			HttpServletResponse response) {
		FTPClient client = new FTPClient();
		FileOutputStream fos = null;
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			User user = new User();
			
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));


			String id_value = "", res = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {

				id_value = request.getParameter("userId");
				user = userService.getUserById(Integer.parseInt(id_value));
				logger.info("Getting User Object based on Id :" + id_value);

			}
			
			String projectName = "";
			if ((StringUtils.isNotBlank(request.getParameter("projectName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("projectName")))) {

				projectName = request.getParameter("projectName");
				logger.info("Project Name :" + projectName);

			}
			
			String selected_jar = "";
			List<Integer> selectedJarIds = new ArrayList<Integer>();
			if ((StringUtils.isNotBlank(request.getParameter("selected_jar")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("selected_jar")))) {

				selected_jar = request.getParameter("selected_jar");
				String[] selected_jars = selected_jar.split(",");
				
				for (int i = 0; i < selected_jars.length; i++) {
					if (!(selected_jars[i].toString()
							.equalsIgnoreCase("checkAll"))) {
						selectedJarIds.add(Integer.parseInt(selected_jars[i].toString()));
					}
				}
			}
			
			SeleniumEclipseProject seleniumEclipseProject = new SeleniumEclipseProject();
			if(StringUtils.isNotBlank(request.getParameter("projectJarId")))
			{
				int projectJarId = Integer.parseInt(request.getParameter("projectJarId"));
				seleniumEclipseProject = seleniumEclipseProjectService.getSeleniumEclipseProjectById(projectJarId);
				seleniumEclipseProject.setUpdatedOn(todayDate);
				seleniumEclipseProject.setUpdatedBy(user.getUserId());
			}
			else
			{
				seleniumEclipseProject.setCreatedOn(todayDate);
				seleniumEclipseProject.setCreatedBy(user.getUserId());
			}
			
			
			seleniumEclipseProject.setUserId(user.getUserId());
			seleniumEclipseProject.setProjectName(projectName);
			seleniumEclipseProject.setProjectJars(StringUtils.join(selectedJarIds, ","));
			
			//Save the jar ids into Project list jar table.
			seleniumEclipseProjectService.saveSeleniumEclipseProject(seleniumEclipseProject);
			
			
			List<String> selectedJars = getProjectJarNames(selectedJarIds);
			
			// getting Master Project from tomcat
			String fileName = "atocProject.zip";
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String appPath = uploadDirectoryBase + uploadDirectory;
			String fullPath = appPath + fileName;

			//Remove if project exist
			String extprojectPath = appPath + projectName+".zip";
			
			File deleteFile = new File(extprojectPath);
			if(deleteFile.delete())
			{
				logger.info("--------------existing project deleted--------------");
				//Delete Folder
				String delete_folder = appPath + projectName;
				File sourceProjectFolder = new File(delete_folder);
				if (sourceProjectFolder.exists())
				{
					
					//FileUtils.cleanDirectory(sourceProjectFolder);
					//FileUtils.forceDelete(new File(delete_folder));
					FileDeleteStrategy.FORCE.delete(new File(delete_folder));
					logger.info("Delete folder"+ delete_folder);
				}
				
			}
			
			String outPutDirectory = appPath + projectName;
			extractMasterProject(appPath, fullPath, projectName);

			// copy jar files from repositiry to project lib folder
			String libPath = uploadDirectoryBase + uploadDirectory
					+ projectName + File.separator + "lib" + File.separator;
			String jarRepopath = uploadDirectoryBase + jarFileRespository
					+ File.separator;
			File directory = new File(jarRepopath);

			// get all the files from a directory
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile()) {

					if (selectedJars.contains(file.getName())) {
						// copy from repo to libs
						String sourceFileName = jarRepopath + file.getName();
						String destinationFileName = libPath + file.getName();
						copyFile(sourceFileName, destinationFileName);
					}

				}
			}

			// Modify classpath
			File libDirectory = new File(libPath);
			File[] libradyFiles = libDirectory.listFiles();
			File classPathFile = new File(uploadDirectoryBase + uploadDirectory
					+ projectName + File.separator + ".classpath");
			File classPathxmlFile = new File(uploadDirectoryBase
					+ uploadDirectory + projectName + File.separator + ".xml");
			classPathFile.renameTo(classPathxmlFile);
			updateClassPath(classPathxmlFile.getAbsolutePath(), libradyFiles);
			classPathxmlFile.renameTo(classPathFile);

			// final project file
			String inputFolder = uploadDirectoryBase + uploadDirectory
					+ projectName;
			String compressedFileName = "" + projectName + ".zip";
			String compressedFilePath = uploadDirectoryBase + uploadDirectory
					+ compressedFileName;
			compressProject(inputFolder, compressedFilePath);
			String contextPath = request.getContextPath() + uploadDirectory
					+ compressedFileName;
			logger.info("project Download Path is :" + request.getScheme()
					+ "://" + request.getServerName() + ":"
					+ request.getServerPort() + "/upload/"
					+ compressedFileName);
			String projectDownloadPath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ "/upload/" + compressedFileName;

			
			/* Will do this later satya
			 * String delete_folder = appPath + projectName;
			File projectFile = new File(delete_folder);
			if (projectFile.exists())
			{
				FileUtils.deleteDirectory(new File(delete_folder));
			}*/
			
			
			// Sending mail to user with link to download
			// Reading From EMail ID from Properties File
			Properties properties_mail = new Properties();
			String propFileName_mail = "/properties/mail.properties";
			InputStream stream_mail = getClass().getClassLoader()
					.getResourceAsStream(propFileName_mail);
			properties_mail.load(stream_mail);
			
			
			EmailJob emailJob = new EmailJob();
			
			

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("from",
					properties_mail.getProperty("javaMailSender.username"));
			map.put("to", user.getUserEmail());
			map.put("subject", "ATOC - Selenium Project");
			map.put("user", user.getFirstName());
			map.put("projectName", projectName);
			map.put("projectDownloadPath", projectDownloadPath);
			map.put("content",
					"By clicking download link your project will downloads succesfully. Thank You");
			
			byte[] byteArray = null;
			byteArray = convertObjectToByteArray(map);
			emailJob.setJobDataByteArray(byteArray);
			userService.saveOrUpdateEmailJob(emailJob);

			logger.info("Created Project By Jar Completed .,");
			modelMap.put("success", true);
			modelMap.put("message", "Project Created Successfully");
			return modelMap;

		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in Creating Project";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
	}

		
	

	

		private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

	private static void extractMasterProject(String destinationFolder,
			String zipFile, String projectName) {
		File directory = new File(destinationFolder);
		FileInputStream fInput = null;
		ZipInputStream zipInput = null;
		FileOutputStream fOutput = null;
		
		// if the output directory doesn't exist, create it
		if (!directory.exists())
			directory.mkdirs();

		// buffer for read and write data to file
		byte[] buffer = new byte[2048];
		File nDir = null;
		try {
			fInput = new FileInputStream(zipFile);
			zipInput = new ZipInputStream(fInput);

			ZipEntry entry = zipInput.getNextEntry();
			while (entry != null) {
				String entryName = entry.getName();
				File file = new File(destinationFolder + File.separator
						+ entryName);

				// create the directories of the zip directory
				if (entry.isDirectory()) {
					File newDir = new File(file.getAbsolutePath());
					if (!newDir.exists()) {
						boolean success = newDir.mkdirs();
						nDir = newDir;
						if (success == false) {
							System.out.println("Problem creating Folder");
						}
					}
				} else {
					fOutput = new FileOutputStream(file);
					int count = 0;
					while ((count = zipInput.read(buffer)) > 0) {
						// write 'count' bytes to the file output stream
						fOutput.write(buffer, 0, count);
					}
					fOutput.close();
					fOutput = null;
				}
				// close ZipEntry and take the next one
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}

			// close the last ZipEntry
			zipInput.closeEntry();
			zipInput.close();
			fInput.close();
			File destinationFile = new File(destinationFolder + File.separator
					+ "atocProject");
			File rFile = new File(destinationFolder + File.separator
					+ projectName);
			destinationFile.renameTo(rFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fOutput = null;
				zipInput = null;
				fInput = null;
			}
			catch(Exception ex)
			{
				System.out.println("Exception while closing:"+ex.getMessage());
			}
				
			
		}

	}

	private static void copyFile(String sourceFileName, String destionFileName) {
		InputStream in = null;
		OutputStream out = null;
		try {

			File sourceFile = new File(sourceFileName);
			File destinationFile = new File(destionFileName);
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(destinationFile);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
				out.close();
				in = null;
				out = null;
			}
			catch(Exception exception)
			{
				
			}
		}
	}

	public void updateClassPath(String fileName, File[] fList) {

		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document = documentBuilder.parse(fileName);

			Node rootNode = document.getElementsByTagName("classpath").item(0);

			for (File file : fList) {
				if (file.isFile()) {
					Element entry = document.createElement("classpathentry");
					entry.setAttribute("kind", "lib");
					entry.setAttribute("path", "lib/" + file.getName() + "");
					rootNode.appendChild(entry);
				}
			}

			// write the DOM object to the file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(document);

			StreamResult streamResult = new StreamResult(new File(fileName));
			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	public static void compressProject(String inputFolder,
			String targetZippedFolder) throws IOException {
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		FileInputStream fileInputStream = null;
		try
		{
			

			fileOutputStream = new FileOutputStream(targetZippedFolder);
			zipOutputStream = new ZipOutputStream(fileOutputStream);

			File inputFile = new File(inputFolder);
			String fileName = inputFile.getName();
			
			if (inputFile.isFile())
			{
				fileInputStream = new FileInputStream(inputFile);
				compressFile(fileInputStream, "", zipOutputStream,fileName);
			}
			else if (inputFile.isDirectory())
			{
				compressFolder(zipOutputStream, inputFile, "");
			}
				

			
		}
		catch(Exception ioe)
		{
			System.out.println("Exception while compress project:"+ioe.getMessage());
		}
		finally
		{
			try
			{
				/*fileOutputStream.close();
				fileInputStream.close();*/
				zipOutputStream.close();
				fileOutputStream = null;
				fileInputStream = null;
				zipOutputStream = null;
			}
			catch(Exception foe)
			{
				System.out.println("Exception while compress project closing streasm:"+foe.getMessage());
			}
		}
		
		
		
	}

	public static void compressFolder(ZipOutputStream zipOutputStream,
			File inputFolder, String parentName) throws IOException {

		String myname = parentName + inputFolder.getName() + "\\";

		ZipEntry folderZipEntry = new ZipEntry(myname);
		zipOutputStream.putNextEntry(folderZipEntry);

		File[] contents = inputFolder.listFiles();

		for (File f : contents) {
			if (f.isFile())
			{
				String fileName = f.getName();
				FileInputStream fileInputStream = new FileInputStream(f);
				compressFile(fileInputStream, myname, zipOutputStream,fileName);
			}
			else if (f.isDirectory())
			{
				compressFolder(zipOutputStream, f, myname);
			}
				
			
				
		}
		zipOutputStream.closeEntry();
		
	}

	public static void compressFile(FileInputStream fileInputStream, String parentName,
			ZipOutputStream zipOutputStream,String fileName) throws IOException {

		// A ZipEntry represents a file entry in the zip archive
		// We name the ZipEntry after the original file's name
		ZipEntry zipEntry = new ZipEntry(parentName + fileName);
		zipOutputStream.putNextEntry(zipEntry);

		//FileInputStream fileInputStream = new FileInputStream(inputFile);
		byte[] buf = new byte[1024];
		int bytesRead;

		// Read the input file by chucks of 1024 bytes
		// and write the read bytes to the zip stream
		while ((bytesRead = fileInputStream.read(buf)) > 0) {
			zipOutputStream.write(buf, 0, bytesRead);
		}

		// close ZipEntry to store the stream to the file
		zipOutputStream.closeEntry();
		fileInputStream.close();
	}
	
	private static byte[] convertObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
}
	
	

}

