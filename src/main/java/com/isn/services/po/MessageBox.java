package com.isn.services.po;

import java.util.ArrayList;
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
import javax.persistence.Table;

@Entity
@Table(name = "isn_message_box")
public class MessageBox {

	private long id;
	private List<Message> messages;
	
	public MessageBox(){
		this.messages = new ArrayList<Message>();
	}
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@ManyToMany(
			targetEntity=com.isn.services.po.Message.class, 
			fetch=FetchType.EAGER)
	@JoinTable(name="isn_message_box_message",   
    			joinColumns = @JoinColumn(name = "message_box_id"),   
    			inverseJoinColumns = @JoinColumn(name = "message_id"))  
	public List<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
