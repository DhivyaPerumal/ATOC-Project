package com.bpa.qaproduct.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "PROJECT")
public class Project {

	@Id
	@Column(name = "PROJECT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectId;

	@Column(name = "PROJECT_NAME")
	protected String projectName;

	@Column(name = "NOTES")
	protected String notes;

	@Column(name = "START_DATE")
	protected Date startDate;

	@Column(name = "END_DATE")
	protected Date endDate;

	@Column(name = "ISACTIVE")
	protected String isActive;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	@Column(name = "PROJECT_TYPE")
	protected String projectType;

	@Column(name = "PROJECT_PATH")
	protected String projectJarPath;

	public String getProjectJarPath() {
		return projectJarPath;
	}

	public void setProjectJarPath(String projectJarPath) {
		this.projectJarPath = projectJarPath;
	}

	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ORGANIZATION_ID")
	protected Organization organization;

	@OneToMany (mappedBy="project", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	private List<TestSuite> testSuites;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Y")) {
			isActiveString = "Yes";
		} else if (isActive.equalsIgnoreCase("N")) {
			isActiveString = "No";
		}

		return isActiveString;
	}

	public String setIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Yes")) {
			isActiveString = "Y";
		} else if (isActive.equalsIgnoreCase("No")) {
			isActiveString = "N";
		}

		return isActiveString;
	}

	public List<TestSuite> getTestSuites() {
		return testSuites;		
	}

	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuites = testSuites;
	}
	
	

}
