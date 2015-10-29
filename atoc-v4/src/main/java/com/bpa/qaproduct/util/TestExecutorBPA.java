package com.bpa.qaproduct.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class TestExecutorBPA {
	public String executeTestSuite(String testsuiteName, String projectname,
			String projectJarPath) throws IOException {
		String uploadDirectory = File.separator + "webapps" + File.separator
				+ "eServices" + File.separator;
		String uploadDirectoryBase = System.getProperty("catalina.base");
		String appPath = uploadDirectoryBase + uploadDirectory;

		String cmdPathDirectory = uploadDirectoryBase + File.separator
				+ "webapps" + File.separator + "eServices";
		System.out.println("**** app path is*******" + appPath);
		System.out.println("cmd path is " + cmdPathDirectory);
		String command = "java -cp  \"" + appPath + "lib\\*;" + appPath
				+ "bin\" org.testng.TestNG \"" + appPath
				+ "suites\\login.xml\" -d \"C:\\test\\\"";
		;

		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd "
				+ cmdPathDirectory + " && " + command);

		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}

		/*
		 * TestNG testing = new TestNG();
		 * 
		 * // testing.setTestJar("C:/Tomcat 7/webapps/eServices.jar");
		 * List<String> listSuites = new ArrayList<String>();
		 * listSuites.add(appPath + "suites//login.xml");
		 * testing.setTestSuites(listSuites); String outputdir = "C:/" +
		 * "TestOutput/"; testing.setOutputDirectory(outputdir);
		 * System.out.println("before run....."); testing.run();
		 * System.out.println("before run.....");
		 */
		return null;
	}

	public static void addPath(String s) throws Exception {
		File f = new File(s);
		URL url = f.toURI().toURL();
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class<?> urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL",
				new Class<?>[] { URL.class });
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[] { url });

	}

}

