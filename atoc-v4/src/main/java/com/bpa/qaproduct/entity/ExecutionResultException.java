package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="executionresult_exception")
public class ExecutionResultException {
	
	@Id
	@Column(name="EXECUTIONRESULT_EXCEPTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer executionResultExceptionId;
	
	/* Changes done for execution result exception in Services STARTS HERE */
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "EXECUTION_RESULT_DETAIL_ID", referencedColumnName = "EXECUTION_RESULT_DETAIL_ID")
	protected ExecutionResultDetail executionResultDetail;
	
	@Column(name="EXECUTIONRESULT_EXCEPTION_METHOD_NAME")
	protected String executionResultExceptionMethodName;
	
	@Column(name="EXECUTIONRESULT_EXCEPTION_FILEPATH")
	protected String executionResultExceptionFilePath;
	
	
	
	public String getExecutionResultExceptionFilePath() {
		return executionResultExceptionFilePath;
	}

	public void setExecutionResultExceptionFilePath(
			String executionResultExceptionFilePath) {
		this.executionResultExceptionFilePath = executionResultExceptionFilePath;
	}

	public String getExecutionResultExceptionMethodName() {
		return executionResultExceptionMethodName;
	}

	public void setExecutionResultExceptionMethodName(
			String executionResultExceptionMethodName) {
		this.executionResultExceptionMethodName = executionResultExceptionMethodName;
	}

	public Integer getExecutionResultExceptionId() {
		return executionResultExceptionId;
	}

	public void setExecutionResultExceptionId(Integer executionResultExceptionId) {
		this.executionResultExceptionId = executionResultExceptionId;
	}

	public ExecutionResultDetail getExecutionResultDetail() {
		return executionResultDetail;
	}

	public void setExecutionResultDetail(ExecutionResultDetail executionResultDetail) {
		this.executionResultDetail = executionResultDetail;
	}
	
}
