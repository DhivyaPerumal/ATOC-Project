package com.bpa.qaproduct.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.services.ec2.model.Instance;
import com.bpa.qaproduct.entity.AmazonImages;
import com.bpa.qaproduct.entity.EmailJob;
import com.bpa.qaproduct.entity.ExecutionMethodParameter;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.ExecutionResultException;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.repository.ExecutionMethodParameterDAO;
import com.bpa.qaproduct.repository.ExecutionResultDAO;
import com.bpa.qaproduct.repository.ExecutionResultDetailDAO;
import com.bpa.qaproduct.util.CreatePDF;
import com.bpa.qaproduct.util.CustomTerminateInstance;
import com.bpa.qaproduct.util.ReportXmlParsing;
import com.bpa.qaproduct.util.SpinningAWSInstance;
import com.bpa.qaproduct.util.TestSuitExecutionUtil;
import com.itextpdf.text.Document;

@Component
@Service(value = "ExecutionService")
@Transactional
public class ExecutionService {
	protected final Log logger = LogFactory.getLog(ExecutionService.class);

	@Autowired
	private ExecutionResultDAO executionResultDAO;

	@Autowired
	private ExecutionResultDetailDAO executionResultDetailDAO;

	@Autowired
	private ExecutionMethodParameterDAO executionMethodParameterDAO;
	
	@Autowired
	private TestSuiteExecutionService testSuiteExecutionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private ExecutionResultExceptionService exResultExceptionService;
	
	@Autowired
	private SampleForNotification sampleForNotification;

	public void testSuiteExecutionName(
			TestSuiteExecution testSuiteExecutionWithReturn,
			String projectJarPath, String projectName, String testSuiteUrl,
			AmazonImages amazonImages) throws IOException {
		SpinningAWSInstance newAWSInstance = new SpinningAWSInstance(testSuiteExecutionWithReturn.getTestSuiteExecId()+"",amazonImages.getAmazonImagesId()+"");
		Instance instance = newAWSInstance.startSpinning(testSuiteExecutionWithReturn.getTestSuiteExecId()+"",
				amazonImages.getAmiId());
		logger.info("The instance ip address is "
				+ instance.getPublicIpAddress()); 
		logger.info("In the test suite execution name");
		logger.info("fresh5 connection");
		Properties prop = new Properties();
		String propFileName = "properties/environment.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String baseUrl = prop.getProperty("ftp_jar_location");
		String reportsSavingPath = prop.getProperty("reports_saving");
		TestSuitExecutionUtil testSuitExecutionUtil = new TestSuitExecutionUtil();
		/*String resultoutputfolderpath = testSuitExecutionUtil.executeTestSuite(
				baseUrl, projectJarPath, projectJarPath.replaceAll(".jar", ""),
				testSuiteUrl, testSuiteExecutionWithReturn, instance,
				reportsSavingPath);*/
		
		//Code Changed for Notification
		
		String resultoutputfolderpath = executeTestSuite(
				baseUrl, projectJarPath, projectJarPath.replaceAll(".jar", ""),
				testSuiteUrl, testSuiteExecutionWithReturn, instance,
				reportsSavingPath);
		
		
		logger.info("outputfolder path..." + resultoutputfolderpath); 
		executionResultSaving(resultoutputfolderpath,
				testSuiteExecutionWithReturn); 
        
	}

