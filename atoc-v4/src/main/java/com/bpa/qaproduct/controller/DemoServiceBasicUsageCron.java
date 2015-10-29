package com.bpa.qaproduct.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

import com.bpa.qaproduct.entity.CustomerRegistration;
import com.bpa.qaproduct.entity.Role;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserRole;
import com.bpa.qaproduct.service.CustomerRegistrationService;
import com.bpa.qaproduct.service.JavaMailSender;
import com.bpa.qaproduct.service.RoleService;
import com.bpa.qaproduct.service.UserRoleService;
import com.bpa.qaproduct.service.UserService;

public class DemoServiceBasicUsageCron {

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	/*
	 * @Autowired private DemoServiceCrone demoServiceCrone;
	 */

	protected final Log logger = LogFactory
			.getLog(DemoServiceBasicUsageCron.class);

	@Scheduled(cron = "0 15 10 * * ? *")
	public void demoServiceMethod() {

		try {
			CustomerRegistration customerRegistration = new CustomerRegistration();

			Role role = new Role();
			UserRole searchUserRole = new UserRole();
			
			System.out
					.println("Method executed at every 5 seconds. Current time is :: "
							+ new Date());
			List<CustomerRegistration> listStatus = new ArrayList<CustomerRegistration>();

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter
					.format(currentDate.getTime()));
			System.out.println("Todat Date" + todayDate);
			customerRegistration.setApprovalStatus("PENDING");
			List<CustomerRegistration> list = customerRegistrationService
					.getAllCustomerRegistrationList(customerRegistration);

			logger.info("Pending list size" + list.size());
			

			for (CustomerRegistration pending : list) {
				Date d = pending.getCreatedOn();
				logger.info("Created On date" + d);
				boolean status = d.before(todayDate);

				logger.info("Comparable date" + status);
				if (status == true) {

					listStatus.add(pending);

					logger.info("date in boolean is true" + status);
				}

				logger.info(pending);

			}
			logger.info("List status One dat before is :" + listStatus);
			role.setRoleName("Master Admin");

			role = roleService.getRoleBySearchParam(role);
			searchUserRole.setRole(role);
			
			List<UserRole> userRoleList = userRoleService.getAllUserRoleList(searchUserRole);
			
			for (UserRole userRole : userRoleList)
			{
				User user = userRole.getUser();
				String userEmail = user.getUserEmail();
				logger.info("User" + user.getUserEmail());
				if ((StringUtils.isNotBlank(userEmail))
						|| (StringUtils.isNotEmpty(userEmail))) {
					MimeMessage mimeMessage = this.javaMailSenderImpl
							.createMimeMessage();
					MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
							mimeMessage, true);
					mimeMessageHelper.setFrom("kirankumar@bpatech.com");
					mimeMessageHelper.setTo(userEmail);
					mimeMessageHelper
							.setSubject("BPA Qa Product Send mail Test Subject");
					String mailBody = "Hi Status is pending for users check it";
					mimeMessageHelper.setText(mailBody, true);
					this.javaMailSenderImpl.send(mimeMessage);
					
				}
			}
			

			

		}

		catch (Exception e) {
			e.printStackTrace();

		}

	}
}