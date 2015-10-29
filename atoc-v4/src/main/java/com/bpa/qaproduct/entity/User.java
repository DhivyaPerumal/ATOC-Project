package com.bpa.qaproduct.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "FIRST_NAME")
	protected String firstName;

	@Column(name = "LAST_NAME")
	protected String lastName;

	@Column(name = "USER_EMAIL")
	protected String userEmail;

	@Column(name = "USER_PASSWORD")
	protected String userPassword;

	@Column(name = "ISACTIVE")
	protected String isActive;

	@Column(name = "CREATED_BY")
	protected Integer createdBy;

	@Column(name = "CREATED_ON")
	protected Date createdOn;

	@Column(name = "UPDATED_BY")
	protected Integer updatedBy;

	@Column(name = "COLOR_SKIN")
	protected String colorSkin;
	
	@Column(name = "PROFILE_IMAGE_DATA")
	@Lob
	protected byte[] profileImgData;
	
	
	public String getColorSkin() {
		return colorSkin;
	}

	public void setColorSkin(String colorSkin) {
		this.colorSkin = colorSkin;
	}

	@ManyToOne
	@JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ORGANIZATION_ID")
	protected Organization organization;
	
	
	@OneToMany (mappedBy="user", fetch = FetchType.EAGER)
	@Cascade({ CascadeType.DELETE })
	private List<UserRole> userRoles;
	
	@Column(name = "UPDATED_ON")
	protected Date updatedOn;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
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

	public String getIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Y")) {
			isActiveString = "Yes";
		} else if (isActive.equalsIgnoreCase("N")) {
			isActiveString = "No";
		} else {
			isActiveString = "No";
		}

		return isActiveString;
	}

	public String setIsActiveString(String isActive) {
		String isActiveString = "";

		if (isActive.equalsIgnoreCase("Yes")) {
			isActiveString = "Y";
		} else if (isActive.equalsIgnoreCase("No")) {
			isActiveString = "N";
		}
		return isActiveString;
	}

	public String getOrgAddressString(String organizationAddress) {
		String orgAddress = "Not Mentioned";

		if (organizationAddress.isEmpty() || organizationAddress == null) {
			orgAddress = "Not Mentioned";
		} else {
			orgAddress = organizationAddress;
		}
		return orgAddress;
	}

	public byte[] getProfileImgData() {
		return profileImgData;
	}

	public void setProfileImgData(byte[] profileImgData) {
		this.profileImgData = profileImgData;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	
	
}