	/*Util Method Added here for Notification*/
	public String executeTestSuite(String resourceBasePath, String jarpath,
			String projectName, String suitePathFromJar,
			TestSuiteExecution testSuiteExecutionWithReturn,
			Instance instanceObj, String reportsSavingPath) throws IOException {
		
		
		
		String nodeIpAddress = "http://" + instanceObj.getPublicDnsName()
				+ ":5555/wd/hub";
		java.util.Date date = new java.util.Date();
		long currentTimestampLong = date.getTime();
		String currentTimestamp = String.valueOf(currentTimestampLong);
		/*String uploadDirectory = File.separator + "webapps" + File.separator
				+ projectName + File.separator;*/
		String uploadDirectory = File.separator + "webapps" + File.separator
				+ testSuiteExecutionWithReturn.getTestSuiteExecId() + File.separator;
		String uploadDirectoryBase = System.getProperty("catalina.base");
		String appPath = uploadDirectoryBase + uploadDirectory;
		String webappsDir = uploadDirectoryBase + File.separator + "webapps"
				+ File.separator;
		String suitePath = webappsDir + testSuiteExecutionWithReturn.getTestSuiteExecId() + File.separator
				+ suitePathFromJar;
		logger.info("SuitePathFromJar......" + suitePath);
		logger.info("opearting system.."
				+ testSuiteExecutionWithReturn.getOperatingSystem());
		logger.info("Browser.." + testSuiteExecutionWithReturn.getBrowser());
		logger.info("Browser Version.."
				+ testSuiteExecutionWithReturn.getBrowserVersion());
		String cmdPathDirectory = uploadDirectoryBase + File.separator
				+ "webapps" + File.separator + testSuiteExecutionWithReturn.getTestSuiteExecId();
		String fulljarPath = resourceBasePath + File.separator + jarpath;
		String testOutputFolder = reportsSavingPath+File.separator+testSuiteExecutionWithReturn.getTestSuiteExecId()+ currentTimestamp;
		appPath = appPath.replace("\\", "/");
		suitePathFromJar = suitePathFromJar.replace("\\", "/");
		testOutputFolder = testOutputFolder.replace("\\", "/");
		String command = "java -cp  \"" + appPath + "lib/*;" + appPath
				+ "\" org.testng.TestNG \"" + appPath + suitePathFromJar
				+ "\" -d \"" + testOutputFolder + "/\"";

		// specific to windows

		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd "
				+ webappsDir + " && " + "mkdir " + testSuiteExecutionWithReturn.getTestSuiteExecId() + " && cd "
				+ cmdPathDirectory + " && " + "jar -xvf \"" + fulljarPath
				+ "\"");
		processExecution(builder);
		// specific to linux

		/*
		 * ProcessBuilder builder = new ProcessBuilder("bash", "-c","cd " +
		 * webappsDir + " && " + "mkdir " + projectName + " && cd " +
		 * cmdPathDirectory + " && " +
		 * "unzip "+fulljarPath+" -d "+webappsDir+"/"+projectName);
		 */

		updateSuiteXml(suitePath, testSuiteExecutionWithReturn.getBrowser(),
				testSuiteExecutionWithReturn.getOperatingSystem(),
				nodeIpAddress);
		logger.info("test xml parsing");

		// Linux specific
		/*
		 * builder = new ProcessBuilder("bash", "-c","cd " + cmdPathDirectory +
		 * " &&  mkdir \"" + testOutputFolder + "\"" + " && " + command +
		 * " && cd .. && rm -rf \"" + cmdPathDirectory + "\"");
		 */
		// Windows Specific
		builder = new ProcessBuilder("cmd.exe", "/c", "cd " + cmdPathDirectory
				+ " && mkdir \"" + testOutputFolder + "\"" + " && " + command
				+ " && cd .. && RD /S /Q \"" + cmdPathDirectory + "\"");
		processExecution(builder);
		// delete the project folder forcefully if the command exists due to System error
		builder = new ProcessBuilder("cmd.exe", "/c", "cd " + cmdPathDirectory
				+ " && cd ..  && RD /S /Q \"" + cmdPathDirectory + "\"");
		processExecution(builder);
		CustomTerminateInstance terminateInstance = new CustomTerminateInstance();
		logger.info("amazon image id is " + instanceObj.getInstanceId());
		try {
			terminateInstance.terminateAMI(instanceObj.getInstanceId(),testSuiteExecutionWithReturn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testOutputFolder;
	}
	public void processExecution(ProcessBuilder processBuilder)
			throws IOException {
		String line;
		processBuilder.redirectErrorStream(true);
		Process p = processBuilder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			logger.info(line);
		}
	}
	
