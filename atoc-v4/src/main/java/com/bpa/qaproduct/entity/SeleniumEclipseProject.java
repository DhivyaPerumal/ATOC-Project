package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT_JAR_LIST")
public class SeleniumEclipseProject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PROJECT_JAR_ID")
	private Integer projectJarId;

	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "PROJECT_NAME")
	private String projectName;

	@Column(name = "PROJECT_JARS")
	private String projectJars;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	public Integer getProjectJarId() {
		return projectJarId;
	}

	public void setProjectJarId(Integer projectJarId) {
		this.projectJarId = projectJarId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectJars() {
		return projectJars;
	}

	public void setProjectJars(String projectJars) {
		this.projectJars = projectJars;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
	

}
