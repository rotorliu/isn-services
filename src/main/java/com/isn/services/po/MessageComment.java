package com.isn.services.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "isn_message_comment")
public class MessageComment {

	private long id;
	private Date time;
	private String content;
	private User commenter;
	private Message owner;
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Column(nullable=false)
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne(
			targetEntity=com.isn.services.po.Message.class, 
			fetch=FetchType.EAGER)
	@JoinColumn(name="owner_message_id", nullable=false)
	public Message getOwner() {
		return owner;
	}
	
	@JsonBackReference(value="owner")
	public void setOwner(Message owner) {
		this.owner = owner;
	}

	@ManyToOne(  
            targetEntity = com.isn.services.po.User.class,   
            fetch = FetchType.EAGER)   
	@JoinColumn(name = "commenter_user_id", nullable=false) 
	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}
}
