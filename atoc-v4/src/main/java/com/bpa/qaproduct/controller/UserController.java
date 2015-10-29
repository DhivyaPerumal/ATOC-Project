package com.bpa.qaproduct.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.entity.UserToken;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.RoleService;
import com.bpa.qaproduct.service.UserRoleService;
import com.bpa.qaproduct.service.UserService;
import com.bpa.qaproduct.service.UserTokenService;

@Controller
public class UserController {

	protected final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private JavaMailSenderImpl javamailsenderImpl;

	@Autowired
	private UserTokenService userTokenservice;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private MailService mailService;

	private HttpHeaders addAccessControllAllowOrigin() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return headers;
	}

	// Get Record Service

	@RequestMapping(value = "/user/loginVerification", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> loginVerification(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {

			// Constructing User Search Object
			User searchUser = new User();

			String userEmail = request.getParameter("userEmail");
			if (userEmail != null && userEmail.isEmpty() == false) {
				searchUser.setUserEmail(userEmail);
			}

			String userPassword = request.getParameter("userPassword");
			if (userPassword != null && userPassword.isEmpty() == false) {
				searchUser.setUserPassword(CustomerRegistrationController
						.base64EncryptPassword(userPassword)); // Sending
																// password with
																// encryption
				// searchUser.setUserPassword(userPassword); // Sending password
				// with out encryption
				logger.info("### gng to Login Encrypt Pwd is :"
						+ CustomerRegistrationController
								.base64EncryptPassword(searchUser
										.getUserPassword()));
				logger.info("### gng to Login Decrypt Pwd is :"
						+ CustomerRegistrationController
								.base64DecryptPassword(searchUser
										.getUserPassword()));

			}

			String colorSelected = request.getParameter("colorSkin");
			if (StringUtils.isNotBlank(request.getParameter("colorSkin"))) {
				if (colorSelected == "default"
						|| colorSelected.equalsIgnoreCase("default")) {
					searchUser.setColorSkin("style.default.css");
				} else if (colorSelected == "navyblue"
						|| colorSelected.equalsIgnoreCase("navyblue")) {
					searchUser.setColorSkin("style.navyblue.css");
				} else if (colorSelected == "palegreen"
						|| colorSelected.equalsIgnoreCase("palegreen")) {
					searchUser.setColorSkin("style.palegreen.css");
				} else if (colorSelected == "red"
						|| colorSelected.equalsIgnoreCase("red")) {
					searchUser.setColorSkin("style.red.css");
				} else if (colorSelected == "green"
						|| colorSelected.equalsIgnoreCase("green")) {
					searchUser.setColorSkin("style.green.css");
				} else if (colorSelected == "brown"
						|| colorSelected.equalsIgnoreCase("brown")) {
					searchUser.setColorSkin("style.brown.css");
				}

			}

			User user = userService.getUserBySearchParameter(searchUser);

			if (user.getUserId() != null) {

				UserRole userRole = new UserRole();
				userRole.setUser(user);
				userRole = userRoleService.searchbyUser(userRole);
				logger.info("user role object :" + userRole);

				Role role = new Role();
				role.setRoleId(userRole.getRole().getRoleId());
				logger.info("user roleid" + userRole.getRole().getRoleId());
				role = roleService.getRoleById(role.getRoleId());
				logger.info("loginVerification Executed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Saved Successfully");
				return getModelMapUser(user, role);

			} else {
				return getModelMapError("Login Failed");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return getModelMapError("Login Failed");

		}

	}

	// Generate Password Dynamically Method

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	public static String generateRandomPassword() {

		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	// Add Url Properties

	@RequestMapping(value = "/user/addUrlProperty", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addUrl(HttpServletRequest request) {
		logger.info("Url Method Started");
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		String cssSelector_path = "";
		String projectDownloadPath = "";
		String cssSelectorFullPath = "";
		String cssSelectorFileName = "";
		try {
			String userId = request.getParameter("userId");
			logger.info("userId" + userId);
			Document doc;

			String url = request.getParameter("urlname");
			logger.info("urlname" + url);

			Properties props = new Properties();
			// For Dynamic data
			String uploadDirectoryBase = System.getProperty("catalina.base");
			logger.info("uploadDirectoryBase" + uploadDirectoryBase);
			cssSelector_path = uploadDirectoryBase + "/webapps/cssproperties/";
			logger.info("addDirectory" + cssSelector_path);
			// For Static Data
			logger.info(cssSelector_path);
			cssSelectorFullPath = cssSelector_path + "cssSelector-" + userId
					+ ".properties";
			cssSelectorFileName = "cssSelector-" + userId + ".properties";
			FileOutputStream fos = new FileOutputStream(cssSelectorFullPath,
					true);

			props.store(fos, null);
			doc = Jsoup.connect(url).get();

			Elements inputElements = doc.select("*");

			for (Element inputElement1 : inputElements) {
				String cssSelectorName = inputElement1.cssSelector();
				props.setProperty("", cssSelectorName);
				props.store(fos, null);

			}

			logger.info("project Download Path is :" + request.getScheme()
					+ "://" + request.getServerName() + ":"
					+ request.getServerPort() + "/cssproperties/"
					+ cssSelectorFileName);
			projectDownloadPath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ "/cssproperties/" + cssSelectorFileName;

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelMap.put("success", true);
		modelMap.put("cssSelectorFileName", cssSelectorFileName);
		modelMap.put("message", " Css Selctor properties Created Successfully");
		return modelMap;
	}

	// Download Url Properties

	@RequestMapping(value = "/user/downloadUrlProperty", method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {

			String cssSelectorFileName = request
					.getParameter("cssSelectorFileName");

			// get absolute path of the application
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String uploadDirectory = "\\webapps\\cssproperties\\";

			String appPath = uploadDirectoryBase + uploadDirectory;

			// construct the complete absolute path of the file
			String fullPath = appPath + cssSelectorFileName;
			File downloadFile = new File(fullPath);
			FileInputStream inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = null;
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			response.addHeader("content-disposition",
					"attachment; filename=cssSelector.properties");

			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

			String cssSelectorFileName1 = request
					.getParameter("cssSelectorFileName");
			String uploadDirectoryBase1 = System.getProperty("catalina.base");
			String uploadDirectory1 = "\\webapps\\cssproperties\\";
			String appPath1 = uploadDirectoryBase1 + uploadDirectory1;

			String fullPath1 = appPath1 + cssSelectorFileName1;

			File file = new File(fullPath);
			boolean success = file.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Add Organisation User Service
	
	
	@RequestMapping(value = "/user/addOrgUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addUser(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			User user = new User();
			Role role = new Role();
			String id_value = "";
			logger.info("object");
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById((Integer.parseInt(id_value)));
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
				logger.info("id_value");

			} else {
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
			}

			String userEmail = request.getParameter("contactEmail");
			logger.info("contact emailid is:" + userEmail);
			if (userEmail != null) {
				User searchUser = new User();
				searchUser.setUserEmail(userEmail);
				searchUser = userService.getUserBySearchParameter(searchUser);
				if(searchUser!= null){
				logger.info("user emailid is:" + user.getUserEmail());
				if (userEmail == searchUser.getUserEmail()
						|| userEmail.equalsIgnoreCase(searchUser.getUserEmail())) {
					logger.info("user already Exists **************:");
					modelMap.put("success", false);
					modelMap.put("message",
							"User already Registered with this Emailid");
					return modelMap;
				}else{
					user.setUserEmail(userEmail);
					logger.info("*** User not exists ***");
					
				}
				}
			}

			String firstName = request.getParameter("firstname");
			if (firstName != null) {

				user.setFirstName(firstName);
				logger.info("firstName" + user.getFirstName());
			}

			String lastName = request.getParameter("lastname");
			if (lastName != null) {

				user.setLastName(lastName);
				logger.info("lastname" + user.getLastName());

			}
			userEmail = request.getParameter("contactEmail");
			if (userEmail != null) {
				user.setUserEmail(userEmail);
				logger.info("contactEmail" + user.getUserEmail());
			}
			String addOrgUser_Pwd = generateRandomPassword(); 
			user.setUserPassword(CustomerRegistrationController.base64EncryptPassword(addOrgUser_Pwd));
			logger.info("password" + user.getUserPassword());

			String isOrgAdmin = request.getParameter("isOrgAdmin");
			if (isOrgAdmin == "yes" || isOrgAdmin.equalsIgnoreCase("yes")) {
				isOrgAdmin = "Org Admin";
				role.setRoleName(isOrgAdmin);
				role = roleService.getRoleBySearchParam(role);
				logger.info("RoleId is :" + role.getRoleId());

			}

			else if (isOrgAdmin == "no" || isOrgAdmin.equalsIgnoreCase("no")) {
				isOrgAdmin = "Org User";
				role.setRoleName(isOrgAdmin);
				role = roleService.getRoleBySearchParam(role);
				logger.info("RoleId is :" + role.getRoleId());
			} else {
				isOrgAdmin = "";
			}

			String isActive = request.getParameter("isActive");
			user.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			user.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			user.setUpdatedBy(Integer.parseInt(updatedBy));

			String organizationName = request.getParameter("orgName");
			logger.info("organizationName is :" + organizationName);
			Organization organization = new Organization();
			if (organizationName != null) {
				logger.info("organizationName is :" + organizationName);
				organization.setOrganizationName(organizationName);
				organization = organizationService
						.getOrganizationBySearchParam(organization);
				user.setOrganization(organization);
				logger.info("organization id based on Name is :"
						+ organization.getOrganizationId());
			}

			user = userService.saveOnlyObj(user);
			logger.info("After saving user :" + user.getUserId());
			if (user != null) {
				UserRole userRole = new UserRole();
				userRole.setUser(user);

				userRole.setRole(role);

				userRole = userRoleService.saveUserRoleWithReturn(userRole);
				Map model = new HashMap();
				String text = null;
				
				String scheme = request.getScheme();
				String serverName = request.getServerName();
				int port = request.getServerPort();
				String contextpath = request.getContextPath();
				String appURL = scheme+"://"+serverName+":"+port+contextpath;
				logger.info("App url is :"+appURL);
				logger.info("Add org USer"+user.getOrganization().getOrganizationName());
				logger.info("User Org Password is"+addOrgUser_Pwd);
				
				
				
				// Reading From EMail ID from Properties File
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);
				logger.info("from"+properties_mail.getProperty("javaMailSender.username"));
				logger.info("to"+user.getUserEmail());
				logger.info("f NAme"+user.getFirstName());
				logger.info("Subject is"+properties_mail.getProperty("addorguser.subject"));
				EmailJob emailJob = new EmailJob();
				emailJob.setEmailType("ADD_LOG_USER");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", user.getUserEmail());
				map.put("subject", properties_mail.getProperty("addorguser.subject"));
				map.put("firstName", user.getFirstName());
				map.put("userEmail", user.getUserEmail());
				map.put("userPassword", addOrgUser_Pwd);
				map.put("appURL", appURL);
				map.put("organizationName", user.getOrganization()
						.getOrganizationName());
				map.put("templateName", "UserDetails.vm");
				map.put("heading",
						"Thank You for Registering with us .!! Here is your Login credentials.");
				map.put("content",
						"By Clicking download link your project will downloads Succesfully. Thank You .!");
			
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);
				
				logger.info("After saving User Role in Db :"
						+ userRole.getUserRoleId());
			}

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		}

		catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	
	
	
	
	

	

	// Add master User Service

	@RequestMapping(value = "/user/addmasterUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addmasterUser(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			User user = new User();
			Role role = new Role();
			String id_value = "";
			logger.info("object");

			if ((StringUtils.isNotBlank(request.getParameter("userMasterId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("userMasterId")))) {
				id_value = request.getParameter("userMasterId");
				user = userService.getUserById((Integer.parseInt(id_value)));
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
				logger.info("id_value");

			} else {
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
			}

			String userEmail = request.getParameter("contactEmail");
			logger.info("contact emailid is:" + userEmail);
			if (userEmail != null) {
				User searchUser = new User();
				searchUser.setUserEmail(userEmail);
				searchUser = userService.getUserBySearchParameter(searchUser);
				logger.info("user emailid is:" + user.getUserEmail());
				if (searchUser != null) {

					if (userEmail == searchUser.getUserEmail()
							|| userEmail.equalsIgnoreCase(searchUser
									.getUserEmail())) {
						logger.info("user already Exists **************:");
						modelMap.put("success", false);
						modelMap.put("userExist", true);
						modelMap.put("message",
								"User already Registered with this Emailid");
						return modelMap;
					} else {
						user.setUserEmail(userEmail);
						logger.info("*** User not exists ***");
					}
				}
			}

			String firstName = request.getParameter("firstname");
			if (firstName != null) {

				user.setFirstName(firstName);
				logger.info("firstName" + user.getFirstName());
			}

			String lastName = request.getParameter("lastname");
			if (lastName != null) {

				user.setLastName(lastName);
				logger.info("lastname" + user.getLastName());

			}
			userEmail = request.getParameter("contactEmail");
			if (userEmail != null) {
				user.setUserEmail(userEmail);
				logger.info("contactEmail" + user.getUserEmail());
			}
			String addMaster_Pwd = generateRandomPassword();
			user.setUserPassword(CustomerRegistrationController
					.base64EncryptPassword(addMaster_Pwd));
			logger.info("password" + user.getUserPassword());

			String ismasteradmin = request.getParameter("ismasteradmin");
			if (ismasteradmin != null) {
				ismasteradmin = "Master Admin";
				role.setRoleName(ismasteradmin);
				role = roleService.getRoleBySearchParam(role);
				logger.info("RoleId is :" + role.getRoleId());

			}

			String isActive = request.getParameter("isActive");
			user.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			user.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			user.setUpdatedBy(Integer.parseInt(updatedBy));

			String organizationName = request.getParameter("orgName");
			logger.info("organizationName is :" + organizationName);
			Organization organization = new Organization();
			if (organizationName != null) {
				logger.info("organizationName is :" + organizationName);
				organization.setOrganizationName(organizationName);
				organization = organizationService
						.getOrganizationBySearchParam(organization);
				user.setOrganization(organization);
				logger.info("organization id based on Name is :"
						+ organization.getOrganizationId());
			}
			String RoleId = request.getParameter("orgName");
			logger.info("RoleId is:" + RoleId);

			user = userService.saveOnlyObj(user);
			logger.info("After saving user :" + user.getUserId());
			if (user != null) {
				UserRole userRole = new UserRole();

				userRole.setUser(user);
				logger.info("After saving user in User Role :"
						+ user.getUserId());
				userRole.setRole(role);
				logger.info("After saving Role in User Role :"
						+ role.getRoleId());
				userRole = userRoleService.saveUserRoleWithReturn(userRole);
				Map model = new HashMap();
				String text = null;

				String scheme = request.getScheme();
				String serverName = request.getServerName();
				int port = request.getServerPort();
				String contextpath = request.getContextPath();
				String appURL = scheme + "://" + serverName + ":" + port
						+ contextpath;
				logger.info("App url is :" + appURL);

				// Reading From EMail ID from Properties File
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				EmailJob emailJob = new EmailJob();
				emailJob.setEmailType("MASTER_LOGIN");;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", user.getUserEmail());
				String subject = "";
				subject = properties_mail.getProperty("addmasteruser.subject");

				map.put("subject", subject);
				map.put("firstName", user.getFirstName());
				map.put("userEmail", user.getUserEmail());
				map.put("userPassword", addMaster_Pwd);
				map.put("appURL", appURL);
				map.put("organizationName", user.getOrganization()
						.getOrganizationName());
				map.put("templateName", "UserDetails.vm");
				map.put("heading",
						"Thank You for Registering with us .!! Here is your Login credentials.");
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);

				logger.info("After saving User Role in Db :"
						+ userRole.getUserRoleId());
			}

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		}

		catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	// View Master User
	@RequestMapping(value = "/user/viewMasterUser", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewMasterUser(HttpServletRequest request) {

		int start = 0;
		int limit = 10;

		try {
			UserRole userRole = new UserRole();
			UserRole userRole_db = null;
			start = Integer.parseInt(request.getParameter("iDisplayStart"));
			limit = Integer.parseInt(request.getParameter("iDisplayLength"));

			String id_value = "";

			User user = new User();
			User user_db = null;

			String userOrganizationAdmin = "";
			Organization userOrganization = new Organization();

			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user_db = new User();
				user_db = userService.getUserById((Integer.parseInt(id_value)));
				userRole_db = userRoleService.getUserRoleById(user_db
						.getUserId());
				userRole.setRole(userRole_db.getRole());
				if (userRole.getRole().getRoleName()
						.equalsIgnoreCase("Org Admin")) {
					userRole.setUser(user_db);
					userOrganizationAdmin = userRole.getUser()
							.getOrganization().getOrganizationName();
				}

			}

			if ((StringUtils.isNotBlank(request.getParameter("firstName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("firstName")))) {
				user.setFirstName(request.getParameter("firstName"));
				logger.info("Search data for firstName"
						+ request.getParameter("firstName"));

			}

			if ((StringUtils.isNotBlank(request.getParameter("lastName")))
					|| (StringUtils
							.isNotEmpty(request.getParameter("lastName")))) {
				user.setLastName(request.getParameter("lastName"));
				logger.info("Serach for lastName"
						+ request.getParameter("lastName"));

			}

			if ((StringUtils.isNotBlank(request.getParameter("organization")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("organization")))) {
				userOrganization.setOrganizationName(request
						.getParameter("organization"));
				logger.info("Serach for lastName"
						+ request.getParameter("organization"));
				user.setOrganization(userOrganization);

			} else if ((StringUtils.isNotBlank(userOrganizationAdmin))
					|| (StringUtils.isNotEmpty(userOrganizationAdmin))) {
				userOrganization.setOrganizationName(userOrganizationAdmin);
				logger.info("Serach for lastName" + userOrganizationAdmin);
				user.setOrganization(userOrganization);

			}
			userRole.setUser(user);

			int totalResults = userService.getUserRoleFilterCount(userRole);
			List<UserRole> list = userService.getUserRoleList(userRole, start,
					limit);

			return getModelMapUserRoleList(list, totalResults);
		} catch (Exception ex) {

			ex.printStackTrace();
			return getModelMapError("Error" + ex.getMessage());
		}

	}

	// Get Forgot Password Service

	@RequestMapping(value = "/user/forgotPassword", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> forgotPassword(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {

			User forgotPwdUser = new User();
			String userEmail = request.getParameter("userEmail");

			if (userEmail != null && userEmail.isEmpty() == false) {

				forgotPwdUser.setUserEmail(userEmail);

			}

			User user = userService.getUserBySearchParameter(forgotPwdUser);

			if (user.getUserId() != null) {
				logger.info("### gng to Forgot Pwd Encrypt Pwd is :"
						+ CustomerRegistrationController
								.base64EncryptPassword(user.getUserPassword()));
				logger.info("### gng to Forgot Pwd Decrypt Pwd is :"
						+ CustomerRegistrationController
								.base64DecryptPassword(user.getUserPassword()));

				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				EmailJob emailJob = new EmailJob();
				emailJob.setEmailType("FORGOT_PASSWORD_EMAIL");

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", user.getUserEmail());
				map.put("subject",
						properties_mail.getProperty("forgotpassword.subject"));
				map.put("firstName", user.getFirstName());
				map.put("userEmail", user.getUserEmail());
				map.put("userPassword", CustomerRegistrationController
						.base64DecryptPassword(user.getUserPassword()));
				map.put("templateName", "ForgotPassword.vm");
				map.put("message",
						"Forgot Password link sent to your registred email");
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);

				modelMap.put("success", true);
				return modelMap;

			} else {
				return getModelMapError("User not found");
			}
		} catch (Exception ex) {
			return getModelMapError("Login Failed");

		}

	}

	@RequestMapping(value = "/user/userId", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> userId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		Date todayDate = new Date();

		String id_value = "";
		id_value = request.getParameter("userId");
		try {
			User user = new User();
			Role role = new Role();
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById((Integer.parseInt(id_value)));

			} else {
				return getModelMapError("Login Failed");
			}

			return getModelMapUser(user, role);

		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;

		}
	}

	// Save Service
	@RequestMapping(value = "/user/saveUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveUser(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			User user = new User();
			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById((Integer.parseInt(id_value)));
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);

			} else {
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
			}

			if (StringUtils.isNotBlank(request.getParameter("firstName"))) {
				user.setFirstName(request.getParameter("firstName"));
			}

			if (StringUtils.isNotBlank(request.getParameter("lastName"))) {
				user.setLastName(request.getParameter("lastName"));
			}

			if (StringUtils.isNotBlank(request.getParameter("userEmail"))) {
				user.setUserEmail(request.getParameter("userEmail"));
			}

			if (StringUtils.isNotBlank(request.getParameter("userPassword"))) {
				user.setUserPassword(request.getParameter("userPassword"));
			}

			if (StringUtils.isNotBlank(request.getParameter("isActive"))) {
				user.setIsActive(request.getParameter("isActive"));
			}

			if (StringUtils.isNotBlank(request.getParameter("createdBy"))) {
				user.setCreatedBy(Integer.parseInt(request
						.getParameter("createdBy")));
			}

			if (StringUtils.isNotBlank(request.getParameter("updatedBy"))) {
				user.setUpdatedBy(Integer.parseInt(request
						.getParameter("updatedBy")));
			}

			if (StringUtils.isNotBlank(request.getParameter("organization"))) {
				String organizationId_str = request
						.getParameter("organization");
				Organization organization = new Organization();
				organization = organizationService.getOrganizationById((Integer
						.parseInt(organizationId_str)));
				user.setOrganization(organization);
			}

			// save profile image.
			if (file.getSize() > 0) {
				user.setProfileImgData(file.getBytes());
			}

			userService.saveUser(user);

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

	// Save Service
	@RequestMapping(value = "/user/saveChangeSkin", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveChangeSkin(HttpServletRequest request) {
		logger.info("saveChangeSkin Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			User user = new User();
			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {
				id_value = request.getParameter("userId");
				user = userService.getUserById((Integer.parseInt(id_value)));
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);

			} else {
				user.setUpdatedOn(todayDate);
				user.setCreatedOn(todayDate);
			}

			String colorSelected = request.getParameter("colorSkin");
			if (StringUtils.isNotBlank(request.getParameter("colorSkin"))) {
				if (colorSelected == "default"
						|| colorSelected.equalsIgnoreCase("default")) {
					user.setColorSkin("default");
				} else if (colorSelected == "navyblue"
						|| colorSelected.equalsIgnoreCase("navyblue")) {
					user.setColorSkin("navyblue");
				} else if (colorSelected == "palegreen"
						|| colorSelected.equalsIgnoreCase("palegreen")) {
					user.setColorSkin("palegreen");
				} else if (colorSelected == "red"
						|| colorSelected.equalsIgnoreCase("red")) {
					user.setColorSkin("red");
				} else if (colorSelected == "green"
						|| colorSelected.equalsIgnoreCase("green")) {
					user.setColorSkin("green");
				} else if (colorSelected == "brown"
						|| colorSelected.equalsIgnoreCase("brown")) {
					user.setColorSkin("brown");
				}

			}

			User savedUser = userService.saveUserObjWithReturn(user);
			Role role = new Role();
			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return getModelMapUser(savedUser, role);
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
	}

	// Delete Service
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/user/deleteUser", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteUser(HttpServletRequest request) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int userId = Integer.parseInt(request.getParameter("userId"));
		try {
			User user = userService.getUserById(userId);
			userService.removeUser(user);
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

	// Delete LoggedIn User Token Id
	@RequestMapping(value = "/user/logoutUser", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> logoutUser(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String userId = request.getParameter("userId");
		if (userId != null) {
			logger.info("User Id" + Integer.parseInt(userId));
		}

		try {
			UserToken userToken = new UserToken();

			String loggedInUserToken = request.getParameter("userToken");

			if (loggedInUserToken != null) {

				String decodedToken = userToken
						.getDecodedToken(loggedInUserToken);
				String[] tokenParams = decodedToken.split(":");
				userToken.setUserTokenId(Integer.parseInt(tokenParams[0]));
				userTokenservice.removeUserToken(userToken);

				logger.info("Delete Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Deleted Successfully");
				return modelMap;
			} else {

				modelMap.put("success", false);
				modelMap.put("message", "Error in deletion");
				return modelMap;

			}

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapUserRoleList(List<UserRole> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(UserRole.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof UserRole)) {
							return new JSONObject(true);
						}
						String DATE_FORMAT = "MM/dd/yyyy";
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

						UserRole userRole = (UserRole) bean;

						Integer userId = new Integer(0);
						String firstName = "";
						String lastName = "";
						String userEmail = "";
						String isActive = "";
						Integer createdBy = new Integer(0);
						Integer updatedBy = new Integer(0);
						String createdOn = "";
						String updatedOn = "";
						String userPassword = "";
						String organization = "";
						String phone = "";
						if (userRole.getUser() != null) {
							userId = userRole.getUser().getUserId();
							firstName = userRole.getUser().getFirstName();
							lastName = userRole.getUser().getLastName();
							userEmail = userRole.getUser().getUserEmail();
							isActive = userRole.getUser().getIsActive();
							userPassword = userRole.getUser().getUserPassword();
							createdBy = userRole.getUser().getCreatedBy();
							updatedBy = userRole.getUser().getUpdatedBy();
							organization = userRole.getUser().getOrganization()
									.getOrganizationName();
							phone = userRole.getUser().getOrganization()
									.getPhone();
							if (userRole.getUser().getCreatedOn() != null) {
								createdOn = sdf.format(userRole.getUser()
										.getCreatedOn());
							}
							if (userRole.getUser().getUpdatedOn() != null) {
								updatedOn = sdf.format(userRole.getUser()
										.getUpdatedOn());
							}

						}

						return new JSONObject()
								.element("userId", userId)
								.element("firstName", firstName)
								.element("lastName", lastName)
								.element("userEmail", userEmail)
								.element("userPassword", userPassword)
								.element("isActive", isActive)
								.element("createdBy", createdBy)
								.element("createdOn", createdOn)
								.element("updatedBy", updatedBy)
								.element("updatedOn", updatedOn)
								.element(
										"organization",
										userRole.getUser().getOrganization()
												.getOrganizationName())
								.element(
										"phone",
										userRole.getUser().getOrganization()
												.getPhone())

						;
					}
				});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction

	/*
	 * Common json methds
	 */

	private Map<String, Object> getModelMapUser(User user, Role role) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		final String createdOn = formatter.format(user.getCreatedOn());
		final String updatedOn = formatter.format(user.getUpdatedOn());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(User.class,
				new JsonBeanProcessor() {
					@SuppressWarnings("restriction")
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof User)) {
							return new JSONObject(true);
						}

						User user = (User) bean;

						String profileImgStr = "";
						if (user.getProfileImgData() != null) {

							profileImgStr = new sun.misc.BASE64Encoder()
									.encode(user.getProfileImgData());

						}
						logger.info("####### Encrypted PAssword JsoN after Login is :"
								+ CustomerRegistrationController
										.base64DecryptPassword(user
												.getUserPassword()));

						return new JSONObject()
								.element("userId", user.getUserId())
								.element("firstName", user.getFirstName())
								.element("lastName", user.getLastName())
								.element("userEmail", user.getUserEmail())
								.element(
										"userPassword",
										CustomerRegistrationController
												.base64DecryptPassword(user
														.getUserPassword()))
								// .element("userPassword",
								// user.getUserPassword())
								.element(
										"isActive",
										user.getIsActiveString(user
												.getIsActive()))
								.element("createdBy", user.getCreatedBy())
								.element("createdOn", createdOn)
								.element("updatedBy", user.getUpdatedBy())
								.element("updatedOn", updatedOn)
								.element("userToken",
										generateToken(user.getUserId()))
								.element(
										"organizationName",
										user.getOrganization()
												.getOrganizationName())
								.element(
										"address",
										user.getOrgAddressString(user
												.getOrganization().getAddress()))
								.element("profileImagData", profileImgStr)
								.element("colorSkin", user.getColorSkin())

						;
					}
				});

		JSON json = JSONSerializer.toJSON(user, jsonConfig);

		JsonConfig jsonConfig1 = new JsonConfig();
		jsonConfig1.registerJsonBeanProcessor(Role.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Role)) {
							return new JSONObject(true);
						}

						Role role = (Role) bean;

						return new JSONObject()
								.element("roleId", role.getRoleId())
								.element("roleName", role.getRoleName())
								.element("roleDescription",
										role.getRoleDescription())

						;
					}
				});
		JSON json1 = JSONSerializer.toJSON(role, jsonConfig1);

		modelMap.put("data", json);
		modelMap.put("data_Role", json1);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapUserProfile(User user) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(User.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof User)) {
							return new JSONObject(true);
						}

						User user = (User) bean;

						return new JSONObject()
								.element("userId", user.getUserId())
								.element("firstName", user.getFirstName())
								.element("lastName", user.getLastName())
								.element("userEmail", user.getUserEmail())
								.element("userPassword", user.getUserPassword())
								.element("isActive", user.getUserPassword())
								.element("createdBy", user.getCreatedBy())
								.element("createdOn", user.getCreatedOn())
								.element("updatedBy", user.getUpdatedBy())
								.element("updatedOn", user.getUpdatedOn())
								.element("organization", user.getOrganization())

						;
					}
				});

		JSON json = JSONSerializer.toJSON(user, jsonConfig);

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

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

	private String generateToken(Integer userId) {

		return userTokenservice.createEncodedToken(userId);

	}

	private static byte[] convertObjectToByteArray(Object obj)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(obj);
		return byteArrayOutputStream.toByteArray();
	}
}
