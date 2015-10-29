package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_REGISTRATION")
public class CustomerRegistration {

	@Id
	@Column(name = "CUSTOMER_REGISTRATION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerRegistrationId;

	@Column(name = "FIRSTNAME")
	protected String firstname;

	@Column(name = "LASTNAME")
	protected String lastname;

	@Column(name = "NOTES")
	protected String notes;

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

	@Column(name = "CONTACT_NAME")
	protected String contactName;

	@Column(name = "CONTACT_EMAIL")
	protected String contactEmail;

	@Column(name = "CONTACT_PHONE")
	protected String contactPhone;

	@Column(name = "ORGANIZATION_NAME")
	protected String organizationName;
	
	

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	@Column(name = "CUSTOMER_WEB_SITE")
	protected String customerWebsite;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "APPROVAL_STATUS")
	protected String approvalStatus;

	@Column(name = "ISADMIN")
	protected String isAdmin;

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getCustomerRegistrationId() {
		return customerRegistrationId;
	}

	public void setCustomerRegistrationId(Integer customerRegistrationId) {
		this.customerRegistrationId = customerRegistrationId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public String getCustomerWebsite() {
		return customerWebsite;
	}

	public void setCustomerWebsite(String customerWebsite) {
		this.customerWebsite = customerWebsite;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}
	
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getIsAdminString(String isAdmin) {
		String isAdminString = "";

		if (isAdmin.equalsIgnoreCase("Org Admin")) {
			isAdminString = "yes";
		} else if (isAdmin.equalsIgnoreCase("Org User")) {
			isAdminString = "no";
		} else {
			isAdminString = "";
		}

		return isAdminString;
	}

	public String setIsAdminString(String isAdmin) {
		String isAdminString = "";

		if (isAdmin.equalsIgnoreCase("yes")) {
			isAdminString = "Org Admin";

		} else if (isAdmin.equalsIgnoreCase("no")) {
			isAdminString = "Org User";
		} else {
			isAdminString = "";
		}

		return isAdminString;
	}

}
