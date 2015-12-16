package com.isn.services.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isn.services.po.Message;
import com.isn.services.po.User;
import com.isn.services.repo.MessageRepository;
import com.isn.services.repo.UserRepository;

@RestController
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	private MessageRepository repoMessage;
	@Autowired
	private UserRepository repoUser;
	
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
		repoMessage.delete(messageId);;
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/{messageId}/sender/{senderUserId}")
	public void setSender(@PathVariable long messageId, @PathVariable long senderUserId){
		Message message = repoMessage.findOne(messageId);
		User sender = repoUser.findOne(senderUserId);
		message.setSender(sender);
		repoMessage.save(message);
	}
	
	//TODO
	//setLock
	
	//addReceiver
}
