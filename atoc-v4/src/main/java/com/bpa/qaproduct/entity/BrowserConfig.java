package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "browser_config")
public class BrowserConfig {

	@Id
	@Column(name = "BROWSER_CONFIG_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer browser_Config_Id;

	@Column(name = "OS_NAME")
	private String os_Name;

	@Column(name = "BROWSER")
	private String browser;

	@Column(name = "BROWSER_VERSION")
	private String browser_Version;

	public Integer getBrowser_Config_Id() {
		return browser_Config_Id;
	}

	public void setBrowser_Config_Id(Integer browser_Config_Id) {
		this.browser_Config_Id = browser_Config_Id;
	}

	public String getOs_Name() {
		return os_Name;
	}

	public void setOs_Name(String os_Name) {
		this.os_Name = os_Name;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowser_Version() {
		return browser_Version;
	}

	public void setBrowser_Version(String browser_Version) {
		this.browser_Version = browser_Version;
	}

}
