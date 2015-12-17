package com.isn.services.po;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "isn_friend")
public class Friend {
	
	private long id;
	private String alias;
	private String tag;
	private String description;
	private String mobile;
	private String email;
	private User owner;
	private List<Message> inmessages;

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
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToOne(
			targetEntity=com.isn.services.po.User.class, 
			fetch=FetchType.EAGER)
	@JoinColumn(name="owner_user_id", nullable=false)
	public User getOwner() {
		return owner;
	}

	@JsonBackReference(value="owner")
	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToMany (  
            targetEntity=com.isn.services.po.Message.class,  
            fetch=FetchType.EAGER)  
    @JoinTable(name="isn_message_receiver",   
            joinColumns = @JoinColumn(name = "friend_id"),   
            inverseJoinColumns = @JoinColumn(name = "message_id")) 
	public List<Message> getInmessages() {
		return inmessages;
	}

	@JsonBackReference(value="inmessages")
	public void setInmessages(List<Message> inmessages) {
		this.inmessages = inmessages;
	}
}
