package com.isn.services.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isn.services.po.MessageComment;
import com.isn.services.po.Friend;
import com.isn.services.po.IdResult;
import com.isn.services.po.Message;
import com.isn.services.po.MessageBox;
import com.isn.services.po.MessageLock;
import com.isn.services.po.User;
import com.isn.services.repo.FriendRepository;
import com.isn.services.repo.MessageBoxRepository;
import com.isn.services.repo.MessageCommentRepository;
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
	@Autowired
	private MessageCommentRepository repoMessageComment;
	@Autowired
	private MessageBoxRepository repoMessageBox;
	
	@RequestMapping(method=RequestMethod.GET,path="/{messageId}", produces="application/json")
    public Message get(@PathVariable long messageId){
		return repoMessage.findOne(messageId);
	}
	
	@RequestMapping(method=RequestMethod.POST,path="",consumes="application/json")
    public IdResult create(@RequestBody Message message){
		message = repoMessage.save(message);
		return new IdResult(message.getId());
    }
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{messageId}")
    public void delete(@PathVariable long messageId){
		repoMessage.delete(messageId);
	}
	
	@RequestMapping(method=RequestMethod.PUT,path="/{messageId}/sender/{senderUserId}")
	public void setSender(@PathVariable long messageId, @PathVariable long senderUserId){
		Message message = repoMessage.findOne(messageId);
		if(message != null && message.getSender() == null){
			User sender = repoUser.findOne(senderUserId);
			if(sender != null){
				message.setSender(sender);
				repoMessage.save(message);
				
				MessageBox mOutBox = sender.getOutBox();
				if(sender.getOutBox() == null){
					mOutBox = new MessageBox();
					sender.setOutBox(mOutBox);
				}
				mOutBox.getMessages().add(message);
				repoMessageBox.save(mOutBox);
				repoUser.save(sender);
			}
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
				
				if(!message.getReceivers().contains(friend)){
					message.getReceivers().add(friend);
					repoMessage.save(message);
					
					User user = concretize(friend);
					if(user != null){
						MessageBox mInBox = user.getInBox();
						if(mInBox == null){
							mInBox = new MessageBox();
							user.setInBox(mInBox);
						}
						mInBox.getMessages().add(message);
						repoMessageBox.save(mInBox);
						repoUser.save(user);
					}
				}
			}
		}
    }
	
	private User concretize(Friend friend){
		return repoUser.findByMobile(friend.getMobile());
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/{messageId}/locks",consumes="application/json")
    public IdResult createMyLock(@PathVariable long messageId, @RequestBody MessageLock lock) {
		Message message = repoMessage.findOne(messageId);
		if(message != null){
			if(lock != null){
				lock.setOwner(message);
				lock = repoMessageLock.save(lock);
			}
			return new IdResult(lock.getId());
		}
		
		return new IdResult(0);
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
	
	@RequestMapping(method=RequestMethod.POST,path="/{messageId}/commenters/{commenterUserId}/comments",consumes="application/json")
    public IdResult createMyComment(@PathVariable long messageId, @PathVariable long commenterUserId, @RequestBody MessageComment comment) {
		Message message = repoMessage.findOne(messageId);
		User commenter = repoUser.findOne(commenterUserId);
		if(message != null && commenter != null){
			if(comment != null){
				comment.setOwner(message);
				comment.setCommenter(commenter);
				comment = repoMessageComment.save(comment);
			}
			return new IdResult(comment.getId());
		}
		
		return new IdResult(0);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{messageId}/commenters/{commenterUserId}/comments/{commentId}")
    public void deleteMyComment(@PathVariable long messageId, @PathVariable long commenterUserId, @PathVariable long commentId){
		Message message = repoMessage.findOne(messageId);
		User commenter = repoUser.findOne(commenterUserId);
		if(message != null && commenter != null){
			List<MessageComment> comments = message.getComments();
			if(comments != null){
				for(int i = 0; i < comments.size(); i++){
					MessageComment comment = comments.get(i);
					if(comment != null && comment.getId() == commentId){
						User uc = comment.getCommenter();
						if(uc == commenter){
							comments.remove(i);
							repoMessageComment.delete(comment);
							
						}
						break;
					}
				}
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{messageId}/comments", produces="application/json")
    public Page<MessageComment> getMyComments(@PathVariable long messageId, Pageable pageRequest){
//		Message message = repoMessage.findOne(messageId);
//		return message.getComments();
		return repoMessageComment.findMessageCommentsOrderByTimeDesc(messageId, pageRequest);
	}
}
