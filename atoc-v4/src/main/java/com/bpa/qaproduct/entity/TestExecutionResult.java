package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_EXECUTION_RESULT")
public class TestExecutionResult {

	@Id
	@Column(name = "TEST_EXECUTION_RESULT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testExeResultId;

	@OneToOne
	@JoinColumn(name = "TEST_SUITE_EXECUTION_ID", referencedColumnName = "TEST_SUTIE_EXECUTION_ID")
	protected TestSuiteExecution testSuiteExecution;

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

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	@ManyToOne
	@JoinColumn(name = "TEST_CASE_ID", referencedColumnName = "TEST_CASE_ID")
	protected TestCase testCase;

	@Column(name = "RESULT")
	protected String result;

	@Column(name = "EXECUTED_ON")
	protected Date executedOn;

	@Column(name = "ISACTIVE")
	protected String isActive;

	public Integer getTestExeResultId() {
		return testExeResultId;
	}

	public void setTestExeResultId(Integer testExeResultId) {
		this.testExeResultId = testExeResultId;
	}

	public TestSuiteExecution getTestSuiteExecution() {
		return testSuiteExecution;
	}

	public void setTestSuiteExecution(TestSuiteExecution testSuiteExecution) {
		this.testSuiteExecution = testSuiteExecution;
	}
}
