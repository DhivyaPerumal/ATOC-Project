package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {

	@Id
	@Column(name = "USER_ROLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userRoleId;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	protected User user;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
	protected Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

}
