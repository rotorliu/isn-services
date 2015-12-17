package com.isn.services.po;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;  

@Entity
@Table(name = "isn_message_lock")
@DiscriminatorColumn(name="type")
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,include=As.PROPERTY,property="type")  
@JsonSubTypes({@Type(value=TimeMessageLock.class,name="TimeMessageLock"),@Type(value=QuestionMessageLock.class,name="QuestionMessageLock")}) 
public abstract class MessageLock {

	private long id;
	private Message owner;
	
	public abstract boolean unlock();

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
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
	
}
