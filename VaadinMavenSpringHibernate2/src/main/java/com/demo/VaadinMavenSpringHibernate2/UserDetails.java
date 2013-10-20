package com.demo.VaadinMavenSpringHibernate2;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity  // Now Hibernate knows that this class needs to be saved.
@Table (name = "New_User_Details")
public class UserDetails {

	@Id  // The value of this field will be the primary key.
	@Column (name = "NewId")
	private int userId;
	
	@Column (name = "NewUserName")
	private String userName;
	
	@Basic // By default and not necessary, field will be saved in a column.
	private String description;
	
	@Transient // This field will not be saved in the database.
	private String address;
	
	@Temporal (TemporalType.DATE) // This will save only the date without time.
	private Date dateJoined;
	
	@Lob  // This is a Large OBject
	private String veryLongDescription;
	
	public String getVeryLongDescription() {
		return veryLongDescription;
	}
	public void setVeryLongDescription(String veryLongDescription) {
		this.veryLongDescription = veryLongDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDateJoined() {
		return dateJoined;
	}
	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
