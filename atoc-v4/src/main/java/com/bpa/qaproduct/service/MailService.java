package com.bpa.qaproduct.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailService {

	private static Logger LOG = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	public String sendDownloadLinkEmail(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(),
				"CreateProjectByJar.vm", map);
	}

	public String sendFtpLocationEmail(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(),
				"ProjectJarDetails.vm", map);
	}

	public String sendRegApprovalEmail(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(),
				"CustomerRegistration.vm", map);
	}

	public String sendLoginDetailsEmail(Map<String, Object> map) {
		String from = map.get("from").toString();
		String to = map.get("to").toString();
		String subject = map.get("subject").toString();
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(), "UserDetails.vm",
				map);
	}
	
	public String sendForgotPasswordEmail(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(), "ForgotPassword.vm",
				map);
	}
	public String sendAddOrganization(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(), "AddOrganization.vm",
				map);
	}
	
	public String sendExecutionResult(Map<String, Object> map) {
		return sendVerificationEmail(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(),
				"ExecutionResults.vm", map);
	}
	public String sendExecutionResultAttachment(Map<String, Object> map) {
		return sendVerificationEmailAttachment(map.get("from").toString(), map.get("to")
				.toString(), map.get("subject").toString(),map,"ExecutionResults.vm");
	}
	private String sendVerificationEmailAttachment(final String from, final String to,
			final String emailSubject,
			final Map<String, Object> map,final String velocityModel) {
		 
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			/* (non-Javadoc)
			 * @see org.springframework.mail.javamail.MimeMessagePreparator#prepare(javax.mail.internet.MimeMessage)
			 */
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(
						mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED,
						"UTF-8");
				messageHelper.setTo(to);
				messageHelper.setFrom(from);
				messageHelper.setSubject(emailSubject);
				
				//To display logo in email.
				String logoName = "atoc_logo.png";
				String uploadDirectory = File.separator + "webapps"
						+ File.separator + "atoc-v4" + File.separator + "resources" + File.separator +"images" +File.separator;
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String logopath = uploadDirectoryBase + uploadDirectory + logoName;
				File logofile = new File(logopath);
				//LOG.info("Logo Path is"+logopath);
				//LOG.info("LogoFile name is"+logofile.getName());
				Map<String,Object> TextMap = new HashMap<String, Object>();
				TextMap.put("from",map.get("from"));
				TextMap.put("to",map.get("to"));
				TextMap.put("subject",map.get("subject"));
				TextMap.put("firstName",map.get("firstName"));
				TextMap.put("testExecutionname", map.get("testExecutionname"));
				TextMap.put("projectname", map.get("projectname"));
				TextMap.put("testsuitename", map.get("testsuitename"));
				TextMap.put("operatingSystem",map.get("operatingSystem"));
				TextMap.put("browser",map.get("browser"));
				TextMap.put("browserVersion",map.get("browserVersion"));
				TextMap.put("totaltests",map.get("totaltests"));
				TextMap.put("passedtests",map.get("passedtests"));
				TextMap.put("failedtests",map.get("failedtests"));
				TextMap.put("skippedtests",map.get("skippedtests"));
				TextMap.put("content",
						 "You can view your test suite execution result in execution history page. Thank You");
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, velocityModel, "UTF-8", TextMap);
                //LOG.info("text"+text);
				File  file_Pdf = null;
				messageHelper.setText(new String(text.getBytes(), "UTF-8"),
						true);
				/*Code for Attachment*/
				Boolean isAttachment = (Boolean)map.get("isAttachment"); 
				//LOG.info("is Attchement is :"+isAttachment);
				if(isAttachment==true){
					//LOG.info("Is Attchement inside if condition"+isAttachment);
					//String FileName = (String)map.get("filename_Str"); 
					file_Pdf = (File)map.get("file");
					
					//LOG.info("File name is"+file_Pdf.getName());
					//String attachName =file_Pdf.getName();
					if(file_Pdf.isFile())
					{
					//	LOG.info("Attached file working condition");
					//messageHelper.addAttachment("reportFile", file_Pdf);
						
						FileSystemResource fileSystemResource = new FileSystemResource(file_Pdf);
						messageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
					//LOG.info("In Message Helper Multi part is : "+messageHelper.getMimeMultipart());
					
					LOG.info("file Pdf "+file_Pdf.getAbsolutePath());
					}
				}
				
				if(logofile.isFile())
				{
					messageHelper.addInline("atoclogo",logofile);
					LOG.info("Logo file created in mail service");
				}
				
				
			}
		};
		
		this.javaMailSenderImpl.send(preparator);
		
		LOG.info("Mail Sucess");
	File file_Pdf_delete=(File) map.get("file");
	if(file_Pdf_delete.delete()){LOG.info("Result File Deleted");
		
		}
		else
		{
			LOG.info("File is not deleted");
		}


		
				return "success";
	}
	
	private String sendVerificationEmail(final String from, final String to,
			final String emailSubject, final String velocityModel,
			final Map<String, Object> map) {
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(
						mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED,
						"UTF-8");
				messageHelper.setTo(to);
				messageHelper.setFrom(from);
				messageHelper.setSubject(emailSubject);
				
				//To display logo in email.
				String logoName = "atoc_logo.png";
				String uploadDirectory = File.separator + "webapps"
						+ File.separator + "atoc-v4" + File.separator + "resources" + File.separator +"images" +File.separator;
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String appPath = uploadDirectoryBase + uploadDirectory + logoName;
				File file = new File(appPath);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, velocityModel, "UTF-8", map);

				File file_Pdf = null;
				messageHelper.setText(new String(text.getBytes(), "UTF-8"),
						true);
				/*Boolean isAttachment = (Boolean)map.get("isAttachment"); 
				if(isAttachment){
					LOG.info("Is Attchement "+isAttachment);
					String FileName = (String)map.get("filename_Str"); 
					file_Pdf = (File)map.get("file");
					messageHelper.addAttachment(FileName, file_Pdf);
					LOG.info("FileName Str "+FileName);
					LOG.info("file Pdf "+file_Pdf);
					
				}*/
				if(file.isFile())
				{
					messageHelper.addInline("atoclogo",file);
				}
				
				/*if(file_Pdf.delete()){LOG.info("Result File Deleted");}*/

			}
		};
		LOG.debug("Sending {} token to : {}", to);

		this.javaMailSenderImpl.send(preparator);
		
		return "success";
	}
	
	public String sendAtocmail(
			final Map<String, Object> map) {
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(
						mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED,
						"UTF-8");
				
				messageHelper.setTo(map.get("to")
						.toString());
				messageHelper.setFrom(map.get("from").toString());
				messageHelper.setSubject( map.get("subject").toString());
				
				//To display logo in email.
				String logoName = "atoc_logo.png";
				String uploadDirectory = File.separator + "webapps"
						+ File.separator + "atoc-v4" + File.separator + "resources" + File.separator +"images" +File.separator;
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String appPath = uploadDirectoryBase + uploadDirectory + logoName;
				File file = new File(appPath);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, map.get("templateName").toString(), "UTF-8", map);

				File file_Pdf = null;
				messageHelper.setText(new String(text.getBytes(), "UTF-8"),
						true);
				/*Boolean isAttachment = (Boolean)map.get("isAttachment"); 
				if(isAttachment){
					LOG.info("Is Attchement "+isAttachment);
					String FileName = (String)map.get("filename_Str"); 
					file_Pdf = (File)map.get("file");
					messageHelper.addAttachment(FileName, file_Pdf);
					LOG.info("FileName Str "+FileName);
					LOG.info("file Pdf "+file_Pdf);
					
				}*/
				if(file.isFile())
				{
					messageHelper.addInline("atoclogo",file);
				}
				
				/*if(file_Pdf.delete()){LOG.info("Result File Deleted");}*/

			}
		};
		LOG.debug("Sending {} token to : {}", map.get("to")
				.toString());

		this.javaMailSenderImpl.send(preparator);
		
		return "success";
	}


}
