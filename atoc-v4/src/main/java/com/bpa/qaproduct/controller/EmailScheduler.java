package com.bpa.qaproduct.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.Project;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.service.EmailJobService;
import com.bpa.qaproduct.service.MailService;
import com.bpa.qaproduct.service.ProjectService;
import com.bpa.qaproduct.service.UserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class EmailScheduler {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailJobService emailJobService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	@Autowired
	private MailService mailService;

	protected final Log logger = LogFactory.getLog(EmailScheduler.class);

	@Scheduled(cron = "*/120 * * * * *")
	public void emailSchedulerMethod() {
		try {

			logger.info("Inside Cron Job try Condition");

			EmailJob emailJob = new EmailJob();

			List<EmailJob> listEmailJob = emailJobService
					.getAllEmailTypeJob(emailJob);
			logger.info("List Size is" + listEmailJob.size());

			if (listEmailJob != null) {
				MimeMessage mimeMessage = this.javaMailSenderImpl
						.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
						mimeMessage, true);
				for (EmailJob emailJobObject : listEmailJob) {
					String JobType = emailJobObject.getEmailType();
					logger.info("JobType is" + JobType);

					Blob jobData = emailJobObject.getJobDataMapObject();

					byte[] jobDataByteArray = jobData.getBytes(1,
							(int) jobData.length());

					ObjectInputStream objectInputStream = null;
					if (jobDataByteArray != null) {
						objectInputStream = new ObjectInputStream(
								new ByteArrayInputStream(jobDataByteArray));
						Object retrievedObject = objectInputStream.readObject();
						Map<String, Object> map = (Map<String, Object>) retrievedObject;
						for (Map.Entry<String, Object> jobEntry : map
								.entrySet()) {
							logger.info("key=" + jobEntry.getKey() + ", value="
									+ jobEntry.getValue());
						}
						mailService.sendAtocmail(map);

						// emailJobService.removeemailJob(emailJobObject);

					}

					for (EmailJob emailJobObj : listEmailJob) {
						emailJobService.removeemailJob(emailJobObj);
					}

				}

			} else {
				logger.info("List is empty");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
