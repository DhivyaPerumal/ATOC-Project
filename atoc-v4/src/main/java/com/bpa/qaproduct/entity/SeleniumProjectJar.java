package com.bpa.qaproduct.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "MASTER_JAR")

public class SeleniumProjectJar {

@Id
@Column(name = "JAR_ID")
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer jarId;


@Column(name = "JAR_NAME")
private String jarName;

@Column(name = "STATUS")
private String status;

@Column(name = "CATEGORY")
private String category;


public Integer getJarId() {
	return jarId;
}

public void setJarId(Integer jarId) {
	this.jarId = jarId;
}

public String getJarName() {
	return jarName;
}

public void setJarName(String jarName) {
	this.jarName = jarName;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getCategory() {
	return category;
}

public void setCategory(String category) {
	this.category = category;
}



}
