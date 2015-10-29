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
@Table(name = "ORGANIZATION")
public class Organization {

	@Id
	@Column(name = "ORGANIZATION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer organizationId;

	@Column(name = "ORGANIZATION_NAME")
	protected String organizationName;

	@ManyToOne
	@JoinColumn(name = "APPLICATION_KEY_ID", referencedColumnName = "APPLICATION_KEY_ID")
	protected ApplicationKey applicationKey;

	@Column(name = "PHONE")
	protected String phone;

	@Column(name = "EMAIL")
	protected String email;

	@Column(name = "FAX")
	protected String fax;

	@Column(name = "NOTES")
	protected String notes;

	@Column(name = "PARENT_ORGANIZATION_ID")
	protected Integer parentOrganizatonId;

	@Column(name = "ADDRESS")
	protected String address;

	@Column(name = "CITY")
	protected String city;

	@Column(name = "STATE")
	protected String state;

	@Column(name = "COUNTRY")
	protected String country;

	@Column(name = "ZIP_CODE")
	protected String zipCode;

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

	@Column(name = "ORGANIZATION_LOGO_FILE_NAME")
	protected String organizationLogoFileName;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getParentOrganizatonId() {
		return parentOrganizatonId;
	}

	public void setParentOrganizatonId(Integer parentOrganizatonId) {
		this.parentOrganizatonId = parentOrganizatonId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public String getIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Y")) {
			isActiveString = "Yes";
		} else if (isActive.equalsIgnoreCase("N")) {
			isActiveString = "No";
		}

		return isActiveString;
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

	public String getOrganizationLogoFileName() {
		return organizationLogoFileName;
	}

	public void setOrganizationLogoFileName(String organizationLogoFileName) {
		this.organizationLogoFileName = organizationLogoFileName;
	}

	public ApplicationKey getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(ApplicationKey applicationKey) {
		this.applicationKey = applicationKey;
	}

	public String getApplicationKeyString(Organization organization) {
		String applicationKeyString = "";

		if (organization.getApplicationKey() != null) {
			applicationKeyString = organization.getApplicationKey()
					.getApplicationKey();
		}
		return applicationKeyString;
	}

}
