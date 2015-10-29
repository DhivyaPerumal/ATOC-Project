package com.bpa.qaproduct.entity;

import java.sql.Blob;
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
@Table(name = "TEST_CASE")
public class TestCase {
	@Id
	@Column(name = "TEST_CASE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testCaseId;

	@Column(name = "TEST_CASE_NAME")
	protected String testCaseName;

	@Column(name = "TEST_CASE_DETAIL")
	protected String testCaseDetail;

	@Column(name = "NOTES")
	protected String notes;

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

	/*
	 * @Column(name = "TEST_SUITE_DETAIL_ID") protected Integer
	 * testSuiteDetailId;
	 */
	@ManyToOne
	@JoinColumn(name = "TEST_SUITE_DETAIL_ID", referencedColumnName = "TEST_SUITE_DETAIL_ID")
	protected TestSuiteDetail testSuiteDetail;

	@Column(name = "TESTCASE_FILENAME")
	protected String testCaseFileName;

	@Column(name = "TESTCASE_FILETYPE")
	protected String testCaseFileType;

	@Column(name = "TESTCASE_UPLOADED_PATH")
	protected String testCaseUploadedPath;

	@Column(name = "TEST_CASE_URL")
	protected String testCaseUrl;

	public String getTestCaseUrl() {
		return testCaseUrl;
	}

	public void setTestCaseUrl(String testCaseUrl) {
		this.testCaseUrl = testCaseUrl;
	}

	public String getTestCaseUploadedPath() {
		return testCaseUploadedPath;
	}

	public void setTestCaseUploadedPath(String testCaseUploadedPath) {
		this.testCaseUploadedPath = testCaseUploadedPath;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getTestCaseDetail() {
		return testCaseDetail;
	}

	public void setTestCaseDetail(String testCaseDetail) {
		this.testCaseDetail = testCaseDetail;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public TestSuiteDetail getTestSuiteDetail() {
		return testSuiteDetail;
	}

	public void setTestSuiteDetail(TestSuiteDetail testSuiteDetail) {
		this.testSuiteDetail = testSuiteDetail;
	}

	public String getTestCaseFileName() {
		return testCaseFileName;
	}

	public void setTestCaseFileName(String testCaseFileName) {
		this.testCaseFileName = testCaseFileName;
	}

	public String getTestCaseFileType() {
		return testCaseFileType;
	}

	public void setTestCaseFileType(String testCaseFileType) {
		this.testCaseFileType = testCaseFileType;
	}

	/*
	 * private Integer testCaseId; private String testCaseName; private String
	 * testCaseFileName;
	 * 
	 * public Integer getTestCaseId() { return testCaseId; } public void
	 * setTestCaseId(Integer testCaseId) { this.testCaseId = testCaseId; }
	 * public String getTestCaseName() { return testCaseName; } public void
	 * setTestCaseName(String testCaseName) { this.testCaseName = testCaseName;
	 * } public String getTestCaseFileName() { return testCaseFileName; } public
	 * void setTestCaseFileName(String testCaseFileName) { this.testCaseFileName
	 * = testCaseFileName; }
	 */

}
