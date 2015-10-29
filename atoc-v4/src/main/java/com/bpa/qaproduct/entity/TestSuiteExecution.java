package com.bpa.qaproduct.entity;

import java.util.Date;
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
@Table(name = "TEST_SUITE_EXECUTION")
public class TestSuiteExecution {

	@Id
	@Column(name = "TEST_SUTIE_EXECUTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testSuiteExecId;

	@Column(name = "SCHEDULED_ON")
	protected Date scheduledOn;

	@Column(name = "EXECUTION_TYPE")
	protected String executionType;

	@Column(name = "PRIORITY")
	protected String priority;

	@Column(name = "LOG_LEVEL")
	protected String logLevel;

	@Column(name = "NOTIFICATION_EMAIL")
	protected String notificationEmail;

	@Column(name = "FATAL_ERROR_NOTIFICATION_MAIL")
	protected String fatalErrorNotificationMail;

	@Column(name = "STATUS")
	protected String status;

	/*
	 * @Column(name = "TEST_SUITE_DETAIL_ID") protected Integer
	 * testSuiteDetailId;
	 */
	/*@ManyToOne
	@JoinColumn(name = "TEST_SUITE_DETAIL_ID", referencedColumnName = "TEST_SUITE_DETAIL_ID")
	protected TestSuiteDetail testSuiteDetail;*/

	@ManyToOne
	@JoinColumn(name = "TEST_SUITE_ID", referencedColumnName = "TEST_SUITE_ID", updatable = false)
	@JsonBackReference
	protected TestSuite testSuite;

	@OneToMany(mappedBy = "testSuiteExecution")
	@Cascade({ CascadeType.DELETE })
	protected Set<ExecutionResult> executionResult;

	@OneToMany(mappedBy = "schedulerTestSuiteExecution")
	@Cascade({ CascadeType.DELETE })
	protected Set<SchedulerExecution> schedulerTestSuiteExecution;

	public Set<ExecutionResult> getExecutionResult() {
		return executionResult;
	}

	public void setExecutionResult(Set<ExecutionResult> executionResult) {
		this.executionResult = executionResult;
	}
	

	

	public Set<SchedulerExecution> getSchedulerTestSuiteExecution() {
		return schedulerTestSuiteExecution;
	}

	public void setSchedulerTestSuiteExecution(
			Set<SchedulerExecution> schedulerTestSuiteExecution) {
		this.schedulerTestSuiteExecution = schedulerTestSuiteExecution;
	}




	@Column(name = "EXECUTION_COMPLETE_ON")
	protected Date execCompleteOn;

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

	@Column(name = "OPERATING_SYSTEM")
	protected String operatingSystem;

	@Column(name = "BROWSER")
	protected String browser;

	@Column(name = "BROWSER_VERSION")
	protected String browserVersion;

	@Column(name = "TEST_SUITE_EXECUTION_NAME")
	protected String testSuiteExecName;
	
	@Column(name = "EXECUTION_STATUS")
	protected String executionStatus;
	
	@Column(name = "NODE_IP_ADDRESS")
	protected String nodeIpAddress;
	
	public String getNodeIpAddress() {
		return nodeIpAddress;
	}

	public void setNodeIpAddress(String nodeIpAddress) {
		this.nodeIpAddress = nodeIpAddress;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getTestSuiteExecName() {
		return testSuiteExecName;
	}

	public void setTestSuiteExecName(String testSuiteExecName) {
		this.testSuiteExecName = testSuiteExecName;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
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

	public Integer getTestSuiteExecId() {
		return testSuiteExecId;
	}

	public void setTestSuiteExecId(Integer testSuiteExecId) {
		this.testSuiteExecId = testSuiteExecId;
	}

	public Date getScheduledOn() {
		return scheduledOn;
	}

	public void setScheduledOn(Date scheduledOn) {
		this.scheduledOn = scheduledOn;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public String getFatalErrorNotificationMail() {
		return fatalErrorNotificationMail;
	}

	public void setFatalErrorNotificationMail(String fatalErrorNotificationMail) {
		this.fatalErrorNotificationMail = fatalErrorNotificationMail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*public TestSuiteDetail getTestSuiteDetail() {
		return testSuiteDetail;
	}

	public void setTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		this.testSuiteDetail = testSuiteDetail;
	}*/

	public TestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}

	public Date getExecCompleteOn() {
		return execCompleteOn;
	}

	public void setExecCompleteOn(Date execCompleteOn) {
		this.execCompleteOn = execCompleteOn;
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

}
