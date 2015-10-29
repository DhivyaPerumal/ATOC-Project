package com.bpa.qaproduct.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.bpa.qaproduct.entity.TestSuiteExecution;

@Component
@Service
public class CustomTerminateInstance {

	private AmazonEC2 ec2;
	private AWSCredentials credentials;

	public void terminateAMI(String amiImageID,TestSuiteExecution testSuiteExecutionWithReturn) throws Exception {
		credentials = new BasicAWSCredentials("AKIAJRDEPSZTBI4JNK2A",
				"3WDEu1P9hCxG22uS2jaQqhBRN49lgWdRvYhQVbfI");
		ec2 = new AmazonEC2Client(credentials);
		ec2.setEndpoint("https://ec2.us-west-2.amazonaws.com");
		ec2.setRegion(Region.getRegion(Regions.US_WEST_2));
		try {
			List<String> idList = new ArrayList<String>();
			idList.add(amiImageID);
			
			// Terminate instances.
			TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(
					idList);
			ec2.terminateInstances(terminateRequest);
		} catch (AmazonServiceException e) {
			// Write out any exceptions that may have occurred.
			System.out.println("Error terminating instances");
			System.out.println("Caught Exception: " + e.getMessage());
			System.out.println("Reponse Status Code: " + e.getStatusCode());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Request ID: " + e.getRequestId());
		}
	}

}