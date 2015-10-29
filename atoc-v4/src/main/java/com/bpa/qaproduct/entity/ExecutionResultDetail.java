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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "EXECUTION_RESULT_DETAIL")
public class ExecutionResultDetail {

	@Id
	@Column(name = "EXECUTION_RESULT_DETAIL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer executionResultDetailId;

	@ManyToOne
	@JoinColumn(name = "EXECUTION_RESULT_ID", referencedColumnName = "EXECUTION_RESULT_ID")
	@JsonBackReference
	protected ExecutionResult executionResult;

	@OneToMany(mappedBy = "executionResultDetail", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	protected Set<ExecutionMethodParameter> executionMethodParameters;
	
	
	/* Changes done for execution result exception in Services ADDED HERE */
	
	@OneToMany(mappedBy = "executionResultDetail",fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	protected Set<ExecutionResultException> executionResultExceptions;
	
	/*@OneToOne
	@JoinColumn(name = "EXECUTIONRESULT_EXCEPTION_ID", referencedColumnName = "EXECUTIONRESULT_EXCEPTION_ID")
	protected ExecutionResultException executionResultException;*/
	
	/*@OneToMany(mappedBy = "executionResultDetail", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	protected Set<ExecutionMethodParameter> executionMethodParameter;

	public Set<ExecutionMethodParameter> getExecutionMethodParameter() {
		return executionMethodParameter;
	}

	public void setExecutionMethodParameter(
			Set<ExecutionMethodParameter> executionMethodParameter) {
		this.executionMethodParameter = executionMethodParameter;
	}*/
	
	
	
	
	@Column(name = "TEST_METHOD_NAME")
	protected String testMethodName;
	
	
	public Set<ExecutionResultException> getExecutionResultExceptions() {
		return executionResultExceptions;
	}

	public void setExecutionResultExceptions(
			Set<ExecutionResultException> executionResultExceptions) {
		this.executionResultExceptions = executionResultExceptions;
	}

	@Column(name = "DURATION")
	protected String duration;

	@Column(name = "STATUS")
	protected String status;

	@Column(name = "STARTED_AT")
	protected Date startedAt;

	@Column(name = "FINISHED_AT")
	protected Date finishedAt;

	@Column(name = "DESCRIPTION")
	protected String description;

	@Column(name = "SIGNATURE")
	protected String signature;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	public Set<ExecutionMethodParameter> getExecutionMethodParameters() {
		return executionMethodParameters;
	}

	public void setExecutionMethodParameters(
			Set<ExecutionMethodParameter> executionMethodParameters) {
		this.executionMethodParameters = executionMethodParameters;
	}

	public String getTestMethodName() {
		return testMethodName;
	}

	public void setTestMethodName(String testMethodName) {
		this.testMethodName = testMethodName;
	}

	public String getStatus() {
		return status;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public Integer getExecutionResultDetailId() {
		return executionResultDetailId;
	}

	public void setExecutionResultDetailId(Integer executionResultDetailId) {
		this.executionResultDetailId = executionResultDetailId;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public ExecutionResult getExecutionResult() {
		return executionResult;
	}

	public void setExecutionResult(ExecutionResult executionResult) {
		this.executionResult = executionResult;
	}

}
