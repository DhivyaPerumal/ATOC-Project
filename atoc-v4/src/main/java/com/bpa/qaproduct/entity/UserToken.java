package com.bpa.qaproduct.entity;

import java.nio.charset.Charset;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.codec.Base64;

@Entity
@Table(name = "user_tokens")
public class UserToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_TOKEN_ID")
	private Integer userTokenId;

	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "EXPIRE_TIME")
	private Timestamp expireTime;

	public Integer getUserTokenId() {
		return userTokenId;
	}

	public void setUserTokenId(Integer userTokenId) {
		this.userTokenId = userTokenId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Timestamp getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}

	public String getEncodedToken() {
		String delimiter = ":";
		String token = getUserTokenId() + delimiter + getUserId() + delimiter
				+ getToken() + delimiter + getExpireTime();
		return base64Encode(token);
	}

	public String getDecodedToken(String token) {
		return base64Decode(token);
	}

	public static String base64Encode(String token) {
		byte[] encodedBytes = Base64.encode(token.getBytes());
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

	public static String base64Decode(String token) {
		byte[] decodedBytes = Base64.decode(token.getBytes());
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}

}
