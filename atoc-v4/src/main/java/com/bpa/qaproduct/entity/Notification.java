package com.bpa.qaproduct.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity

@Table(name = "NOTIFICATION")

public class Notification {

	@Id
	@Column(name = "NOTIFICATION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificaitonId;
	
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	@JsonBackReference
	protected  User user;
	
	
	 public User getUser() {
		 return user;
		 }

		 public void setUserId(User user) {
		 this.user = user;
		 }

	@Column(name = "NOTIFICATION_MESSAGE")
	private String notificationMessage;
	
	@Column(name = "NOTIFICATION_TITLE")
	private String notificationTitle;
	
	@Column(name = "NOTIFICATION_STATUS")
	private String notificationStatus;
	
	@Column(name = "CREATED_ON")
    private Date createdOn;
    
	@Column(name = "UPDATED_ON")
    private Date updatedOn;
    
	@Column(name = "CREATED_BY")
    private Integer createdBy;
    
	@Column(name = "UPDATED_BY")
    private Integer updatedBy;

	public Integer getNotificaitonId() {
		return notificaitonId;
	}

	public void setNotificaitonId(Integer notificaitonId) {
		this.notificaitonId = notificaitonId;
	}



	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
