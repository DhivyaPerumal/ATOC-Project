package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_SUIT_TEST_CASE_ORDER")
public class TestSuiteTestCaseOrder {

	@ManyToOne
	@JoinColumn(name = "TEST_SUIT_DETAIL_ID", referencedColumnName = "TEST_SUITE_DETAIL_ID")
	protected TestSuiteDetail testSuiterDetail;

	@ManyToOne
	@JoinColumn(name = "TEST_CASE_ID", referencedColumnName = "TEST_CASE_ID")
	protected TestCase testCase;

	@Column(name = "ORDER_NO")
	protected Integer orderNo;

	public TestSuiteDetail getTestSuiterDetail() {
		return testSuiterDetail;
	}

	public void setTestSuiterDetail(TestSuiteDetail testSuiterDetail) {
		this.testSuiterDetail = testSuiterDetail;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}
