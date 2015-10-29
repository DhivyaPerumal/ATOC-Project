package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name="TEST_CASE_STEP")
public class TestCaseStep {
	
	@Id
	@Column(name="TEST_CASE_STEP_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer testCaseStepId;
	
	@Column(name = "command")
	protected String command;
	
	@Column(name = "locator")
	protected String locator;
	
	@Column(name = "value")
	protected String value;
	
	@Column(name = "order_no")
	protected Integer order_no;
	
	@Column(name = "CREATED_BY")
	protected Integer createdBy;
	
	@Column(name = "CREATED_ON")
	protected Date createdOn;
	
	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;
	
	@Column(name = "UPDATED_ON")
	protected Date updatedOn;
	
	/*@Column(name = "test_case_id")
	protected Date testCaseId;*/	
	@ManyToOne
	@JoinColumn(name = "test_case_id", referencedColumnName = "TEST_CASE_ID")
	protected TestCase testCase;	

	public Integer getTestCaseStepId() {
		return testCaseStepId;
	}

	public void setTestCaseStepId(Integer testCaseStepId) {
		this.testCaseStepId = testCaseStepId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrder_no() {
		return order_no;
	}

	public void setOrder_no(Integer order_no) {
		this.order_no = order_no;
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

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	
	

}
