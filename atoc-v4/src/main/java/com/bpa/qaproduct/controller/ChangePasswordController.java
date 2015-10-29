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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.OrganizationService;
import com.bpa.qaproduct.service.UserService;

@Controller
public class ChangePasswordController {

	@Autowired
	private UserService userService;

	protected final Log logger = LogFactory
			.getLog(ChangePasswordController.class);

	private HttpHeaders addAccessControllAllowOrigin() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return headers;
	}

	// Get Record Service

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> viewUserDetails(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		try {

			HttpHeaders headers = addAccessControllAllowOrigin();
			ResponseEntity<User> entity = new ResponseEntity<User>(headers,
					HttpStatus.OK);

			boolean isLoggedIn = false;

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));

			// Constructing User Search Object
			User user = new User();
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("userId")))
					|| (StringUtils.isNotEmpty(request.getParameter("userId")))) {

				id_value = request.getParameter("userId");
				logger.info("Getting User Object based on Id :" + id_value);

				user = userService.getUserById((Integer.parseInt(id_value)));
				user.setUpdatedOn(todayDate);
				user.setUpdatedBy(20);
			}

			String userPassword = request.getParameter("userPassword");
			String newPassword = request.getParameter("newPassword");
			if (CustomerRegistrationController.base64EncryptPassword(userPassword) == user.getUserPassword()
					|| CustomerRegistrationController.base64EncryptPassword(userPassword).equals(user.getUserPassword())) { // For Encrypted Password 
					
			/*if (userPassword == user.getUserPassword()
					|| userPassword.equals(user.getUserPassword())) {*/

				user.setUserPassword(CustomerRegistrationController.base64EncryptPassword(newPassword));
				//user.setUserPassword(newPassword); 

				if (userPassword != null && newPassword != null) {

					user.setUserPassword(CustomerRegistrationController.base64EncryptPassword(newPassword));
					//user.setUserPassword(newPassword);
					logger.info("Db Password after changes done :"
							+ user.getUserPassword());
				}
			
				userService.saveUser(user);
				logger.info("Insert Method Executed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Saved Successfully");
				return modelMap;
			} else {
				// logger.info("Insert Method Not Executed.,");
				String msg = "Error while changing password";
				return getModelMapError(msg);
			}

		} catch (Exception ex) {
			String msg = "Sorry Password doesn't match";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
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
