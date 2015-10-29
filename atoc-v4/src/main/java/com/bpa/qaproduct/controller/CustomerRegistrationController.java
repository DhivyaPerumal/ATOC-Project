package com.bpa.qaproduct.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.internet.MimeMessage;
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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.service.CustomerRegistrationService;
import com.bpa.qaproduct.service.JavaMailSender;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.RoleService;
import com.bpa.qaproduct.service.UserRoleService;
import com.bpa.qaproduct.service.UserService;
import com.bpa.qaproduct.util.DataAccessLayerException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Controller
public class CustomerRegistrationController {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	@Autowired
	private MailService mailService;

	protected final Log logger = LogFactory
			.getLog(CustomerRegistrationController.class);

	@Autowired
	private VelocityEngine velocityEngine;

	// List Service

	@RequestMapping(value = "/customer/viewCustomerList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewCustomerList(HttpServletRequest request) {
		// logger.info("******In viewCustomerList****");

		try {

			String id_value = "";
			id_value = request.getParameter("userId");
			logger.info("Getting User Object based on Id :" + id_value);
			// Constructing CustomerRegistration Search Object
			CustomerRegistration customerRegistration = new CustomerRegistration();

			String searchUserName = request.getParameter("searchUserName");
			if (searchUserName != null && searchUserName.isEmpty() == false) {
				customerRegistration.setFirstname(searchUserName);
			}
			customerRegistration.setApprovalStatus("PENDING"); // NEED TO CHECK
																// WITH TEAM

			int totalResults = customerRegistrationService
					.getCustomerRegistrationFilterCount(customerRegistration);
			List<CustomerRegistration> list = customerRegistrationService
					.getAllCustomerRegistrationList(customerRegistration);

			// logger.info("Returned list size" + list.size());

			return getModelMapUserList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}

	}

