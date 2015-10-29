package com.bpa.qaproduct.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bpa.qaproduct.entity.ExecutionMethodParameter;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.ExecutionResultException;
import com.bpa.qaproduct.service.ExecutionService;
import com.jcraft.jsch.Logger;

public class ReportXmlParsing {

	protected final Log logger = LogFactory.getLog(ReportXmlParsing.class);
	
	private String uploadDirectory = File.separator + "webapps"+ File.separator;
	
	public ExecutionResult parseXmlReportFile(String xmlPath,Integer userId)
			throws ParserConfigurationException, SAXException, IOException,
			ParseException {

		// TODO Auto-generated method stub
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date todayDate = (Date) formatter.parse(formatter.format(currentDate
				.getTime()));
		ReportXmlParsing reportXmlParsing = new ReportXmlParsing();
		ExecutionResult executionResult = new ExecutionResult();
		Set<ExecutionResultDetail> methodSet = new HashSet<ExecutionResultDetail>();
		Set<ExecutionMethodParameter> methodParamSet = new HashSet<ExecutionMethodParameter>();
		ExecutionResultDetail executionResultDetail = null;
		ExecutionMethodParameter executionMethodParameter = null;
		Set<ExecutionResultException> exResultExceptionSet = new HashSet<ExecutionResultException>();
		
		File exceptionMethod_file = null; FileOutputStream exceptionMethodFOS = null;
		OutputStreamWriter oStreamExceptionWriter = null;
        BufferedWriter exceptionBufferedWriter = null;
		
		NodeList nodeList;
		ReportXmlParsing xmlParsingCustom = new ReportXmlParsing();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		/*
		 * Document document = db .parse(new File(
		 * "C:/Tomcat 7/webapps/eServices1418810819854/testng-results.xml"));
		 */
		int exceptionIndex = 0;
		Document document = db.parse(new File(xmlPath));
		nodeList = document.getElementsByTagName("testng-results");
		System.out.println("**********Testing Results**********");
		for (int testResultIndex = 0, testResultsSize = nodeList.getLength(); testResultIndex < testResultsSize; testResultIndex++) {

			String skipped = xmlParsingCustom.valuePresent(nodeList, "skipped",
					testResultIndex);
			String failed = xmlParsingCustom.valuePresent(nodeList, "failed",
					testResultIndex);
			String total = xmlParsingCustom.valuePresent(nodeList, "total",
					testResultIndex);
			String passed = xmlParsingCustom.valuePresent(nodeList, "passed",
					testResultIndex);
			logger.info("Skipped is :" + skipped + " Failed is :" + failed);
			logger.info("passed is :" + passed + " total is :" + total);
			executionResult.setSkipped(reportXmlParsing.toInteger(skipped));
			executionResult.setFailed(reportXmlParsing.toInteger(failed));
			executionResult.setTotal(reportXmlParsing.toInteger(total));
			executionResult.setPassed(reportXmlParsing.toInteger(passed));

			nodeList = document.getElementsByTagName("suite");
			
			// System.out.println("************Test Suites********" +
			// nodeList.getLength());
			for (int testSuiteIndex = 0, testSuiteSize = nodeList.getLength(); testSuiteIndex < testSuiteSize; testSuiteIndex++) {
				String suiteName = xmlParsingCustom.valuePresent(nodeList,
						"name", testSuiteIndex);
				String suiteDuration = xmlParsingCustom.valuePresent(nodeList,
						"duration-ms", testSuiteIndex);
				String startedSuitetime = xmlParsingCustom.valuePresent(
						nodeList, "started-at", testSuiteIndex);
				String finishedSuiteTime = xmlParsingCustom.valuePresent(
						nodeList, "finished-at", testSuiteIndex);

				executionResult.setDuration(suiteDuration);
				executionResult.setStartTime(reportXmlParsing
						.convertStringToDate(startedSuitetime));
				executionResult.setEndTime(reportXmlParsing
						.convertStringToDate(finishedSuiteTime));
				executionResult.setCreatedBy(userId);
				executionResult.setCreatedOn(todayDate);
				executionResult.setUpdatedBy(userId);
				executionResult.setUpdatedOn(todayDate);
				nodeList = document.getElementsByTagName("test");
				// System.out.println("**********tests**************");
				for (int testIndex = 0, testSize = nodeList.getLength(); testIndex < testSize; testIndex++) {
					String testName = xmlParsingCustom.valuePresent(nodeList,
							"name", testIndex);
					String testDuration = xmlParsingCustom.valuePresent(
							nodeList, "duration-ms", testIndex);
					String testStartedAt = xmlParsingCustom.valuePresent(
							nodeList, "started-at", testIndex);
					String testFinishedAt = xmlParsingCustom.valuePresent(
							nodeList, "finished-at", testIndex);
					// System.out.println("*******test-methods*****************");
					nodeList = document.getElementsByTagName("test-method");
					for (int testMethodIndex = 0, testMethodSize = nodeList
							.getLength(); testMethodIndex < testMethodSize; testMethodIndex++) {
						executionResultDetail = new ExecutionResultDetail();
						String methodName = xmlParsingCustom.valuePresent(
								nodeList, "name", testMethodIndex);
						
						String methodStatus = xmlParsingCustom.valuePresent(
								nodeList, "status", testMethodIndex);
						String methodSignature = xmlParsingCustom.valuePresent(
								nodeList, "signature", testMethodIndex);
						String methodDuration = xmlParsingCustom.valuePresent(
								nodeList, "duration-ms", testMethodIndex);
						String methodStartedAt = xmlParsingCustom.valuePresent(
								nodeList, "started-at", testMethodIndex);
						String methodFinishedAt = xmlParsingCustom
								.valuePresent(nodeList, "finished-at",
										testMethodIndex);
						String methodDescription = xmlParsingCustom
								.valuePresent(nodeList, "description",
										testMethodIndex);
						String methodDataProvider = xmlParsingCustom
								.valuePresent(nodeList, "data-provider",
										testMethodIndex);
						executionResultDetail.setTestMethodName(methodName);
						executionResultDetail.setStatus(methodStatus);
						executionResultDetail.setSignature(methodSignature);
						executionResultDetail.setDuration(methodDuration);
						executionResultDetail.setStartedAt(reportXmlParsing
								.convertStringToDate(methodStartedAt));
						executionResultDetail.setFinishedAt(reportXmlParsing
								.convertStringToDate(methodFinishedAt));
						executionResultDetail.setDescription(methodDescription);
						executionResultDetail.setCreatedBy(userId);
						executionResultDetail.setUpdatedBy(userId);
						executionResultDetail.setCreatedOn(todayDate);
						executionResultDetail.setUpdatedOn(todayDate);
						
						
						/* Changes done for execution result exception in Services STARTS HERE */
						
						try{
						NodeList exceptionNodeList = document
								.getElementsByTagName("exception");
						logger.info("### Test Method Size is : "+exceptionNodeList.getLength());
						ExecutionResultException exResultException = null;
						exResultException = new ExecutionResultException();
						//exResultException.setExecutionResultExceptionMethodName("");
						while(exceptionIndex < exceptionNodeList.getLength()){
							 Element el = (Element) exceptionNodeList.item(exceptionIndex);
							 
							 String exResDet_Status = exceptionNodeList.item(exceptionIndex).getParentNode().getAttributes().getNamedItem("status").getTextContent();
								if(exResDet_Status.equalsIgnoreCase("FAIL") || exResDet_Status.equalsIgnoreCase("SKIP")){
									logger.info("execution Res Det Status after if  is :"+exResDet_Status);
									String exResDet_exactMethodName = exceptionNodeList.item(exceptionIndex).getParentNode().getAttributes().getNamedItem("name").getTextContent();
									executionResultDetail.setTestMethodName(exResDet_exactMethodName);
									executionResultDetail.setStatus(exResDet_Status);
									logger.info("execution Res Det Method Name is :"+exResDet_exactMethodName);
									
							 logger.info("### Test Method Name is : "+exResDet_exactMethodName);
							 String exception_Message = el.getElementsByTagName("message").item(0).getTextContent();
							 String exception_FullStackTrace = el.getElementsByTagName("full-stacktrace").item(0).getTextContent();
							 
							 Properties properties_mail = new Properties();
								InputStream iStream_mail = null;
								String propFileName_mail = "/properties/environment.properties";
								InputStream stream_mail = getClass().getClassLoader()
										.getResourceAsStream(propFileName_mail);
								properties_mail.load(stream_mail);
								String testResultFolder = properties_mail.getProperty("testException_results_store");
								String serverPath = System.getProperty("catalina.base");
								String serverPathWebapps = serverPath + uploadDirectory +testResultFolder+File.separator;
								File exceptionFolder = new File(serverPathWebapps); if(!(exceptionFolder.isDirectory()))exceptionFolder.mkdirs();
							 logger.info("Server Web Apps Path is :"+serverPathWebapps);
							 exceptionMethod_file = new File(serverPathWebapps+exResDet_exactMethodName+"_"+userId+"_"+System.currentTimeMillis()+".txt");
							 logger.info("Server Full path web app file : "+exceptionMethod_file.getAbsolutePath());
							 if(!(exceptionMethod_file.exists())) exceptionMethod_file.createNewFile();
							 exceptionMethodFOS = new FileOutputStream(exceptionMethod_file);
							 logger.info("Exception File name is : "+exceptionMethod_file.getAbsolutePath());
							 String exceptionMtd_filePath = exceptionMethod_file.getAbsolutePath(); 
							 oStreamExceptionWriter = new OutputStreamWriter(exceptionMethodFOS, "UTF-8");
					            exceptionBufferedWriter = new BufferedWriter(oStreamExceptionWriter);
					            exceptionBufferedWriter.write(exResDet_exactMethodName+ " : "); exceptionBufferedWriter.newLine(); exceptionBufferedWriter.newLine();
					            exceptionBufferedWriter.write(exception_Message);
					            exceptionBufferedWriter.newLine(); exceptionBufferedWriter.newLine();exceptionBufferedWriter.newLine();
					            exceptionBufferedWriter.write(exception_FullStackTrace);
							 
							 exResultException.setExecutionResultExceptionFilePath(exceptionMtd_filePath);
							 //exResultException.setExecutionResultExceptionStackTrace(exception_FullStackTrace.getBytes());
							 
							 executionResultDetail.setTestMethodName(exResDet_exactMethodName);
							 exResultException.setExecutionResultExceptionMethodName(exResDet_exactMethodName);
							 exResultExceptionSet.add(exResultException);
								}
							 exceptionIndex++;
							break;
						}
						exceptionBufferedWriter.close();
			            oStreamExceptionWriter.close();exceptionMethodFOS.close();
						executionResultDetail.setExecutionResultExceptions(exResultExceptionSet);
						
						}
						catch(DOMException de){de.printStackTrace();} 
						catch(NullPointerException npe){npe.printStackTrace();}  
						catch(IllegalStateException ise){ise.printStackTrace();}
						catch(ClassCastException cce){cce.printStackTrace();}
						catch(Exception e){e.printStackTrace();}
						
						/* Changes done for execution result exception in Services ENDS HERE */
						
						Node node = nodeList.item(testMethodIndex);

						NodeList paramNode = node.getChildNodes();
						methodParamSet = new HashSet<ExecutionMethodParameter>();
						for (int t = 0; t < paramNode.getLength(); t++) {
							Node childNode = paramNode.item(t);

							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								// Print each employee's detail
								Element eElement = (Element) childNode;
								NodeList subParamNodeList = eElement
										.getElementsByTagName("param");
								for (int paramValueIndex = 0, paramValueSize = subParamNodeList
										.getLength(); paramValueIndex < paramValueSize; paramValueIndex++) {
									executionMethodParameter = new ExecutionMethodParameter();
									Node paramValueNode = subParamNodeList
											.item(paramValueIndex);
									String paramIndex = xmlParsingCustom
											.valuePresent(subParamNodeList,
													"index", paramValueIndex);
									String paramVal = paramValueNode
											.getTextContent().trim();
									executionMethodParameter
											.setParameterIndex(reportXmlParsing
													.toInteger(paramIndex));
									executionMethodParameter
											.setParameterValue(paramVal);
									executionMethodParameter.setCreatedBy(userId);
									executionMethodParameter.setUpdatedBy(userId);
									executionMethodParameter
											.setUpdatedOn(todayDate);
									executionMethodParameter
											.setCreatedOn(todayDate);
									methodParamSet
											.add(executionMethodParameter);
								}

								executionResultDetail
										.setExecutionMethodParameters(methodParamSet);

							}

						}
						methodSet.add(executionResultDetail);
					}

				}

			}
		}
		executionResult.setExecutionResultDetails(methodSet);
		return executionResult;
	}

	private String valuePresent(NodeList nodeList, String attribute, int index) {
		String attributeValue = "";
		NodeList childNodeList = nodeList;
		if (childNodeList.item(index).getAttributes().getNamedItem(attribute) != null) {
			attributeValue = childNodeList.item(index).getAttributes()
					.getNamedItem(attribute).getNodeValue().trim();
		}

		return attributeValue;
	}

	private Date convertStringToDate(String stringDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = null;
		try {

			date = sdf.parse(stringDate);
			System.out.println(date);
			// System.out.println(formatter.format(date));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private Integer toInteger(String valueToConvert) {
		int value = 0;
		if (valueToConvert.trim().length() > 0) {
			value = Integer.parseInt(valueToConvert);
		}
		return value;

	}

}
