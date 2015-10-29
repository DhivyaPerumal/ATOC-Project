package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "amazon_images")
public class AmazonImages {

	@Id
	@Column(name = "AMAZON_IMAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer amazonImagesId;

	@Column(name = "AMI_NAME")
	private String amiName;

	@Column(name = "AMI_ID")
	private String amiId;

	@Column(name = "OS_NAME")
	private String OSName;

	@Column(name = "BROWSER")
	private String browser;

	@Column(name = "BROWSER_VERSION")
	private String browserVersion;

	public Integer getAmazonImagesId() {
		return amazonImagesId;
	}

	public void setAmazonImagesId(Integer amazonImagesId) {
		this.amazonImagesId = amazonImagesId;
	}

	public String getAmiName() {
		return amiName;
	}

	public void setAmiName(String amiName) {
		this.amiName = amiName;
	}

	public String getAmiId() {
		return amiId;
	}

	public void setAmiId(String amiId) {
		this.amiId = amiId;
	}

	public String getOSName() {
		return OSName;
	}

	public void setOSName(String oSName) {
		OSName = oSName;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

}
