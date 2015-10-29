package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_EXECUTION_RESULT_DETAIL")
public class TestExecutionResultDetail {

	@Id
	@Column(name = "TEST_EXECUTION_RESULT_DETAIL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testExeResultDetailId;

	@Column(name = "PARAMETER_NAME")
	protected String parameterName;

	@Column(name = "PARAMETER_VALUE")
	protected String parameterValue;

	@OneToOne
	@JoinColumn(name = "TEST_EXECUTION_RESULT_ID", referencedColumnName = "TEST_EXECUTION_RESULT_ID")
	protected TestExecutionResult testExecutionResult;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

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

	public Integer getTestExeResultDetailId() {
		return testExeResultDetailId;
	}

	public void setTestExeResultDetailId(Integer testExeResultDetailId) {
		this.testExeResultDetailId = testExeResultDetailId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public TestExecutionResult getTestExecutionResult() {
		return testExecutionResult;
	}

	public void setTestExecutionResult(TestExecutionResult testExecutionResult) {
		this.testExecutionResult = testExecutionResult;
	}

}
