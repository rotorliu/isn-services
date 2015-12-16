package com.isn.services.po;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "isn_user")
public class User {
	
	private long id;
	private String name;
	private String password;
	private String mobile;
	private Gender gender;
	private Calendar birthday;
	private String email;
	private List<Friend> friends;

	@Column  
	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO) 
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Column 
	@Temporal(TemporalType.DATE)  
	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany ( 
            fetch=FetchType.EAGER, 
            cascade = { CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH },
            mappedBy = "owner")  
	public List<Friend> getFriends() {
		return friends;
	}

	@JsonBackReference
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
	
}
