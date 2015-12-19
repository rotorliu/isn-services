package com.isn.services.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.isn.services.conf.CloopenSettings;
import com.isn.services.po.Friend;
import com.isn.services.po.Message;
import com.isn.services.po.MessageBox;
import com.isn.services.po.MessageComment;
import com.isn.services.po.User;
import com.isn.services.po.VerificationCode;
import com.isn.services.repo.FriendRepository;
import com.isn.services.repo.MessageBoxRepository;
import com.isn.services.repo.MessageCommentRepository;
import com.isn.services.repo.MessageRepository;
import com.isn.services.repo.UserRepository;
import com.isn.services.repo.VerificationCodeRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository repoUser;
	@Autowired
	private FriendRepository repoFriend;
	@Autowired
	private MessageRepository repoMessage;
	@Autowired
	private MessageBoxRepository repoMessageBox;
	@Autowired
	private VerificationCodeRepository repoVerificationCode;
	@Autowired
	private MessageCommentRepository repoMessageComment;
	@Autowired  
	CloopenSettings cloopenSettings;  
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{userid}")
    public void delete(@PathVariable long userid) {
		repoUser.delete(userid);
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/{userid}",produces="application/json")
    public User get(@PathVariable long userid) {
		return repoUser.findOne(userid);
    }
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST,path="/register" ,produces="application/json")
    public long register(@RequestParam(value="mobile") String mobile, 
    		@RequestParam(value="name") String name, 
    		@RequestParam(value="password") String password, 
    		@RequestParam(value="verificationCode") String verificationCode) {
		if(repoUser.findByMobile(mobile) != null){
			return -1;//user exist
		}
		else{
			if(check(mobile, verificationCode)){
				User user = new User();
				user.setMobile(mobile);
				user.setPassword(password);
				user.setName(name);
				user = repoUser.save(user);
				return user.getId();
			}
			else{
				return -2;//verification code error;
			}
		}
    }
	
	private boolean check(String mobile ,String verificationCode){
		VerificationCode vcode = repoVerificationCode.findByMobile(mobile);
		if(vcode != null && vcode.getCode().equals(verificationCode)){
			repoVerificationCode.deleteByMobile(mobile);
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getUserByMobile",produces="application/json")
    public User getUserByMobile(@RequestParam(value="mobile") String mobile) {
    	return repoUser.findByMobile(mobile);
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/login",produces="application/json")
    public long login(@RequestParam(value="mobile") String mobile,
    		@RequestParam(value="password") String password) {
    	User user = getUserByMobile(mobile);
    	if(user == null){
    		return -1;//user does not exist.
    	}
    	else{
    		String user_password = user.getPassword();
	    	if(user_password != null && password != null &&
	    			!user_password.trim().equals(password.trim())){
	    		return -2;//password error.
	    	}
	    	else{
	    		return user.getId();
	    	}
    	}
    }
	
	@RequestMapping(method=RequestMethod.PUT,path="",consumes="application/json")
    public void update(@RequestBody User user) {
		User old = repoUser.findOne(user.getId());
		if(old != null){
			old.setBirthday(user.getBirthday());
			old.setEmail(user.getEmail());
			old.setGender(user.getGender());
			old.setMobile(user.getMobile());
			old.setName(user.getName());
			old.setPassword(user.getPassword());
			repoUser.save(old);
		}
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/requestVerificationCode",produces="application/json")
    public void requestVerificationCode(@RequestParam(value="mobile") String mobile) {
    	String code = generateVerificationCode();
    	if(sendToMobile(mobile,code)){
    		registerVerificationCode(mobile,code);
    	}
    }
	
	private String generateVerificationCode(){
		Random r = new Random();
		int code = r.nextInt(899999);
		return (code + 100000) + "";
	}
	
	private boolean sendToMobile(String mobile, String code){
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(cloopenSettings.getRestUrl(), cloopenSettings.getRestPort());
		restAPI.setAccount(cloopenSettings.getAccountSid(), cloopenSettings.getAuthToken());
		restAPI.setAppId(cloopenSettings.getAppId());
		switch(cloopenSettings.getSendType()){
			case SMS:
				result = restAPI.sendTemplateSMS(mobile, cloopenSettings.getSMSTemplateId() ,new String[]{code});
				break;
			case Voice:
				result = restAPI.voiceVerify(code, mobile,"",cloopenSettings.getVoicePlayTimes(),"", cloopenSettings.getLang(), "");
				break;
		}

		return "000000".equals(result.get("statusCode"));

	}
	
	private void registerVerificationCode(String mobile, String code){
		VerificationCode vcode = new VerificationCode();
		vcode.setMobile(mobile);
		vcode.setCode(code);
		repoVerificationCode.save(vcode);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/friends", produces="application/json")
    public Page<Friend> getMyFriends(@PathVariable long userId, Pageable pageRequest){
//		User user = repoUser.findOne(userId);
//		return user.getFriends();
		return repoFriend.findMyFriendsOrderByAliasAsc(userId, pageRequest);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/friends/{friendId}", produces="application/json")
	public Friend getMyFriend(@PathVariable long userId, @PathVariable long friendId){
		User user = repoUser.findOne(userId);
		if(user != null){
			List<Friend> friends = user.getFriends();
			if(friends != null){
				for(int i = 0; i < friends.size(); i++){
					Friend friend = friends.get(i);
					if(friend != null && friend.getId() == friendId){
						return friend;
					}
				}
			}
		}
		
		return null;
	}

	@RequestMapping(method=RequestMethod.POST,path="/{userId}/friends",consumes="application/json")
    public long createMyFriend(@PathVariable long userId, @RequestBody Friend friend) {
		User user = repoUser.findOne(userId);
		if(user != null){
			if(friend != null){
				friend.setOwner(user);
				repoFriend.save(friend);
			}
			return friend.getId();
		}
		
		return 0;
    }
	
	@RequestMapping(method=RequestMethod.PUT,path="/{userId}/friends",consumes="application/json")
    public void updateMyFriend(@PathVariable long userId, @RequestBody Friend friend) {
		User user = repoUser.findOne(userId);
		if(user != null){
			List<Friend> friends = user.getFriends();
			if(friends != null){
				for(int i = 0; i < friends.size(); i++){
					Friend f = friends.get(i);
					if(f != null && f.getId() == friend.getId()){
						f.setAlias(friend.getAlias());
						f.setDescription(friend.getDescription());
						f.setEmail(friend.getEmail());
						f.setMobile(friend.getMobile());
						f.setTag(friend.getTag());
						repoFriend.save(f);
					}
				}
			}
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{userId}/friends/{friendId}")
    public void deleteMyFriend(@PathVariable long userId, @PathVariable long friendId){
		User user = repoUser.findOne(userId);
		if(user != null){
			List<Friend> friends = user.getFriends();
			if(friends != null){
				for(int i = 0; i < friends.size(); i++){
					Friend friend = friends.get(i);
					if(friend != null && friend.getId() == friendId){
						friends.remove(i);
						repoFriend.delete(friend);
						break;
					}
				}
			}
		}
		
	}
	
	/*
	 * Get all messages that the user receive, even though the message was removed from message inbox
	 */
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/inmessages", produces="application/json")
    public Page<Message> getMyReceivedMessages(@PathVariable long userId, Pageable pageRequest){
		return repoMessage.findMyReceivedMessages(userId, pageRequest);
	}
	
	/*
	 * Get all messages that the user send, even though the message was removed from message outbox
	 */
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/outmessages", produces="application/json")
    public Page<Message> getMySendMessages(@PathVariable long userId, Pageable pageRequest){
//		User user = repoUser.findOne(userId);
//		if(user != null){
//			return user.getOutmessages();
//		}
//		return null;
		return repoMessage.findMySendMessages(userId, pageRequest);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/inbox", produces="application/json")
    public Page<Message> getMyInBoxMessages(@PathVariable long userId, Pageable pageRequest){
//		User user = repoUser.findOne(userId);
//		if(user != null){
//			MessageBox mInBox = user.getInBox();
//			if(mInBox != null){
//				return mInBox.getMessages();
//			}
//		}
//		return null;
		return repoMessage.findMyInBoxMessages(userId, pageRequest);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/outbox", produces="application/json")
    public Page<Message> getMyOutBoxMessages(@PathVariable long userId, Pageable pageRequest){
//		User user = repoUser.findOne(userId);
//		if(user != null){
//			MessageBox mOutBox = user.getOutBox();
//			if(mOutBox != null){
//				return mOutBox.getMessages();
//			}
//		}
//		return null;
		return repoMessage.findMyOutBoxMessages(userId, pageRequest);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{userId}/inbox/{messageId}")
	public void removeMessageFromMyInBox(@PathVariable long userId, @PathVariable long messageId){
		User user = repoUser.findOne(userId);
		if(user != null){
			MessageBox mInBox = user.getInBox();
			if(mInBox != null){
				Message message = repoMessage.findOne(messageId);
				mInBox.getMessages().remove(message);
				repoMessageBox.save(mInBox);
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{userId}/outbox/{messageId}")
	public void removeMessageFromMyOutBox(@PathVariable long userId, @PathVariable long messageId){
		User user = repoUser.findOne(userId);
		if(user != null){
			MessageBox mOutBox = user.getOutBox();
			if(mOutBox != null){
				Message message = repoMessage.findOne(messageId);
				mOutBox.getMessages().remove(message);
				repoMessageBox.save(mOutBox);
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/comments", produces="application/json")
    public Page<MessageComment> getMyComments(@PathVariable long userId, Pageable pageRequest){
//		User user = repoUser.findOne(userId);
//		return user.getComments();
		return repoMessageComment.findUserCommentsOrderByTimeDesc(userId, pageRequest);
	}
}