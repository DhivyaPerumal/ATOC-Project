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

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "EXECUTION_METHOD_PARAMETER")
public class ExecutionMethodParameter {

	@Id
	@Column(name = "EXECUTION_PARAMETER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer executionParameterId;

	@ManyToOne
	@JoinColumn(name = "EXECUTION_RESULT_DETAIL_ID", referencedColumnName = "EXECUTION_RESULT_DETAIL_ID", updatable = false)
	@JsonBackReference
	protected ExecutionResultDetail executionResultDetail;

	@Column(name = "PARAMETER_INDEX")
	protected int parameterIndex;

	@Column(name = "PARAMETER_VALUE")
	protected String parameterValue;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	public Integer getExecutionParameterId() {
		return executionParameterId;
	}

	public void setExecutionParameterId(Integer executionParameterId) {
		this.executionParameterId = executionParameterId;
	}

	public ExecutionResultDetail getExecutionResultDetail() {
		return executionResultDetail;
	}

	public void setExecutionResultDetail(
			ExecutionResultDetail executionResultDetail) {
		this.executionResultDetail = executionResultDetail;
	}

	public int getParameterIndex() {
		return parameterIndex;
	}

	public void setParameterIndex(int parameterIndex) {
		this.parameterIndex = parameterIndex;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
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
