package com.mgr.training.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "USER")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private String userId;	
	@Column(name = "name", unique = true, nullable = false)
	private String displayName;	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false)
	private Date createdOn;
	@Column(name = "password")
	private byte[] password;

	public static final String ANONYMOUS_USERNAME = "anonymous";

	public static User anonymous() {
		User user = new User();
		user.setUserId(ANONYMOUS_USERNAME);
		user.setCreatedOn(new Date(0));
		user.setDisplayName("guest");
		return user;
	}
	
	@PrePersist 
	protected void onCreate(){
		createdOn = new Date();
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}	
	public static String getAnonymousUsername() {
		return ANONYMOUS_USERNAME;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
}