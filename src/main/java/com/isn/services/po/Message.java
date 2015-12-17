package com.isn.services.po;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "isn_message")
public class Message {

	private long id;
	private String title;
	private String ref;
	private String tag;
	private Date sendTime;
	private MessageState state;
	private MessageLock lock;
	private ContentType contentType;
	private PrivacyType privacyType;
	private User sender;
	private List<Friend> receivers;
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Column
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Column
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Column
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public MessageState getState() {
		return state;
	}

	public void setState(MessageState state) {
		this.state = state;
	}

	@OneToOne(  
            targetEntity = com.isn.services.po.MessageLock.class,   
            fetch = FetchType.EAGER,   
            cascade = { CascadeType.ALL })  
    @Cascade( { org.hibernate.annotations.CascadeType.ALL } )   
	@JoinColumn(name = "message_lock_id", nullable=true) 
	public MessageLock getLock() {
		return lock;
	}

	public void setLock(MessageLock lock) {
		this.lock = lock;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public PrivacyType getPrivacyType() {
		return privacyType;
	}

	public void setPrivacyType(PrivacyType privacyType) {
		this.privacyType = privacyType;
	}

	@ManyToOne(  
            targetEntity = com.isn.services.po.User.class,   
            fetch = FetchType.EAGER)   
	@JoinColumn(name = "sernder_user_id") 
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	@ManyToMany (  
            targetEntity=com.isn.services.po.Friend.class,  
            fetch=FetchType.EAGER)    
    @JoinTable(name="isn_message_receiver",   
            joinColumns = @JoinColumn(name = "message_id"),   
            inverseJoinColumns = @JoinColumn(name = "friend_id")) 
	public List<Friend> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Friend> receivers) {
		this.receivers = receivers;
	}

	@Column
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
