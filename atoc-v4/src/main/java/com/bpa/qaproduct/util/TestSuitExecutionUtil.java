package com.bpa.qaproduct.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.services.ec2.model.Instance;
import com.bpa.qaproduct.entity.TestSuiteExecution;
@Component
@Transactional
@Service
@Configurable
public class TestSuitExecutionUtil {
	protected final Log logger = LogFactory.getLog(TestSuitExecutionUtil.class);

	
	
	/**
	 * 
	 * @param resourceBasePath
	 * @param jarpath
	 * @param projectName
	 * @param suitePathFromJar
	 * @throws IOException
	 */
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
			//terminateInstance.terminateAMI(instanceObj.getInstanceId(),testSuiteExecutionWithReturn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testOutputFolder;
	}

	public void updateSuiteXml(String suitePath, String browserValue,
			String osValue, String ipValue) {

		try {
			logger.info("inside the update method.....");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(suitePath);
			Document document = db.parse(file);
			NodeList ParametersList = document
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
			DOMSource source = new DOMSource(document);
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
}
