package com.isn.services.po;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;

@Entity
public class GroupMessageLock extends MessageLock {

	private List<MessageLock> locks;
	
	@Override
	public boolean unlock() {
		
		boolean result = true;
		if(locks != null){
			for(int i = 0; i < locks.size(); i++){
				MessageLock lock = locks.get(i);
				if(lock != null){
					result &= lock.unlock();
				}
			}
		}
		return result;
	}

	@OneToMany (  
            targetEntity=com.isn.services.po.MessageLock.class,  
            fetch=FetchType.EAGER,  
            cascade = { CascadeType.ALL })  
    @Cascade( { org.hibernate.annotations.CascadeType.ALL } )   
    @JoinTable(name="isn_message_lock_group",   
            joinColumns = @JoinColumn(name = "lock_id"),   
            inverseJoinColumns = @JoinColumn(name = "child_lock_id")) 
	public List<MessageLock> getLocks() {
		return locks;
	}

	public void setLocks(List<MessageLock> locks) {
		this.locks = locks;
	}

}
