package com.bpa.qaproduct.entity;

import java.sql.Time;
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
@Table(name = "SCHEDULER_EXEC")
public class SchedulerExecution {
	
	@Id
	@Column(name = "SCHEDULE_EXEC_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer schedulerExecutionId;
	
	@ManyToOne
	@JoinColumn(name="TEST_SUTIE_EXECUTION_ID", referencedColumnName="TEST_SUTIE_EXECUTION_ID")
	@JsonBackReference
	private TestSuiteExecution schedulerTestSuiteExecution;
	
	@Column(name="SCHEDULE_DATE")
	private Date scheduleDate;
	
	@Column(name="SCHEDULE_TIME")
	private Time scheduleTime;
	
	@Column(name = "SCHEDULE_TIMEZONE")
	private String scheduleTimeZone;
	
	@Column(name="STATUS")
	private String status;

	public String getScheduleTimeZone() {
		return scheduleTimeZone;
	}

	public void setScheduleTimeZone(String scheduleTimeZone) {
		this.scheduleTimeZone = scheduleTimeZone;
	}

	public Integer getSchedulerExecutionId() {
		return schedulerExecutionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSchedulerExecutionId(Integer schedulerExecutionId) {
		this.schedulerExecutionId = schedulerExecutionId;
	}

	

	public TestSuiteExecution getSchedulerTestSuiteExecution() {
		return schedulerTestSuiteExecution;
	}

	public void setSchedulerTestSuiteExecution(
			TestSuiteExecution schedulerTestSuiteExecution) {
		this.schedulerTestSuiteExecution = schedulerTestSuiteExecution;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Time getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Time scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
	
	

}