	public void updateSuiteXml(String suitePath, String browserValue,
			String osValue, String ipValue) {

		try {
			logger.info("inside the update method.....");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(suitePath);
			Document document = (Document) db.parse(file);
			NodeList ParametersList = ((Element) document)
					.getElementsByTagName("parameter");
			for (int y = 0; y < ParametersList.getLength(); y++) {
				Element parameter = (Element) ParametersList.item(y);
				if (parameter.getAttribute("name").equalsIgnoreCase("browser")) {
					NamedNodeMap attr = parameter.getAttributes();
					Node nodeAttr = attr.getNamedItem("value");
					nodeAttr.setTextContent(browserValue.toLowerCase());
				}
				if (parameter.getAttribute("name").equalsIgnoreCase("IP")) {
					NamedNodeMap attr = parameter.getAttributes();
					Node nodeAttr = attr.getNamedItem("value");
					nodeAttr.setTextContent(ipValue);
				}
				if (parameter.getAttribute("name").equalsIgnoreCase("platform")) {
					NamedNodeMap attr = parameter.getAttributes();
					if(osValue.toUpperCase().contains("WINDOWS"))
					{
						osValue="WINDOWS";
					}
					else
					{
						osValue = "LINUX";
					}
					Node nodeAttr = attr.getNamedItem("value");
					nodeAttr.setTextContent(osValue.toUpperCase());
				}

			}
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file);
			DOMSource source = new DOMSource((Node) document);
			transformer.transform(source, result);
			logger.info("end the update method.....");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	private void executionResultSaving(String resultoutputfolderpath,
			TestSuiteExecution testSuiteExecutionWithReturn) {
		ReportXmlParsing reportXml = new ReportXmlParsing();
		resultoutputfolderpath = resultoutputfolderpath + "/testng-results.xml";
		ExecutionResult executionResult = new ExecutionResult();
		ExecutionResult executionResultWithReturn = null;
		ExecutionResultDetail executionResultDetail = null;
		ExecutionResultDetail executionResultDetailWithReturn = null; ExecutionResultDetail executionResultDetailWithReturnTwo = null;
		ExecutionMethodParameter executionMethodParameter = null;
		ExecutionResultException executionResultException = null; ExecutionResultException execResExceptionWithReturn = null;
		Set<ExecutionResultException> exResultExceptionSet = null;
		
		logger.info("resultoutputfolderpath path..." + resultoutputfolderpath);
		try {
			logger.info("In Execution Service User Id is :"+testSuiteExecutionWithReturn.getCreatedBy());
			executionResult = reportXml
					.parseXmlReportFile(resultoutputfolderpath,testSuiteExecutionWithReturn.getCreatedBy());

			logger.info("ExeResDet is :"
					+ executionResult.getExecutionResultDetails()
					+ "and size is :"
					+ executionResult.getExecutionResultDetails().size());

			for (ExecutionResultDetail exDetail : executionResult
					.getExecutionResultDetails()) {
			}

			executionResult.setTestExecutionName(testSuiteExecutionWithReturn
					.getTestSuiteExecName());
			logger.info("getTestSuiteExecName is :"
					+ testSuiteExecutionWithReturn.getTestSuiteExecName());
			executionResult.setTestSuiteExecution(testSuiteExecutionWithReturn);
			logger.info("executionResult path..." + executionResult.toString());
			executionResultWithReturn = new ExecutionResult();
			
			logger.info("Execution result is "+executionResultWithReturn.getExecutionResultId());
			logger.info("Test Execution Name is"+executionResultWithReturn.getTestExecutionName());
			logger.info("Total Result is"+executionResultWithReturn.getTotal());
			logger.info("Execution Result details"+testSuiteExecutionWithReturn.getTestSuiteExecName());
			logger.info("Result Project Name is"+testSuiteExecutionWithReturn.getTestSuite().getProject().getProjectName());
			logger.info("Result passed is"+executionResultWithReturn.getPassed());
			logger.info("Result failed is"+executionResultWithReturn.getFailed());
			executionResultWithReturn = executionResultDAO.saveExecutionResultWithReturn(executionResult);
			logger.info("After Saving Records in Db...*************"
					+ executionResult.getExecutionResultId());

			logger.info("After Saved ExeRes And going to Save ExeResDet is :"
					+ executionResult.getExecutionResultDetails().size());
			Set<ExecutionResultDetail> exList = executionResult
					.getExecutionResultDetails();
			for (ExecutionResultDetail exDetail : exList) {
				executionResultDetail = new ExecutionResultDetail();

				executionResultDetail.setTestMethodName(exDetail
						.getTestMethodName());
				executionResultDetail.setDuration(exDetail.getDuration());
				executionResultDetail.setStatus(exDetail.getStatus());
				executionResultDetail.setStartedAt(exDetail.getStartedAt());
				executionResultDetail.setFinishedAt(exDetail.getFinishedAt());
				executionResultDetail.setDescription(exDetail.getDescription());
				executionResultDetail.setSignature(exDetail.getSignature());
				executionResultDetail.setCreatedOn(exDetail.getCreatedOn());
				executionResultDetail.setCreatedBy(exDetail.getCreatedBy());
				executionResultDetail.setUpdatedOn(exDetail.getUpdatedOn());
				executionResultDetail.setUpdatedBy(exDetail.getUpdatedBy());
				executionResultDetail.setExecutionResult(executionResult);
				
				executionResultDetail.setExecutionResultExceptions(exDetail.getExecutionResultExceptions());
					
				executionResultDetailWithReturn = executionResultDetailDAO
							.saveExecutionResultDetailWithReturn(executionResultDetail);
				/* Changes done for execution result exception in Services STARTS HERE */
				try{
					exResultExceptionSet = exDetail.getExecutionResultExceptions();
					
					for(ExecutionResultException exResultException : exResultExceptionSet){
						
						if(exDetail.getTestMethodName().equalsIgnoreCase(exResultException.getExecutionResultExceptionMethodName()) && 
								(exDetail.getStatus().equalsIgnoreCase("FAIL") || exDetail.getStatus().equalsIgnoreCase("SKIP") )){
							logger.info("====="+exResultException.getExecutionResultExceptionMethodName()+" === "+exDetail.getTestMethodName());
							logger.info("====="+exResultException.getExecutionResultExceptionFilePath());
							if(executionResultDetailWithReturn != null){
								execResExceptionWithReturn = new ExecutionResultException();
								exResultException.setExecutionResultDetail(executionResultDetailWithReturn);
						execResExceptionWithReturn = exResultExceptionService.saveExecutionResultExceptionWithReturn(exResultException);
						logger.info("Exe Res Exception Inserted Is is :"+execResExceptionWithReturn.getExecutionResultExceptionId());
							}
						}
					}
					
					//executionResultException = exDetail.getExecutionResultException();
					//logger.info("Exe Res Exception Size :"+exResultExceptionSet.size());
					/*for(ExecutionResultException exResultException : exResultExceptionSet){
						if(executionResultDetailWithReturn.getStatus().equalsIgnoreCase("FAIL") || executionResultDetailWithReturn.getStatus().equalsIgnoreCase("SKIP")
								&& executionResultDetailWithReturn.getTestMethodName().equals(exResultException.getExecutionResultExceptionMethodName())){*/
					/*executionResultException.setExecutionResultDetail(executionResultDetailWithReturn);
						logger.info("Ex rtes Detail Id id :"+executionResultDetailWithReturn.getExecutionResultDetailId());
						if( (StringUtils.isNotBlank(executionResultException.getExecutionResultExceptionMethodName())) || 
								StringUtils.isNotEmpty(executionResultException.getExecutionResultExceptionMethodName())) {
					if(executionResultException.getExecutionResultExceptionMethodName().equalsIgnoreCase(null) && executionResultException.getExecutionResultExceptionMethodName() != "") {
					logger.info("### executionResultDetail Size is : "+executionResultException.getExecutionResultExceptionMethodName());
					logger.info(exDetail.getStatus()+" :::::::::"+executionResultException.getExecutionResultDetail().getStatus());
						if(executionResultDetailWithReturn != null){
							execResExceptionWithReturn = new ExecutionResultException();
					execResExceptionWithReturn = exResultExceptionService.saveExecutionResultExceptionWithReturn(executionResultException);
					logger.info("Exe Res Exception Inserted Is is :"+execResExceptionWithReturn.getExecutionResultExceptionId());
					
						}
					}*/
					
					/*	}
					}*/
				}
				catch(NullPointerException npe){npe.printStackTrace();}  
				catch(IllegalStateException ise){ise.printStackTrace();}
				catch(ClassCastException cce){cce.printStackTrace();}
				catch(Exception e){e.printStackTrace();}  
				
				/* Changes done for execution result exception in Services STARTS HERE */
		
				
				Set<ExecutionMethodParameter> exParameters = exDetail
						.getExecutionMethodParameters();
				for (ExecutionMethodParameter exMethodParameter : exParameters) {
					executionMethodParameter = new ExecutionMethodParameter();

					executionMethodParameter
							.setParameterIndex(exMethodParameter
									.getParameterIndex());
					executionMethodParameter
							.setParameterValue(exMethodParameter
									.getParameterValue());
					executionMethodParameter
							.setExecutionResultDetail(exMethodParameter
									.getExecutionResultDetail());
					executionMethodParameter.setUpdatedOn(exMethodParameter
							.getUpdatedOn());
					executionMethodParameter.setUpdatedBy(exMethodParameter
							.getUpdatedBy());
					executionMethodParameter.setCreatedOn(exMethodParameter
							.getCreatedOn());
					executionMethodParameter.setCreatedBy(exMethodParameter
							.getCreatedBy());
					executionMethodParameter
							.setExecutionResultDetail(executionResultDetail);
					executionMethodParameterDAO
							.saveExecutionMethodParameterWithReturn(executionMethodParameter);

				}
			}
			
			//logger.info("PDF document  is created :"+appPath);
			/*logger.info("Execution result is "+executionResultWithReturn.getExecutionResultId());
			logger.info("Test Execution Name is"+executionResultWithReturn.getTestExecutionName());
			logger.info("Total Result is"+executionResultWithReturn.getTotal());
			logger.info("Test Execution Name is"+executionResultWithReturn.getTestSuiteExecution().getTestSuiteExecName());
			
			logger.info("Result Project Name is"+executionResultWithReturn.getTestSuiteExecution().getTestSuite().getProject().getProjectName());
			logger.info("Result passed is"+executionResultWithReturn.getPassed());
			logger.info("Result failed is"+executionResultWithReturn.getFailed());*/
			if(executionResultWithReturn != null ){
				//logger.info("Inside if condition");;
				String filename_Str = "Execution_Result_"+executionResultWithReturn.getTestSuiteExecution().getTestSuiteExecId();
				// Document document = CreatePDF.createExecutionResultAttachment(filename_Str, executionResult);
				logger.info("Execution Name is"+executionResultWithReturn.getTestExecutionName());
				String fileName_pdf = executionResultWithReturn.getTestExecutionName()+".pdf";
				
				logger.info("FileName is : "+fileName_pdf);
			
				String uploadDirectory = File.separator + "webapps"
						+ File.separator;
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String appPath = uploadDirectoryBase + uploadDirectory + fileName_pdf;
			     logger.info("appPath is"+appPath);
			 // set headers for the response
			 //	logger.info("Execution Result detail is"+executionResultWithReturn.getExecutionResultDetails().size());
				
				/*String appPath = "C://Report.pdf";*/
				File downloadfile = new File(appPath);
				Document document1 = CreatePDF.createExecutionResultAttachment(appPath, executionResultWithReturn);
			
				//File file = new File(appPath);
				
				logger.info("In Execution Result got success & updating test Suite Excution STATUS"+downloadfile.getName());
				testSuiteExecutionWithReturn.setExecutionStatus("COMPLETED");
				testSuiteExecutionWithReturn.setExecCompleteOn(new Date());
			//	testSuiteExecutionService.saveTestSuiteExecutionwithReturn(testSuiteExecutionWithReturn);
				/*logger.info("### Test Suite Name"+testSuiteExecutionWithReturn.getTestSuite().getSuiteName());
				logger.info("### Project Name"+testSuiteExecutionWithReturn.getTestSuite().getProject().getProjectName());
				logger.info("### TestSuite ExecName  Name"+testSuiteExecutionWithReturn.getTestSuiteExecName());*/
				
				User searchuser = new User();
				searchuser = userService.getUserById(testSuiteExecutionWithReturn.getCreatedBy());
				if(searchuser != null){

				try {
					// Reading From EMail ID from Properties File
					
					Properties properties_mail = new Properties();
					String propFileName_mail = "/properties/mail.properties";
					InputStream stream_mail = getClass().getClassLoader()
							.getResourceAsStream(propFileName_mail);
					properties_mail.load(stream_mail);
					EmailJob emailJob = new EmailJob();
					logger.info("**************** In Test Execution Result Mail is Ready to Sending");

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("from", properties_mail
							.getProperty("javaMailSender.username"));
					map.put("to", searchuser.getUserEmail());
					map.put("subject", "ATOC - Test Execution Result");
					map.put("firstName", searchuser.getFirstName());
					map.put("testExecutionname", executionResultWithReturn.getTestSuiteExecution().getTestSuiteExecName());
					map.put("projectname", executionResultWithReturn.getTestSuiteExecution().getTestSuite().getProject().getProjectName());
					map.put("testsuitename", executionResultWithReturn.getTestSuiteExecution().getTestSuite().getSuiteName());
					map.put("operatingSystem", executionResultWithReturn.getTestSuiteExecution().getOperatingSystem());
					map.put("browser", executionResultWithReturn.getTestSuiteExecution().getBrowser());
					map.put("browserVersion", executionResultWithReturn.getTestSuiteExecution().getBrowserVersion());
					map.put("totaltests", executionResultWithReturn.getTotal());
					map.put("passedtests", executionResultWithReturn.getPassed());
					map.put("failedtests", executionResultWithReturn.getFailed());
					map.put("skippedtests", executionResultWithReturn.getSkipped());
				
					if(downloadfile.isFile()){
						logger.info("file exists");
					map.put("isAttachment", true);
				//	map.put("filename_Str", fileName_pdf);
					map.put("file",downloadfile);
					}
					
					map.put("templateName", "ExecutionResults.vm");
					map.put("content",
							"You can view your test suite execution result in execution history page. Thank You");
					
					
					byte[] byteArray = null;
					byteArray = convertObjectToByteArray(map);
					emailJob.setJobDataByteArray(byteArray);
					userService.saveOrUpdateEmailJob(emailJob);
					
					
					//mailService.sendExecutionResultAttachment(map);
					logger.info(" ************* In Test Execution   Mail Sended");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
				
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static byte[] convertObjectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
}

}
