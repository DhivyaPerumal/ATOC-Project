package com.bpa.qaproduct.entity;

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "TEST_SUITE")
public class TestSuite {

	@Id
	@Column(name = "TEST_SUITE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testSuiteId;

	@Column(name = "SUITE_NAME")
	protected String suiteName;

	@Column(name = "NOTES")
	protected String notes;

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

	@Column(name = "TEST_SUITE_URL")
	protected String testSuiteUrl;

	@Column(name = "TESTSUITE_XML_PATH_IN_JAR")
	private String testSuiteXmlPathInJar;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID", updatable = false)
	@JsonBackReference
	protected Project project;

	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ORGANIZATION_ID")
	@JsonBackReference
	protected Organization organization;

	/*@OneToMany(mappedBy = "testSuite", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	protected Set<TestSuiteExecution> testSuiteExecution;

	public Set<TestSuiteExecution> getTestSuiteExecution() {
		return testSuiteExecution;
	}

	public void setTestSuiteExecution(Set<TestSuiteExecution> testSuiteExecution) {
		this.testSuiteExecution = testSuiteExecution;
	}*/
	
	@OneToMany (mappedBy="testSuite")
	@Cascade({ CascadeType.DELETE })
	private List<TestSuiteExecution> testSuiteExecutionList;

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getTestSuiteUrl() {
		return testSuiteUrl;
	}

	public void setTestSuiteUrl(String testSuiteUrl) {
		this.testSuiteUrl = testSuiteUrl;
	}

	public String getTestSuiteXmlPathInJar() {
		return testSuiteXmlPathInJar;
	}

	public void setTestSuiteXmlPathInJar(String testSuiteXmlPathInJar) {
		this.testSuiteXmlPathInJar = testSuiteXmlPathInJar;
	}

	public String getIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Y")) {
			isActiveString = "yes";
		} else if (isActive.equalsIgnoreCase("N")) {
			isActiveString = "no";
		}

		return isActiveString;
	}

	public String setIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("yes")) {
			isActiveString = "Y";
		} else if (isActive.equalsIgnoreCase("no")) {
			isActiveString = "N";
		}

		return isActiveString;
	}

	public List<TestSuiteExecution> getTestSuiteExecutionList() {
		return testSuiteExecutionList;
	}

	public void setTestSuiteExecutionList(
			List<TestSuiteExecution> testSuiteExecutionList) {
		this.testSuiteExecutionList = testSuiteExecutionList;
	}

	
	
	

}