	// Pagination Service
	@RequestMapping(value = "/customer/viewCustomerListPagination", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewCustomerListPagination(HttpServletRequest request) {
		int start = 0;
		int limit = 10;

		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));
		try {
			String id_value = "";
			id_value = request.getParameter("userId");
			logger.info("Getting User Object based on Id :" + id_value);
			CustomerRegistration customerRegistration = new CustomerRegistration();
			String searchUserName = request.getParameter("searchUserName");
			if (searchUserName != null && searchUserName.isEmpty() == false) {
				customerRegistration.setFirstname(searchUserName);
			}
			customerRegistration.setApprovalStatus("PENDING"); // NEED TO CHECK
																// WITH TEAM

			int totalResults = customerRegistrationService
					.getCustomerRegistrationFilterCount(customerRegistration);
			List<CustomerRegistration> list = customerRegistrationService
					.getCustomerRegistrationListPagination(
							customerRegistration, start, limit);

			// logger.info("Returned list size" + list.size());

			return getModelMapUserListPagination(list, totalResults);

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}

	}

	// Get User Roles
	@RequestMapping(value = "/customer/getUserRoles", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getUserRoles(HttpServletRequest request) {
		logger.info("******In getUserRoles****");

		try {

			// Fetching userId

			User user = new User();
			UserRole userRole = new UserRole();

			String userId = request.getParameter("userId");
			if (request.getParameter("userId") != null
					|| request.getParameter("userId") == "") {
				user = userService.getUserById(Integer.parseInt("userId"));
				logger.info("user Id" + Integer.parseInt("userId"));
				userRole.setUser(user);
			}

			// Fetching RoleId
			Role role = new Role();

			String roleId = request.getParameter("roleId");
			if (request.getParameter("roleId") != null
					|| request.getParameter("roleId") == "") {
				role = roleService.getRoleById(Integer.parseInt("roleId"));
				logger.info("role Id is" + Integer.parseInt("roleId"));
				userRole.setRole(role);
			}

			List<UserRole> userRoleList = new ArrayList<UserRole>();

			return getModelMapUserRolesList(userRoleList, userRoleList.size());

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}

	}

	// Save Service
	@RequestMapping(value = "/customer/saveCustomer", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveCustomerRegistration(HttpServletRequest request,
			Object userEmail) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			CustomerRegistration customerRegistration = new CustomerRegistration();
			String id_value = "";
			id_value = request.getParameter("userId");

			// logger.info("Getting User Object based on Id :"+id_value);
			if ((StringUtils.isNotBlank(request
					.getParameter("customerRegistrationId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("customerRegistrationId")))) {
				id_value = request.getParameter("customerRegistrationId");
				customerRegistration = customerRegistrationService
						.getCustomerRegistrationById(Integer.parseInt(id_value));
				customerRegistration.setCreatedOn(getTodayDate());

			} else {
				customerRegistration.setCreatedOn(getTodayDate());

			}

			CustomerRegistration user = new CustomerRegistration();
			String contactEmail = request.getParameter("contactEmail");
			customerRegistration.setContactEmail(contactEmail);
			logger.info("contact emailid is:" + contactEmail);
			if (contactEmail != null) {
				customerRegistration.setContactEmail(contactEmail);
				user = customerRegistrationService
						.getCustomerRegistrationBySearchParameter(customerRegistration);
				if (user != null) {
					if (user.getApprovalStatus().equalsIgnoreCase("REJECTED")) {

					} else {
						// logger.info("user emailid is:" +user.getUserEmail());
						if (contactEmail == user.getContactEmail()
								|| contactEmail.equalsIgnoreCase(user
										.getContactEmail())) {
							logger.info("user already Exists **************:");
							modelMap.put("success", false);
							modelMap.put("customerRegistration",
									"userEmailExist");
							modelMap.put("message",
									"User already Registered with this Emailid");
							return modelMap;
						}
					}

				}

			}

			String firstname = request.getParameter("firstname");
			if (firstname != null) {
				customerRegistration.setFirstname(firstname);
			}

			String lastname = request.getParameter("lastname");
			if (lastname != null) {
				customerRegistration.setLastname(lastname);
			}

			String notes = request.getParameter("notes");
			if (notes != null) {
				customerRegistration.setNotes(notes);
			}

			String address = request.getParameter("address");
			if (address != null) {
				customerRegistration.setAddress(address);
			}

			String city = request.getParameter("city");
			if (city != null) {
				customerRegistration.setCity(city);
			}

			String state = request.getParameter("state");
			if (state != null) {
				customerRegistration.setState(state);
			}

			String country = request.getParameter("country");
			if (country != null) {
				customerRegistration.setCountry(country);
			}

			String zipCode = request.getParameter("zipCode");
			if (zipCode != null) {
				customerRegistration.setZipCode(zipCode);
			}

			String contactName = request.getParameter("contactName");
			if (contactName != null) {
				customerRegistration.setContactName(contactName);
			}

			String contactPhone = request.getParameter("contactPhone");
			if (contactPhone != null) {
				customerRegistration.setContactPhone(contactPhone);
			}

			String customerWebsite = request.getParameter("customerWebsite");
			if (customerWebsite != null) {
				customerRegistration.setCustomerWebsite(customerWebsite);
			}

			String organizationName = null;
			organizationName = request.getParameter("organizationName");
			if (organizationName != null) {
				customerRegistration.setOrganizationName(organizationName);
			}

			String isAdmin = null;
			isAdmin = request.getParameter("isAdmin");
			if (isAdmin.equalsIgnoreCase("yes") && isAdmin != null
					|| isAdmin == "") {
				customerRegistration.setIsAdmin(customerRegistration
						.setIsAdminString("yes"));
				logger.info("In Save Customer Reg ia Admin is :"
						+ customerRegistration.setIsAdminString("yes"));
			} else if (isAdmin.equalsIgnoreCase("no") && isAdmin != null
					|| isAdmin == "") {
				customerRegistration.setIsAdmin(customerRegistration
						.setIsAdminString("no"));
				logger.info("In Save Customer Reg ia Admin is :"
						+ customerRegistration.setIsAdminString("no"));
			} else {
				customerRegistration.setIsAdmin(customerRegistration
						.setIsAdminString("no"));
				logger.info("In Save Customer Reg ia Admin is :"
						+ customerRegistration.setIsAdminString(""));
			}

		
			
			String approvalStatus = request.getParameter("approvalStatus");
			customerRegistration.setApprovalStatus(approvalStatus);
			// boolean successFlag = false;
			try {
				customerRegistrationService
						.saveCustomerRegistration(customerRegistration);
				Properties properties_mail = new Properties();
				InputStream iStream_mail = null;
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				EmailJob emailJob =  new EmailJob();
				emailJob.setEmailType("REGISTRAIOTN REQUEST");
				
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", customerRegistration.getContactEmail());
				map.put("subject", "ATOC - Registration Success");
				map.put("firstName", customerRegistration.getFirstname());
				map.put("contactEmail", customerRegistration.getContactEmail());
				map.put("contactPhone", customerRegistration.getContactPhone());
				map.put("address", customerRegistration.getAddress());
				map.put("status", "PENDING");
				map.put("templateName", "CustomerRegistration.vm");
				map.put("content",
						"Your application is pending for approval. You will be notified of your status by email.");
				
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);
				
				// successFlag = true;
			} catch (DataAccessLayerException ex1) {
				Throwable t = ex1.getCause();
				while ((t != null)
						&& !(t instanceof MySQLIntegrityConstraintViolationException)) {
					t = t.getCause();
					logger.info("Message::::::::::::::::::::::::"
							+ t.getMessage());
					String exMessage = t.getMessage();
					if (exMessage.contains("CONTACT_EMAIL_UNIQUE")) {
						modelMap.put("success", false);
						modelMap.put("customerRegistration", "userEmailExist");
						modelMap.put("message",
								"User already Registered with this Emailid");
						return modelMap;

					}

				}

			}
			if (approvalStatus != null && approvalStatus == "PENDING"
					|| approvalStatus.equalsIgnoreCase("PENDING")) {
				
				
					
					
				
				
				/*map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", customerRegistration.getContactEmail());
				map.put("subject", "ATOC - Registration Success");
				map.put("firstName", customerRegistration.getFirstname());
				map.put("contactEmail", customerRegistration.getContactEmail());
				map.put("contactPhone", customerRegistration.getContactPhone());
				map.put("address", customerRegistration.getAddress());
				map.put("status", "PENDING");
				map.put("content",
						"Your application is pending for approval. You will be notified of your status by email.");
				mailService.sendRegApprovalEmail(map);*/

				customerRegistration.setApprovalStatus("PENDING");

			} else if (approvalStatus != null && approvalStatus == "REJECTED"
					|| approvalStatus.equalsIgnoreCase("REJECTED")) {
				customerRegistration.setApprovalStatus("REJECTED");
			} else if (approvalStatus != null && approvalStatus == "APPROVED"
					|| approvalStatus.equalsIgnoreCase("APPROVED")) {
				customerRegistration.setApprovalStatus("APPROVED");
			} else {
				customerRegistration.setApprovalStatus("PENDING");
				javaMailSender
						.sendMail(
								"kirankumar@bpatech.com",
								customerRegistration.getContactEmail(),
								"ATOC Send mail Test Subject",
								"Wait..! BPA Qa Product Send mail Test Body \n \n \n \t You records have been in PENDING state to approve your records ");
			}

			// logger.info("Insert Method Executed.,");
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

	// Update Service

	@RequestMapping(value = "/customer/updateCustomer", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> updateCustomer(HttpServletRequest request)

	{
		// logger.info("Update Method Started");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Properties properties_mail = new Properties();
			InputStream iStream_mail = null;
			String propFileName_mail = "/properties/mail.properties";
			InputStream stream_mail = getClass().getClassLoader()
					.getResourceAsStream(propFileName_mail);
			properties_mail.load(stream_mail);

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			CustomerRegistration customerRegistration = new CustomerRegistration();
			String id_value1;
			id_value1 = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value1);
			if ((StringUtils.isNotBlank(request
					.getParameter("customerRegistrationId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("customerRegistrationId")))) {
				id_value1 = request.getParameter("customerRegistrationId");
				customerRegistration = customerRegistrationService
						.getCustomerRegistrationById(Integer
								.parseInt(id_value1));
				customerRegistration.setCreatedOn(todayDate);
			} else {
				customerRegistration.setCreatedOn(todayDate);
			}

			String approvalStatus = request.getParameter("approvalStatus");
			if (approvalStatus != null && approvalStatus == "PENDING"
					|| approvalStatus.equalsIgnoreCase("PENDING")) {
				customerRegistration.setApprovalStatus(approvalStatus);
			} else if (approvalStatus != null && approvalStatus == "REJECTED"
					|| approvalStatus.equalsIgnoreCase("REJECTED")) {
				customerRegistration.setApprovalStatus("REJECTED");
				String notes = request.getParameter("notes");
				customerRegistration.setNotes(notes);
				Map<String, Object> map = new HashMap<String, Object>();
				
				EmailJob emailJob =  new EmailJob();
				emailJob.setEmailType("REGISTRAIOTN_REQUEST_REJECT");
				
			map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", customerRegistration.getContactEmail());
				map.put("subject", "ATOC - Registration Rejected");
				map.put("firstName", customerRegistration.getFirstname());
				map.put("contactEmail", customerRegistration.getContactEmail());
				map.put("contactPhone", customerRegistration.getContactPhone());
				map.put("address", customerRegistration.getAddress());
				
				map.put("status", "REJECTED");
				map.put("templateName", "CustomerRegistration.vm");
				map.put("content",
						"Sorry to inform you that your registration has been rejected due to "
								+ notes + "");
				
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);
				
				

			} else if (approvalStatus != null && approvalStatus == "APPROVED"
					|| approvalStatus.equalsIgnoreCase("APPROVED")) {
				customerRegistration.setApprovalStatus("APPROVED");
				logger.info(":::::::::::::::: In Before Mail Sending");
				EmailJob emailJob =  new EmailJob();
				emailJob.setEmailType("REGISTRAIOTN_REQUEST_APPROVE");
		
					
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", customerRegistration.getContactEmail());
				map.put("subject", "ATOC - Registration Approved");
				map.put("firstName", customerRegistration.getFirstname());
				map.put("contactEmail", customerRegistration.getContactEmail());
				map.put("contactPhone", customerRegistration.getContactPhone());
				map.put("address", customerRegistration.getAddress());
				map.put("status", "APPROVED");
				map.put("templateName", "CustomerRegistration.vm");
				map.put("content",
						"Your ATOC account has been verfied and activated. You will be receiving access credentials shortly.");
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);
			
				

				logger.info("::::::::::::::::: In After Mail Sending");
			} else {
				customerRegistration.setApprovalStatus("PENDING");
			}

			logger.info("After size");

			customerRegistration = customerRegistrationService
					.saveOrupdateCustomerRegistrationWithReturn(customerRegistration);
			if (customerRegistration.getApprovalStatus().equalsIgnoreCase(
					"APPROVED")) {
				logger.info("In update Customer Reg after approved n saved in db  :");
				String scheme = request.getScheme();
				String serverName = request.getServerName();
				int port = request.getServerPort();
				String contextpath = request.getContextPath();
				String appURL = scheme + "://" + serverName + ":" + port
						+ contextpath;
				logger.info("App url is :" + appURL);

				updateOrganization(customerRegistration, appURL);
			}

			// logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;

		}
	}

	// Delete Service
	@RequestMapping(value = "/customer/deleteCustomer", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteCustomer(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		String customerId_str = request.getParameter("customerRegistrationId");
		int customerRegistrationId = Integer.parseInt(customerId_str);
		try {
			CustomerRegistration customerRegistration = customerRegistrationService
					.getCustomerRegistrationById(customerRegistrationId);
			customerRegistrationService
					.removeCustomerRegistration(customerRegistration);
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

	@RequestMapping(value = "/customer/getCustomerInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> customerRegistrationId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>(2);

		String customerRegistrationId_str = request
				.getParameter("customerRegistrationId");
		CustomerRegistration customerRegistration = new CustomerRegistration();
		try {
			if (customerRegistrationId_str != null) {
				customerRegistration = customerRegistrationService
						.getCustomerRegistrationById(Integer
								.parseInt(customerRegistrationId_str));
			} else {
				return getModelMapError("Failed to Load Data");
			}
			return getCustomerInfo(customerRegistration);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in loading data";
			map.put("success", false);
			map.put("message", msg);
			return map;
		}
	}

	// JSon Construction
	private Map<String, Object> getCustomerInfo(
			CustomerRegistration cRegistration) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(CustomerRegistration.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof CustomerRegistration)) {
							return new JSONObject(true);
						}

						CustomerRegistration cRegistration = (CustomerRegistration) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String createdOnString = "";
						if (cRegistration.getCreatedOn() != null) {
							createdOnString = importDateFormat
									.format(cRegistration.getCreatedOn());
						}

						return new JSONObject()
								.element(
										"customerRegistrationId",
										cRegistration
												.getCustomerRegistrationId())
								.element("firstname",
										cRegistration.getFirstname())
								.element("lastname",
										cRegistration.getLastname())
								.element("notes", cRegistration.getNotes())
								.element("address", cRegistration.getAddress())
								.element("city", cRegistration.getCity())
								.element("state", cRegistration.getState())
								.element("country", cRegistration.getCountry())
								.element("zipCode", cRegistration.getZipCode())
								.element("contactName",
										cRegistration.getContactName())
								.element("contactEmail",
										cRegistration.getContactEmail())
								.element("contactPhone",
										cRegistration.getContactPhone())
								.element("organizationName",
										cRegistration.getOrganizationName())
								.element("customerWebsite",
										cRegistration.getCustomerWebsite())
								.element("createdOn", createdOnString)
								.element("approvalStatus",
										cRegistration.getApprovalStatus())

						;
					}
				});

		JSON json = JSONSerializer.toJSON(cRegistration, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction for User Roles
	private Map<String, Object> getModelMapUserRolesList(List<UserRole> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(UserRole.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof UserRole)) {
							return new JSONObject(true);
						}

						UserRole userRole = (UserRole) bean;
						return new JSONObject().element("user",
								userRole.getUser()).element("role",
								userRole.getRole())

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
	private Map<String, Object> getModelMapUserList(
			List<CustomerRegistration> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(CustomerRegistration.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof CustomerRegistration)) {
							return new JSONObject(true);
						}

						CustomerRegistration cRegistration = (CustomerRegistration) bean;
						return new JSONObject()
								.element(
										"customerRegistrationId",
										cRegistration
												.getCustomerRegistrationId())
								.element("firstname",
										cRegistration.getFirstname())
								.element("lastname",
										cRegistration.getLastname())
								.element("notes", cRegistration.getNotes())
								.element("address", cRegistration.getAddress())
								.element("city", cRegistration.getCity())
								.element("state", cRegistration.getState())
								.element("country", cRegistration.getCountry())
								.element("zipCode", cRegistration.getZipCode())
								.element("contactName",
										cRegistration.getContactName())
								.element("contactEmail",
										cRegistration.getContactEmail())
								.element("contactPhone",
										cRegistration.getContactPhone())
								.element("organizationName",
										cRegistration.getOrganizationName())
								.element("customerWebsite",
										cRegistration.getCustomerWebsite())
								.element("createdOn",
										cRegistration.getCreatedOn())
								.element("approvalStatus",
										cRegistration.getApprovalStatus())

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
	private Map<String, Object> getModelMapUserListPagination(
			List<CustomerRegistration> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(CustomerRegistration.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof CustomerRegistration)) {
							return new JSONObject(true);
						}

						CustomerRegistration cRegistration = (CustomerRegistration) bean;
						return new JSONObject()
								.element(
										"customerRegistrationId",
										cRegistration
												.getCustomerRegistrationId())
								.element("firstname",
										cRegistration.getFirstname())
								.element("lastname",
										cRegistration.getLastname())
								.element("notes", cRegistration.getNotes())
								.element("address", cRegistration.getAddress())
								.element("city", cRegistration.getCity())
								.element("state", cRegistration.getState())
								.element("country", cRegistration.getCountry())
								.element("zipCode", cRegistration.getZipCode())
								.element("contactName",
										cRegistration.getContactName())
								.element("contactEmail",
										cRegistration.getContactEmail())
								.element("contactPhone",
										cRegistration.getContactPhone())
								.element("organizationName",
										cRegistration.getOrganizationName())
								.element("customerWebsite",
										cRegistration.getCustomerWebsite())
								.element("createdOn",
										cRegistration.getCreatedOn())
								.element("approvalStatus",
										cRegistration.getApprovalStatus())

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

	private boolean updateOrganization(
			CustomerRegistration customerRegistration, String appURL)
			throws ParseException {
		boolean returnStatus = false;
		Organization organization = null;

		long currentDateTime = System.currentTimeMillis();
		if (customerRegistration.getOrganizationName() != null) {
			organization = new Organization();
			organization.setOrganizationName(customerRegistration
					.getOrganizationName());
			organization = organizationService
					.getOrganizationBySearchParam(organization);
		}
		if (organization == null) {
			organization = new Organization();
			if (organization.getOrganizationName() == null
					|| organization.getOrganizationName()
							.equalsIgnoreCase(null)) {
				organization = createOrganization(
						customerRegistration.getOrganizationName(),
						customerRegistration);
			}
		}

		// user1.setOrganization(organization);
		// user1.setOrganization(organization);
		User user = new User();
		user.setOrganization(organization);
		user.setFirstName(customerRegistration.getFirstname());
		user.setLastName(customerRegistration.getLastname());
		user.setUserEmail(customerRegistration.getContactEmail());
		user.setUserPassword(base64EncryptPassword(generateRandomPassword())); // for encrypted password
		//user.setUserPassword(generateRandomPassword());// No password encryption
		logger.info("####### Encrypted PAssword Saving in DB is :"+user.getUserPassword());
		user.setCreatedOn(customerRegistration.getCreatedOn());
		user.setCreatedBy(1);
		user.setUpdatedBy(1);
		user.setUpdatedOn(getTodayDate());
		user.setIsActive("y");

		User savedUser = userService.saveUserObjWithReturn(user);
		String isAdmin = customerRegistration
				.getIsAdminString(customerRegistration.getIsAdmin());
		logger.info("is Admin in after user insertion " + isAdmin);
		Role role = new Role();
		role.setRoleName(customerRegistration.setIsAdminString(isAdmin));
		role = roleService.getRoleBySearchParam(role);
		if (savedUser != null && role != null) {
			UserRole userRole = new UserRole();
			userRole.setUser(savedUser);
			userRole.setRole(role);
			userRole = userRoleService.saveUserRoleWithReturn(userRole);
			logger.info("After insertion in User Role "
					+ userRole.getUser().getUserId());
		}

		try {
			Properties properties_mail = new Properties();
			InputStream iStream_mail = null;
			String propFileName_mail = "/properties/mail.properties";
			InputStream stream_mail = getClass().getClassLoader()
					.getResourceAsStream(propFileName_mail);
			properties_mail.load(stream_mail);
			EmailJob emailJob = new EmailJob();
			emailJob.setEmailType("LOGIN_CREDENTIALS");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("from",
					properties_mail.getProperty("javaMailSender.username"));
			map.put("to", savedUser.getUserEmail());
			map.put("subject", "ATOC - Login Credentials");
			map.put("firstName", user.getFirstName());
			map.put("userEmail", user.getUserEmail());
			map.put("userPassword", base64DecryptPassword(user.getUserPassword())); // sending Encrypted password in Mail
			//map.put("userPassword", user.getUserPassword()); // sending pwd with out encryption in Mail
			
			logger.info("####### Encrypted Password Going Mail is :"+ base64DecryptPassword(user.getUserPassword()));
			
			map.put("organizationName", user.getOrganization()
					.getOrganizationName());
			map.put("appURL", appURL);
			map.put("templateName", "UserDetails.vm");
			map.put("heading",
					"Thank You for Registering with us .!! Here is your Login credentials.");
			map.put("content",
					"Your ATOC account has been verfied and activated. You will be receiving in across application access credentials.");
		
			byte[] byteArray = null;
			byteArray = convertObjectToByteArray(map);
			emailJob.setJobDataByteArray(byteArray);
			userService.saveOrUpdateEmailJob(emailJob);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnStatus = true;
		return returnStatus;

	}
	public static String base64EncryptPassword(String token) {
		byte[] encodedBytes = Base64.encode(token.getBytes());
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

	public static String base64DecryptPassword(String token) {
		byte[] decodedBytes = Base64.decode(token.getBytes());
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}
	
	private Organization createOrganization(String organizationName,
			CustomerRegistration customerRegistration) {

		Organization newOrganiztion = new Organization();
		newOrganiztion.setCreatedBy(1);
		newOrganiztion.setUpdatedBy(1);
		newOrganiztion.setIsActive("y");
		newOrganiztion.setCity(customerRegistration.getCity());
		newOrganiztion.setPhone(customerRegistration.getContactPhone());
		newOrganiztion.setEmail(customerRegistration.getContactEmail());
		newOrganiztion.setNotes(customerRegistration.getNotes());
		newOrganiztion.setAddress(customerRegistration.getAddress());
		newOrganiztion.setState(customerRegistration.getState());
		newOrganiztion.setCountry(customerRegistration.getCountry());
		newOrganiztion.setZipCode(customerRegistration.getZipCode());

		try {
			newOrganiztion.setCreatedOn(getTodayDate());
			newOrganiztion.setUpdatedOn(getTodayDate());
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		newOrganiztion.setOrganizationName(organizationName);
		organizationService.saveOrganization(newOrganiztion);
		return newOrganiztion;
	}

	// Generate Password Dynamically Method

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	public static String generateRandomPassword() {

		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@#$%&";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	private Date getTodayDate() throws ParseException {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate
				.getTime()));
		return todayDate;
	}
	private static byte[] convertObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
}

}
