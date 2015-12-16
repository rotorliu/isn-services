package com.isn.services.po;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "isn_friend")
public class Friend {
	
	private long id;
	/*
	 * 昵称
	 */
	private String alias;
	/*
	 * 标签：例如：朋友，亲人等
	 */
	private String tag;
	/*
	 * 朋友的User，当朋友是一个虚拟或尚不存在的人的时候，可能为空。例如：未来的女儿
	 */
	private User user;
	private String description;
	/*
	 * 当创建朋友时，用户并不确定该朋友是不是系统用户，后台要将mobile和email等联系方式在恰当的时候与User联系起来，
	 * 就像微信提示用户，他的通讯录里的人已是微信用户，可以邀请他成为你的好友。
	 */
	private String mobile;
	private String email;
	private User owner;

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

	@OneToOne(  
            targetEntity = com.isn.services.po.User.class,   
            fetch = FetchType.EAGER,   
            cascade = { CascadeType.ALL })  
    @Cascade( { org.hibernate.annotations.CascadeType.ALL } )   
	@JoinColumn(name = "user_id", nullable=true) 
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(
			targetEntity=com.isn.services.po.User.class, 
			fetch=FetchType.EAGER)
	@JoinColumn(name="owner_user_id", nullable=false)
	public User getOwner() {
		return owner;
	}

	@JsonBackReference
	public void setOwner(User owner) {
		this.owner = owner;
	}
}
