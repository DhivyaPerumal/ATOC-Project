package com.bpa.qaproduct.entity;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;

@Entity
@Table(name="emailjobtable")

public class EmailJob {

	@Id
	@Column(name = "EMAIL_JOB_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emailjobid;
	
  
	@Transient
	protected byte[] jobDataByteArray;
	
	
	
	@Column(name = "EMAIL_TYPE")
	protected String emailType;
	
	
	
	@Column(name = "EMAIL_MAP_OBJECT", columnDefinition="blob")
	private Blob jobDataMapObject;



	public Integer getEmailjobid() {
		return emailjobid;
	}



	public void setEmailjobid(Integer emailjobid) {
		this.emailjobid = emailjobid;
	}



	public byte[] getJobDataByteArray() {
		return jobDataByteArray;
	}



	public void setJobDataByteArray(byte[] jobDataByteArray) {
		this.jobDataByteArray = jobDataByteArray;
	}



	public String getEmailType() {
		return emailType;
	}



	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}



	public Blob getJobDataMapObject() {
		return jobDataMapObject;
	}



	public void setJobDataMapObject(Blob jobDataMapObject) {
		this.jobDataMapObject = jobDataMapObject;
	}
	
	
	

	
}
