package com.bpa.qaproduct.job;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.TestSuite;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.service.ExecutionService;

@DisallowConcurrentExecution
@Component
public class JobScheduler implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(JobScheduler.class);

	@Autowired
	private ExecutionService executionService;

	@SuppressWarnings("rawtypes")
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		logger.info("Process stated :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: In ImportFromXMLJob");
		try {
			logger.info("Currently Executing Jobs::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"
					+ context.getScheduler().getCurrentlyExecutingJobs().size());
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ApplicationContext appCtx;
		try {
			SpringBeanAutowiringSupport
					.processInjectionBasedOnCurrentContext(this);

			Map dataMap = context.getJobDetail().getJobDataMap();

			logger.info("::::::::::::::::::::::::::: Job Schedule is Running ::::::::::::::::::::::::::::");
			TestSuiteExecution testSuiteExecutionWithReturn = (TestSuiteExecution) dataMap
					.get("testSuiteExecutionWithReturn");
			TestSuite testSuite = (TestSuite) dataMap.get("testSuite");
			AmazonImages amazonImages = (AmazonImages) dataMap
					.get("amazonImages");

			logger.info(":::::::::::::::::::Job Scheduler is Going to execute :::::::::::::::");
			logger.info("Executing User Id is :"+testSuiteExecutionWithReturn.getCreatedBy());
			executionService.testSuiteExecutionName(
					testSuiteExecutionWithReturn, testSuite.getProject()
							.getProjectJarPath(), testSuite.getProject()
							.getProjectName(), testSuite.getTestSuiteUrl(),
					amazonImages);
			logger.info(":::::::::::::::::: Job Scheduler Execution Completed :::::::::::::::::::::::::::::::");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
