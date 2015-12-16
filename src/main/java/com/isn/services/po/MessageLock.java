package com.isn.services.po;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "isn_message_lock")
@DiscriminatorColumn(name="message_lock_type")
public abstract class MessageLock {

	private long id;
	
	public abstract boolean unlock();

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}
	
}
