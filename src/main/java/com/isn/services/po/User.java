package com.isn.services.po;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	private List<Message> outmessages;
	private List<MessageComment> comments;
	private MessageBox inBox;
	private MessageBox outBox;

	@Column  
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable=false)
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
            mappedBy = "owner")  
	public List<Friend> getFriends() {
		return friends;
	}

	@JsonBackReference(value="friends")
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	@OneToMany ( 
            fetch=FetchType.EAGER, 
            mappedBy = "sender")  
	public List<Message> getOutmessages() {
		return outmessages;
	}

	@JsonBackReference(value="outmessages")
	public void setOutmessages(List<Message> outmessages) {
		this.outmessages = outmessages;
	}

	@OneToMany ( 
            fetch=FetchType.EAGER, 
            mappedBy = "commenter")
	public List<MessageComment> getComments() {
		return comments;
	}

	@JsonBackReference(value="comments")
	public void setComments(List<MessageComment> comments) {
		this.comments = comments;
	}

	@OneToOne(
			targetEntity=com.isn.services.po.MessageBox.class, 
			fetch=FetchType.EAGER)
	@JoinColumn(name="message_inbox_id")
	public MessageBox getInBox() {
		return inBox;
	}

	@JsonBackReference(value="inBox")
	public void setInBox(MessageBox inBox) {
		this.inBox = inBox;
	}

	@OneToOne(
			targetEntity=com.isn.services.po.MessageBox.class, 
			fetch=FetchType.EAGER)
	@JoinColumn(name="message_outbox_id")
	public MessageBox getOutBox() {
		return outBox;
	}

	@JsonBackReference(value="outBox")
	public void setOutBox(MessageBox outBox) {
		this.outBox = outBox;
	}
}
