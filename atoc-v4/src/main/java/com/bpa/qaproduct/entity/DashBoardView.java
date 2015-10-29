package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "DASHBOARD_VIEW")
@Immutable
public class DashBoardView {
	@Id
	@Column(name = "PROJECT_ID")
	protected Integer projectId;

	@Column(name = "PROJECT_NAME")
	protected String projectName;

	@Column(name = "TEST_CASE_COUNT")
	protected Long testCaseCount;

	@Column(name = "TEST_SUITE_COUNT")
	protected Long testSuiteCount;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Long getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(Long testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	public Long getTestSuiteCount() {
		return testSuiteCount;
	}

	public void setTestSuiteCount(Long testSuiteCount) {
		this.testSuiteCount = testSuiteCount;
	}

}
