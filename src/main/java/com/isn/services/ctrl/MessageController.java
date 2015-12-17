package com.isn.services.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isn.services.po.Friend;
import com.isn.services.po.Message;
import com.isn.services.po.MessageLock;
import com.isn.services.po.User;
import com.isn.services.repo.FriendRepository;
import com.isn.services.repo.MessageLockRepository;
import com.isn.services.repo.MessageRepository;
import com.isn.services.repo.UserRepository;

@RestController
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	private MessageRepository repoMessage;
	@Autowired
	private UserRepository repoUser;
	@Autowired
	private FriendRepository repoFriend;
	@Autowired
	private MessageLockRepository repoMessageLock;
	
	@RequestMapping(method=RequestMethod.GET,path="/{messageId}", produces="application/json")
    public Message get(@PathVariable long messageId){
		return repoMessage.findOne(messageId);
	}
	
	@RequestMapping(method=RequestMethod.POST,path="",consumes="application/json")
    public long create(@RequestBody Message message){
		message = repoMessage.save(message);
		return message.getId();
    }
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{messageId}")
    public void delete(@PathVariable long messageId){
		repoMessage.delete(messageId);
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/{messageId}/sender/{senderUserId}")
	public void setSender(@PathVariable long messageId, @PathVariable long senderUserId){
		Message message = repoMessage.findOne(messageId);
		if(message != null){
			User sender = repoUser.findOne(senderUserId);
			message.setSender(sender);
			repoMessage.save(message);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/{messageId}/receivers/{receiverFriendId}")
    public void addReceiver(@PathVariable long messageId, @PathVariable long receiverFriendId) {
		Message message = repoMessage.findOne(messageId);
		if(message != null){
			Friend friend = repoFriend.findOne(receiverFriendId);
			if(friend != null){
				if(message.getReceivers() == null){
					message.setReceivers(new ArrayList<Friend>());
				}
				message.getReceivers().add(friend);
				repoMessage.save(message);
			}
		}
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/{messageId}/locks",consumes="application/json")
    public long createMyLock(@PathVariable long messageId, @RequestBody MessageLock lock) {
		Message message = repoMessage.findOne(messageId);
		if(message != null){
			if(message.getLocks() == null){
				message.setLocks(new ArrayList<MessageLock>());
			}
			if(lock != null){
				lock.setOwner(message);
				repoMessageLock.save(lock);
			}
			return lock.getId();
		}
		
		return 0;
	}
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{messageId}/locks/{lockId}")
    public void deleteMyLock(@PathVariable long messageId, @PathVariable long lockId){
		Message message = repoMessage.findOne(messageId);
		if(message != null){
			List<MessageLock> locks = message.getLocks();
			if(locks != null){
				for(int i = 0; i < locks.size(); i++){
					MessageLock lock = locks.get(i);
					if(lock != null && lock.getId() == lockId){
						locks.remove(i);
						repoMessageLock.delete(lock);
						break;
					}
				}
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{messageId}/locks", produces="application/json")
    public List<MessageLock> getMyLocks(@PathVariable long messageId){
		Message message = repoMessage.findOne(messageId);
		return message.getLocks();
	}
}
