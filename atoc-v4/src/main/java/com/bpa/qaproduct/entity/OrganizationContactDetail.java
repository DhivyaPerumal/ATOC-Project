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
@Table(name = "ORGANIZATION_CONTACT_DETAIL")
public class OrganizationContactDetail {

	@Id
	@Column(name = "ORGANIZATION_CONTACT_DETAIL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer OrgContactDetailId;

	@Column(name = "CONTACT_NAME")
	protected String contactName;

	@Column(name = "CONTACT_EMAIL")
	protected String contactEmail;

	@Column(name = "CONTACT_PHONE")
	protected String contactPhone;

	@Column(name = "CONTACT_DESIGNATION")
	protected String contactDesignation;

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

	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ORGANIZATION_ID")
	protected Organization organization;

	public Integer getOrgContactDetailId() {
		return OrgContactDetailId;
	}

	public void setOrgContactDetailId(Integer orgContactDetailId) {
		OrgContactDetailId = orgContactDetailId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactDesignation() {
		return contactDesignation;
	}

	public void setContactDesignation(String contactDesignation) {
		this.contactDesignation = contactDesignation;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
