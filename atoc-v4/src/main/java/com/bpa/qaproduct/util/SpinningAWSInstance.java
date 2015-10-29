package com.bpa.qaproduct.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

public class SpinningAWSInstance {
	// private Logger log = Logger.getInstance(SpinInstance.class);
	protected final Log log = LogFactory.getLog(SpinningAWSInstance.class);
	// require variable for every operation
	private String accessKey = "AKIAJRDEPSZTBI4JNK2A";
	private String secretKey = "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";
	private AWSCredentials credentials;
	private String endPoint;
	private Region region;
	private AmazonEC2Client ec2client;
	// private AmazonRDSClient rdsclient;

	// EC2 Security Group Variables
	private String groupName = "launch-wizard-20";

	// KeyPair variables
	private String keyName = "redhatlinux";

	// EC2 Instance variables

	private String instanceType = "t2.micro";
	private String instanceName = "GeorgeTestNew";
	private String amiId = null; 
	public SpinningAWSInstance(String name, String amazonImageId) {
		instanceName = name;
		amiId = amazonImageId;
	}
	
	public Instance startSpinning(String newInstanceName, String imageAMIid) {
		instanceName = newInstanceName;
		init();
		Instance instance = createEC2OnDemandInstance(imageAMIid);
		return instance;
	}

	private void init() {
		credentials = new BasicAWSCredentials(accessKey, secretKey);
		// end point for singapore
		endPoint = "https://ec2.us-west-2.amazonaws.com";
		// regions for singapore
		region = Region.getRegion(Regions.US_WEST_2);
		// EC2Client object
		ec2client = new AmazonEC2Client(credentials);
		ec2client.setEndpoint(endPoint);
		ec2client.setRegion(region);

	}

	/*
	 * public static String base64Encode(String token) { byte[] encodedBytes =
	 * Base64.encode(token.getBytes()); return new String(encodedBytes,
	 * Charset.forName("UTF-8")); }
	 */
	private Instance createEC2OnDemandInstance(String imageId) {
		Instance instance = null;
		// String commands =
		// SpinningAWSInstance.base64Encode("#!/bin/bash && java -jar /home/ubuntu/selenium\\ jar/selenium-server-standalone-2.44.0.jar -role node -hub http://54.149.5.105:4444/grid/register");
		// String commands = SpinningAWSInstance.getUserDataScript();

		try {

			// request for new on demand instance
			RunInstancesRequest rir = new RunInstancesRequest();
			rir.withImageId(imageId);
			rir.withInstanceType(instanceType);
			rir.withMinCount(1);
			rir.withMaxCount(1);
			rir.withKeyName(keyName);
			rir.withMonitoring(false);
			rir.withSecurityGroups(groupName);
			RunInstancesResult riresult = ec2client.runInstances(rir);
			log.info(riresult.getReservation().getReservationId());

			// / Find newly created instance id
			String instanceId = null;

			DescribeInstancesResult result = ec2client.describeInstances();
			Iterator<Reservation> i = result.getReservations().iterator();
			while (i.hasNext()) {
				Reservation r = i.next();
				List<Instance> instances = r.getInstances();
				for (Instance ii : instances) {
					log.info(ii.getImageId() + "\t" + ii.getInstanceId() + "\t"
							+ ii.getState().getName() + "\t"
							+ ii.getPrivateDnsName());
					if (ii.getState().getName().equals("pending")) {
						instanceId = ii.getInstanceId();

					}

				}
			}
			log.info("New Instance ID :" + instanceId);
			// / Waiting for Instance Running////
			boolean isWaiting = true;
			while (isWaiting) {
				log.info("*** Waiting ***");
				Thread.sleep(1000);
				DescribeInstancesResult r = ec2client.describeInstances();
				Iterator<Reservation> ir = r.getReservations().iterator();
				while (ir.hasNext()) {
					Reservation rr = ir.next();
					List<Instance> instances = rr.getInstances();
					for (Instance ii : instances) {
						log.info(ii.getImageId() + "\t" + ii.getInstanceId()
								+ "\t" + ii.getState().getName() + "\t"
								+ ii.getPrivateDnsName());

						if (ii.getState().getName().equals("running")
								&& ii.getInstanceId().equals(instanceId)) {
							log.info(ii.getPublicDnsName());
							instance = ii;
							isWaiting = false;
							log.info("before thread sleep for 4 min");
							// Thread.sleep(500000);
							TimeUnit.MINUTES.sleep(4);
							log.info("after thread sleep for 4 min");
						}

					}
				}
			}

			log.info("ip of instance is " + instance.getPublicIpAddress());

			// / Creating Tag for New Instance ////
			log.info("Creating Tags for New Instance");
			CreateTagsRequest crt = new CreateTagsRequest();
			ArrayList<Tag> arrTag = new ArrayList<Tag>();
			arrTag.add(new Tag().withKey("Name").withValue(instanceName));
			crt.setTags(arrTag);

			ArrayList<String> arrInstances = new ArrayList<String>();
			arrInstances.add(instanceId);
			crt.setResources(arrInstances);
			ec2client.createTags(crt);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return instance;
	}
	
	
	/*@Override
	public Instance call() throws Exception {
		
		Instance instanceAfterSpin =  startSpinning(instanceName, amiId);
		return instanceAfterSpin;
	}*/
	
	/*
	 * private static String getUserDataScript(){ ArrayList<String> lines = new
	 * ArrayList<String>(); lines.add("#! /bin/bash"); lines.add(
	 * "java -jar /home/ubuntu/selenium\\ jar/selenium-server-standalone-2.44.0.jar -role node -hub http://ec2-54-69-49-45.us-west-2.compute.amazonaws.com:4444/grid/register"
	 * );
	 * 
	 * lines.add("<powershell>"); lines.add("$prog=\"cmd.exe\""); lines.add(
	 * "$params=@(\"/C\";\"java -jar C:\\Selenium\\selenium-server-standalone-2.44.0.jar -role node -hub http:\\ec2-54-69-49-45.us-west-2.compute.amazonaws.com:4444\\grid\\register \";\" >c:\\result.txt\")"
	 * ); lines.add("Start-Process -Verb runas $prog $params"); lines.add(
	 * "java -jar C:\\Selenium\\selenium-server-standalone-2.44.0.jar -role node -hub http:\\ec2-54-69-49-45.us-west-2.compute.amazonaws.com:4444\\grid\\register"
	 * ); // lines.add("gc c:\temp\result.txt");
	 * //lines.add("start /min C:\\Users\\Administrator\\Desktop\\startHub.bat"
	 * ); lines.add("</powershell>"); String str = new
	 * String(Base64.encode(join(lines, "\n").getBytes())); return str; }
	 * 
	 * static String join(Collection<String> s, String delimiter) {
	 * StringBuilder builder = new StringBuilder(); Iterator<String> iter =
	 * s.iterator(); while (iter.hasNext()) { builder.append(iter.next()); if
	 * (!iter.hasNext()) { break; } builder.append(delimiter); } return
	 * builder.toString(); }
	 */

}
