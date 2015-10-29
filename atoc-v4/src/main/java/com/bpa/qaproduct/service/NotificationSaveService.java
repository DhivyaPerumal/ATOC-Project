package com.bpa.qaproduct.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.bouncycastle.asn1.ocsp.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpa.qaproduct.controller.TestSuiteExecutionController;
import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.Notification;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.TestSuiteExecutionDAO;
import com.jcraft.jsch.Logger;


@Aspect
@Service("NotificationSaveService")
public class NotificationSaveService {
	
@Autowired
private TestSuiteExecutionService testSuiteExecutionService;

@Autowired
private UserService userService;



	protected final Log logger = LogFactory
			.getLog(NotificationSaveService.class);
	

	
	@Pointcut("execution(* com.bpa.qaproduct.service.ExecutionService.testSuiteExecutionName(..))")
	public void TestPointCut()
	{
		logger.info("Point Cut Method");
	}
	
	
	@Pointcut("execution(* com.bpa.qaproduct.service.ExecutionService.executionResultSaving(..))")
	public void TestPointCutExecution()
	{
		logger.info("Point Cut Method for execution");
	}
	
	
	@Pointcut("execution(* com.bpa.qaproduct.service.ExecutionService.executeTestSuite(..))")
	public void TestExecutionPointCutMethod()
	{
		
	}
	
	
	
	@Before(value = "TestPointCut() && "+ "args(testSuiteExecutionWithReturn,..)")
	public void notificationMethod(TestSuiteExecution testSuiteExecutionWithReturn) throws ParseException 
	{
		
		logger.info("Notification Method Starts for Before");
		
		logger.info("Notification message is"+testSuiteExecutionWithReturn.getBrowser()+"Execution of"+testSuiteExecutionWithReturn.getTestSuite());
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

		User user = new User();
		user.setUserId(testSuiteExecutionWithReturn.getCreatedBy());
		logger.info("User is"+testSuiteExecutionWithReturn.getCreatedBy());
		Notification notification = new Notification();
	
		logger.info("Main function");
		notification.setNotificationStatus("NOTVIEWED");
		
		notification.setUserId(user);
		notification.setNotificationMessage("Your Spinning Starts Successfully");
		logger.info("Notification test execution message is"+testSuiteExecutionWithReturn.getBrowser()+"Version of"+testSuiteExecutionWithReturn.getBrowserVersion()+"with Execution name"+testSuiteExecutionWithReturn.getTestSuiteExecName());
		
		notification.setNotificationTitle(testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion()+"Before Spinning");
		logger.info("Notification title is"+testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion());
		notification.setCreatedOn(todayDate);
		notification.setUpdatedOn(todayDate);
		notification.setCreatedBy(testSuiteExecutionWithReturn.getCreatedBy());
		notification.setUpdatedBy(testSuiteExecutionWithReturn.getUpdatedBy());
		testSuiteExecutionService.saveOrUpdateNotification(notification);
		
	}
	
	@After(value = "TestPointCut() && " + "args(testSuiteExecutionWithReturn,..)")
	public void notificationAfterMethod(TestSuiteExecution testSuiteExecutionWithReturn) throws ParseException
	{
		logger.info("Notification Method  After Spinning");
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
		User user = new User();
		user.setUserId(testSuiteExecutionWithReturn.getCreatedBy());
		logger.info("User is"+testSuiteExecutionWithReturn.getCreatedBy());
		Notification notification = new Notification();
		notification.setNotificationStatus("NOTVIEWED");
		notification.setUserId(user);
		notification.setNotificationMessage("Your Spinning Completes Successfully");
		notification.setNotificationTitle(testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion()+"After Execution");
		notification.setCreatedOn(todayDate);
		notification.setUpdatedOn(todayDate);
		notification.setCreatedBy(testSuiteExecutionWithReturn.getCreatedBy());
		notification.setUpdatedBy(testSuiteExecutionWithReturn.getUpdatedBy());
		testSuiteExecutionService.saveOrUpdateNotification(notification);
	}
	
	@Before(value = "TestPointCutExecution() && "+ "args(testSuiteExecutionWithReturn,..)")
	public void notificationBeforeExecutionSaving(TestSuiteExecution testSuiteExecutionWithReturn) throws ParseException
	{
		logger.info("Before Execution in Notification");
		logger.info("Method Running Successfully");
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
		User user = new User();
		user.setUserId(testSuiteExecutionWithReturn.getCreatedBy());
		Notification notification = new Notification();
		notification.setNotificationStatus("NOTVIEWED");
		notification.setUserId(user);
		notification.setNotificationMessage("Your Execution is Started for Particular Project");
		notification.setNotificationTitle(testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion()+"Before Execution Saving");
		notification.setCreatedOn(todayDate);
		notification.setUpdatedOn(todayDate);
		notification.setCreatedBy(testSuiteExecutionWithReturn.getCreatedBy());
		notification.setUpdatedBy(testSuiteExecutionWithReturn.getUpdatedBy());
		testSuiteExecutionService.saveOrUpdateNotification(notification);
	}
	
	@After(value = "TestPointCutExecution() && "+ "args(testSuiteExecutionWithReturn,..)")
	public void notificationAfterExecution(TestSuiteExecution testSuiteExecutionWithReturn) throws ParseException
	{
		logger.info("After Execution in Notification");
		logger.info("After Execution Service Completed");
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
		User user = new User();
		user.setUserId(testSuiteExecutionWithReturn.getCreatedBy());
		Notification notification = new Notification();
		notification.setNotificationStatus("NOTVIEWED");
		notification.setUserId(user);
		notification.setNotificationMessage("Execution Completed Successfully");
		notification.setNotificationTitle(testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion()+"After Execution Completes");
		notification.setCreatedOn(todayDate);
		notification.setUpdatedOn(todayDate);
		notification.setCreatedBy(testSuiteExecutionWithReturn.getCreatedBy());
		notification.setUpdatedBy(testSuiteExecutionWithReturn.getUpdatedBy());
		testSuiteExecutionService.saveOrUpdateNotification(notification);
	}
	
	@After(value = "TestExecutionPointCutMethod() && "+ "args(testSuiteExecutionWithReturn,..)")
	public void notificationForTerminationError(TestSuiteExecution testSuiteExecutionWithReturn) throws ParseException
	{
		logger.info("termination with error");
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
		User user = new User();
		user.setUserId(testSuiteExecutionWithReturn.getCreatedBy());
		Notification notification = new Notification();
		notification.setNotificationStatus("NOTVIEWED");
		notification.setUserId(user);
		notification.setNotificationMessage("Execution Terminated Successfully");
		notification.setNotificationTitle(testSuiteExecutionWithReturn.getBrowser()+"Title Project for Version"+testSuiteExecutionWithReturn.getBrowserVersion()+"After Execution Completes");
		notification.setCreatedOn(todayDate);
		notification.setUpdatedOn(todayDate);
		notification.setCreatedBy(testSuiteExecutionWithReturn.getCreatedBy());
		notification.setUpdatedBy(testSuiteExecutionWithReturn.getUpdatedBy());
		testSuiteExecutionService.saveOrUpdateNotification(notification);
	}
	
}
