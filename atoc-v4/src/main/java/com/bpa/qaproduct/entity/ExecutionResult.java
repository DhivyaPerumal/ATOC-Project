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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "EXECUTION_RESULT")
public class ExecutionResult {

	@Id
	@Column(name = "EXECUTION_RESULT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer executionResultId;

	@OneToOne
	@JoinColumn(name = "TEST_SUITE_EXECUTION_ID", referencedColumnName = "TEST_SUTIE_EXECUTION_ID", updatable = false)
	@JsonBackReference
	protected TestSuiteExecution testSuiteExecution;

	@OneToMany(mappedBy = "executionResult", fetch = FetchType.EAGER)
	protected Set<ExecutionResultDetail> executionResultDetails;

	@OneToMany(mappedBy = "executionResult", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	protected Set<ExecutionResultDetail> executionResultDetail;

	public Set<ExecutionResultDetail> getExecutionResultDetail() {
		return executionResultDetail;
	}

	public void setExecutionResultDetail(
			Set<ExecutionResultDetail> executionResultDetail) {
		this.executionResultDetail = executionResultDetail;
	}

	@Column(name = "TEST_EXECUTION_NAME")
	protected String testExecutionName;

	@Column(name = "START_TIME")
	protected Date startTime;

	@Column(name = "END_TIME")
	protected Date endTime;

	@Column(name = "DURATION")
	protected String duration;

	@Column(name = "TOTAL")
	protected int total;

	@Column(name = "PASSED")
	protected int passed;

	@Column(name = "FAILED")
	protected int failed;

	@Column(name = "SKIPPED")
	protected int skipped;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	@Column(name = "EXECUTION_STATUS")
	protected String executionStatus;

	public Set<ExecutionResultDetail> getExecutionResultDetails() {
		return executionResultDetails;
	}

	public void setExecutionResultDetails(
			Set<ExecutionResultDetail> executionResultDetails) {
		this.executionResultDetails = executionResultDetails;
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

	public Integer getExecutionResultId() {
		return executionResultId;
	}

	public void setExecutionResultId(Integer executionResultId) {
		this.executionResultId = executionResultId;
	}

	public TestSuiteExecution getTestSuiteExecution() {
		return testSuiteExecution;
	}

	public void setTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		this.testSuiteExecution = testSuiteExecution;
	}

	public String getTestExecutionName() {
		return testExecutionName;
	}

	public void setTestExecutionName(String testExecutionName) {
		this.testExecutionName = testExecutionName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

}
