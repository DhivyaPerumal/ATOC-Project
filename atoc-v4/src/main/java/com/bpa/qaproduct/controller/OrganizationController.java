package com.bpa.qaproduct.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bpa.qaproduct.entity.ApplicationKey;
import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Organization;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.ApplicationKeyService;
import com.bpa.qaproduct.service.CustomerRegistrationService;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.UserRoleService;
import com.bpa.qaproduct.service.UserService;
import com.bpa.qaproduct.service.UserTokenService;
import com.bpa.qaproduct.util.DataAccessLayerException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Controller
public class OrganizationController {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ApplicationKeyService applicationKeyService;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private JavaMailSenderImpl javamailsenderImpl;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;
	protected final Log logger = LogFactory
			.getLog(OrganizationController.class);

	// List Service

	@RequestMapping(value = "/organization/viewAllOrganizationList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewAllOrganizationList(HttpServletRequest request) {

		try {

			// Constructing Organization Search Object
			Organization searchOrganization = new Organization();

			String searchOrganizationName = request
					.getParameter("searchOrganizationName");
			if (searchOrganizationName != null
					&& searchOrganizationName.isEmpty() == false) {
				searchOrganization.setOrganizationName(searchOrganizationName);
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {

				id_value = request.getParameter("userId");
				logger.info("Getting User Object based on Id :" + id_value);

			}
			int totalResults = organizationService
					.getOrganizationFilterCount(searchOrganization);
			List<Organization> list = organizationService
					.getAllOrganizationList(searchOrganization);

			// logger.info("Returned list size" + list.size());

			return getModelMapOrganizationList(list, totalResults);

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/organization/viewOrganizationList", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> viewOrganizationList(HttpServletRequest request) {

		int start = 0;
		int limit = 10;

		start = Integer.parseInt(request.getParameter("iDisplayStart"));
		limit = Integer.parseInt(request.getParameter("iDisplayLength"));

		try {

			// Constructing Organization Search Object
			Organization searchOrganization = new Organization();

			String searchOrganizationName = request
					.getParameter("searchOrganizationName");
			if (searchOrganizationName != null
					&& searchOrganizationName.isEmpty() == false) {
				searchOrganization.setOrganizationName(searchOrganizationName);
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {

				id_value = request.getParameter("userId");
				logger.info("Getting User Object based on Id :" + id_value);

			}

			int totalResults = organizationService
					.getOrganizationFilterCount(searchOrganization);
			List<Organization> list = organizationService.getOrganizationList(
					searchOrganization, start, limit);

			// logger.info("Returned list size" + list.size());

			return getModelMapOrganizationList(list, totalResults);

		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	// Save Service
	@RequestMapping(value = "/organization/saveOrganization", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveOrganization(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Organization organization = new Organization();
			String id_value = "";
			id_value = request.getParameter("userId");

			logger.info("Getting User Object based on Id :" + id_value);
			if ((StringUtils.isNotBlank(request.getParameter("organizationId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("organizationId")))) {
				id_value = request.getParameter("organizationId");
				organization = organizationService.getOrganizationById(Integer
						.parseInt(id_value));
				organization.setCreatedOn(todayDate);
				organization.setUpdatedOn(todayDate);

			} else {
				// organization.setCreatedBy(1); // need to change after getting
				// user from session
				organization.setCreatedOn(todayDate);
				organization.setUpdatedOn(todayDate);

			}

			String email = request.getParameter("email");
			if (email != null) {
				organization.setEmail(email);
				organization = organizationService
						.getOrganizationBySearchParam(organization);
				logger.info("Org Email is" + organization.getEmail());

				if (email == organization.getEmail()
						|| email.equalsIgnoreCase(organization.getEmail())) {
					logger.info("Organization Email Already Exists");
					modelMap.put("success", false);
					modelMap.put("message",
							"Organization Email name already exists");
					return modelMap;
				} else {

					organization.setCreatedOn(todayDate);

					organization.setUpdatedOn(todayDate);

				}

			}

			int applicationKeyId = 0;
			if ((StringUtils.isNotBlank(request
					.getParameter("applicationKeyId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("applicationKeyId")))
					&& request.getParameter("applicationKeyId") != null) {
				try {

					applicationKeyId = Integer.parseInt(request
							.getParameter("applicationKeyId"));
					ApplicationKey applicationKey = applicationKeyService
							.getApplicationKeyById(applicationKeyId);
					organization.setApplicationKey(applicationKey);

				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					applicationKeyId = organization.getApplicationKey()
							.getApplicationKeyId();

					organization.setApplicationKey(organization
							.getApplicationKey());

				}

			}
			User user = new User();
			String id_value1 = "";

			String organizationName = request.getParameter("organizationName");
			organization.setOrganizationName(organizationName);

			String phone = request.getParameter("phone");
			organization.setPhone(phone);

			// String email = request.getParameter("email");
			organization.setEmail(email);

			String fax = request.getParameter("fax");
			organization.setFax(fax);

			String notes = request.getParameter("notes");
			organization.setNotes(notes);

			String address = request.getParameter("address");
			organization.setAddress(address);

			String city = request.getParameter("city");
			organization.setCity(city);

			String state = request.getParameter("state");
			organization.setState(state);

			String country = request.getParameter("country");
			organization.setCountry(country);

			String zipCode = request.getParameter("zipCode");
			organization.setZipCode(zipCode);

			String isActive = request.getParameter("isActive");
			organization.setIsActive(isActive);

			String createdBy = request.getParameter("createdBy");
			organization.setCreatedBy(Integer.parseInt(createdBy));

			String updatedBy = request.getParameter("updatedBy");
			organization.setUpdatedBy(Integer.parseInt(updatedBy));

			String orgLogoFileName = request
					.getParameter("organizationLogoFileName");
			organization.setOrganizationLogoFileName(orgLogoFileName);

			organizationService.saveOrganization(organization);

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

	// Save Service
	@RequestMapping(value = "/organization/adminAddOrganization", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> adminAddOrganization(HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Organization organization = new Organization();
			String id_value = "";
			String orgName = "";
			String email = "";
			id_value = request.getParameter("userId");
			logger.info("Getting User Object based on Id :" + id_value);

			if ((StringUtils.isNotBlank(request.getParameter("email")))
					|| (StringUtils.isNotEmpty(request.getParameter("email")))) {
				email = request.getParameter("email");
				organization.setEmail(email);
			}
			if ((StringUtils.isNotBlank(request
					.getParameter("organizationName")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("organizationName")))) {
				orgName = request.getParameter("organizationName");
				organization.setOrganizationName(orgName);
			}

			String id_value1 = "";

			String organizationName = request.getParameter("organizationName");
			if (organizationName != null || organizationName != "") {
				organization.setOrganizationName(organizationName);
			}

			String phone = request.getParameter("phone");
			if (phone != null || phone != "") {
				organization.setPhone(phone);
			}

			// String email = request.getParameter("email");
			organization.setEmail(email);

			String fax = request.getParameter("fax");
			if (fax != null || fax != "") {
				organization.setFax(fax);
			}

			String notes = request.getParameter("notes");
			if (notes != null || notes != "") {
				organization.setNotes(notes);
			}

			String address = request.getParameter("address");
			if (address != null || address != "") {
				organization.setAddress(address);
			}

			String city = request.getParameter("city");
			if (city != null || city != "") {
				organization.setCity(city);
			}

			String state = request.getParameter("state");
			if (state != null || state != "") {
				organization.setState(state);
			}

			String country = request.getParameter("country");
			if (country != null || country != "") {
				organization.setCountry(country);
			}

			String zipCode = request.getParameter("zipCode");
			if (zipCode != null || zipCode != "") {
				organization.setZipCode(zipCode);
			}

			String isActive = request.getParameter("isActive");
			if (isActive != null || isActive != "") {
				organization.setIsActive(isActive);
			}

			String createdBy = request.getParameter("createdBy");
			if (createdBy != null || createdBy != "") {
				organization.setCreatedBy(Integer.parseInt(createdBy));
			}

			String updatedBy = request.getParameter("updatedBy");
			if (updatedBy != null || updatedBy != "") {
				organization.setUpdatedBy(Integer.parseInt(updatedBy));
			}

			String orgLogoFileName = request
					.getParameter("organizationLogoFileName");
			if (orgLogoFileName != null || orgLogoFileName != "") {
				organization.setOrganizationLogoFileName(orgLogoFileName);
			}

			try {
				organization = organizationService
						.saveOrganization(organization);
			} catch (DataAccessLayerException ex1) {
				Throwable t = ex1.getCause();
				while ((t != null)
						&& !(t instanceof MySQLIntegrityConstraintViolationException)) {
					t = t.getCause();
					logger.info("Message::::::::::::::::::::::::"
							+ t.getMessage());
					String exMessage = t.getMessage();
					if (exMessage.contains("ORGANIZATION_NAME_UNIQUE")) {
						modelMap.put("success", false);
						modelMap.put("addOrg", "orgNameExist");
						modelMap.put("message",
								"Organization Email name already exists");
						return modelMap;

					}
					if (exMessage.contains("EMAIL_UNIQUE")) {
						modelMap.put("success", false);
						modelMap.put("addOrg", "orgEmailExist");
						modelMap.put("message",
								"Organization Email name already exists");
						return modelMap;
					}

				}

			}

			logger.info("Inserted Org Obj Id is :"
					+ organization.getOrganizationId());
			if (organization.getOrganizationId() != null) {
				// Reading From EMail ID from Properties File
				EmailJob emailJob = new EmailJob();
				emailJob.setEmailType("ADD_NEW_ORGANIZATION");
				
				
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader()
						.getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from",
						properties_mail.getProperty("javaMailSender.username"));
				map.put("to", organization.getEmail());
				map.put("subject",
						properties_mail.getProperty("addorgbyadmin.subject"));
				map.put("organizationName", organization.getOrganizationName());
				map.put("templateName", "AddOrganization.vm");
				map.put("content",
						"You will receive access credentials shortly.");
				
				byte[] byteArray = null;
				byteArray = convertObjectToByteArray(map);
				emailJob.setJobDataByteArray(byteArray);
				userService.saveOrUpdateEmailJob(emailJob);

			}

			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		}

		catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	// Service to populate Organization based on Customer Registration
	@RequestMapping(value = "/organization/createOrganizationFromCustomerRegistration", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> createOrganizationFromCustomerRegistration(
			HttpServletRequest request) {
		// logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			Organization organization = new Organization();
			CustomerRegistration customerRegistration = new CustomerRegistration();
			String id_value = "";

			if ((StringUtils.isNotBlank(request
					.getParameter("customerRegistrationId")))
					|| (StringUtils.isNotEmpty(request
							.getParameter("customerRegistrationId")))) {
				id_value = request.getParameter("customerRegistrationId");
				customerRegistration = customerRegistrationService
						.getCustomerRegistrationById(Integer.parseInt(id_value));

				// Search for application key which was not assigned to any
				// customer.
				ApplicationKey searchApplicationKey = new ApplicationKey();
				searchApplicationKey.setAssignedStatus("Y");
				ApplicationKey applicationKey = (ApplicationKey) applicationKeyService
						.getAllApplicationKeyList(searchApplicationKey).get(0);
				organization.setApplicationKey(applicationKey);
				organization.setOrganizationName(customerRegistration
						.getOrganizationName());
				organization.setPhone(customerRegistration.getContactPhone());
				organization.setNotes(customerRegistration.getNotes());
				organization.setAddress(customerRegistration.getAddress());
				organization.setCity(customerRegistration.getCity());
				organization.setState(customerRegistration.getState());
				organization.setCountry(customerRegistration.getCountry());
				organization.setZipCode(customerRegistration.getZipCode());
				organization.setEmail(customerRegistration.getContactEmail());
				organization.setIsActive("Y");
				organizationService.saveOrganization(organization);

			}
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
	@RequestMapping(value = "/organization/deleteOrganization", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteOrganization(HttpServletRequest request) {

		// logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int organizationId = Integer.parseInt(request
				.getParameter("organizationId"));
		try {
			Organization organization = organizationService
					.getOrganizationById(organizationId);
			organizationService.removeCorganization(organization);
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

	@RequestMapping(value = "/organization/getOrganizationInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> testSuiteId(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>(2);

		String organizationId_str = request.getParameter("organizationId");
		Organization organization = new Organization();
		try {
			if (organizationId_str != null) {
				organization = organizationService.getOrganizationById(Integer
						.parseInt(organizationId_str));
			} else {
				return getModelMapError("Failed to Load Data");
			}
			return getModelMapOrganizationInfo(organization);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in loading data";
			map.put("success", false);
			map.put("message", msg);
			return map;
		}
	}

	// JSon Construction
	private Map<String, Object> getModelMapOrganizationInfo(
			Organization organization) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(TestSuite.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Organization)) {
							return new JSONObject(true);
						}

						Organization organization = (Organization) bean;

						SimpleDateFormat importDateFormat = new SimpleDateFormat(
								"MM/dd/yyyy");

						String createdOnString = "";
						if (organization.getCreatedOn() != null) {
							createdOnString = importDateFormat
									.format(organization.getCreatedOn());
						}
						String updatedOnString = "";
						if (organization.getUpdatedOn() != null) {
							updatedOnString = importDateFormat
									.format(organization.getUpdatedOn());
						}

						String isActiveString = "";
						if (organization.getIsActive() != null) {
							isActiveString = organization
									.getIsActiveString(organization
											.getIsActive());
						}

						String phoneString = "";
						if (organization.getPhone() != null) {
							phoneString = organization.getPhone();
						}

						String faxString = "";
						if (organization.getFax() != null) {
							faxString = organization.getFax();
						}

						return new JSONObject()
								.element("organizationId",
										organization.getOrganizationId())
								.element("organizationName",
										organization.getOrganizationName())
								.element("phone", phoneString)
								.element("address", organization.getAddress())
								.element("fax", faxString)
								.element("notes", organization.getNotes())
								.element("email", organization.getEmail())
								.element("city", organization.getCity())
								.element("state", organization.getState())
								.element("country", organization.getCountry())
								.element("zipCode", organization.getZipCode())
								.element("isActive", organization.getIsActive())
								.element("isActiveString", isActiveString)
								.element("createdOn", createdOnString)
								.element("updatedOn", updatedOnString)
								.element(
										"applicationKeyString",
										organization
												.getApplicationKeyString(organization));
					}
				});

		JSON json = JSONSerializer.toJSON(organization, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	// JSon Construction
	private Map<String, Object> getModelMapOrganizationList(
			List<Organization> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(Organization.class,
				new JsonBeanProcessor() {
					public JSONObject processBean(Object bean,
							JsonConfig jsonConfig) {
						if (!(bean instanceof Organization)) {
							return new JSONObject(true);
						}

						Organization organization = (Organization) bean;

						String isActiveString = "";
						if (organization.getIsActive() != null) {
							isActiveString = organization
									.getIsActiveString(organization
											.getIsActive());
						}

						String phoneString = "";
						if (organization.getPhone() != null) {
							phoneString = organization.getPhone();
						}

						String faxString = "";
						if (organization.getFax() != null) {
							faxString = organization.getFax();
						}

						return new JSONObject()
								.element("organizationId",
										organization.getOrganizationId())
								.element("organizationName",
										organization.getOrganizationName())
								.element("phone", phoneString)
								.element("address", organization.getAddress())
								.element("fax", faxString)
								.element("notes", organization.getNotes())
								.element("email", organization.getEmail())
								.element("city", organization.getCity())
								.element("state", organization.getState())
								.element("country", organization.getCountry())
								.element("zipCode", organization.getZipCode())
								.element("isActive", organization.getIsActive())
								.element("isActiveString", isActiveString)
								.element(
										"applicationKeyString",
										organization
												.getApplicationKeyString(organization));
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
	private static byte[] convertObjectToByteArray(Object obj)
			throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(obj);
		return byteArrayOutputStream.toByteArray();
	}

}
