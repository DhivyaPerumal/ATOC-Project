package com.bpa.qaproduct.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.ec2.model.Instance;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.bpa.qaproduct.util.CustomTerminateInstance;
import com.bpa.qaproduct.util.TestSuitExecutionUtil;

@Component
@Service
@Transactional
public class SampleForNotification {
	protected final Log logger = LogFactory.getLog(TestSuitExecutionUtil.class);
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
	//	processExecution(builder);
		// specific to linux

		/*
		 * ProcessBuilder builder = new ProcessBuilder("bash", "-c","cd " +
		 * webappsDir + " && " + "mkdir " + projectName + " && cd " +
		 * cmdPathDirectory + " && " +
		 * "unzip "+fulljarPath+" -d "+webappsDir+"/"+projectName);
		 */

	
		logger.info("test xml parsing");

		// Linux specific
		/*
		 * builder = new ProcessBuilder("bash", "-c","cd " + cmdPathDirectory +
		 * " &&  mkdir \"" + testOutputFolder + "\"" + " && " + command +
		 * " && cd .. && rm -rf \"" + cmdPathDirectory + "\"");
		 */
		// Windows Specific
		
		logger.info("amazon image id is " + instanceObj.getInstanceId());
		try {
			//terminateInstance.terminateAMI(instanceObj.getInstanceId(),testSuiteExecutionWithReturn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testOutputFolder;
	}
}
