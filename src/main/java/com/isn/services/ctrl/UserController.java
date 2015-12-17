package com.isn.services.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isn.services.po.Friend;
import com.isn.services.po.Message;
import com.isn.services.po.MessageComment;
import com.isn.services.po.User;
import com.isn.services.repo.FriendRepository;
import com.isn.services.repo.MessageRepository;
import com.isn.services.repo.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final static Map<String, String> VERIFICATION_CODES = new HashMap<String, String>();

	@Autowired
	private UserRepository repoUser;
	@Autowired
	private FriendRepository repoFriend;
	@Autowired
	private MessageRepository repoMessage;
	
	@RequestMapping(method=RequestMethod.DELETE,path="/{userid}")
    public void delete(@PathVariable long userid) {
		repoUser.delete(userid);
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/{userid}",produces="application/json")
    public User get(@PathVariable long userid) {
		return repoUser.findOne(userid);
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/register" ,produces="application/json")
    public long register(@RequestParam(value="mobile") String mobile, 
    		@RequestParam(value="name") String name, 
    		@RequestParam(value="password") String password, 
    		@RequestParam(value="verificationCode") String verificationCode) {
		if(repoUser.findByMobile(mobile).size() > 0){
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
		return true;
//		String code = VERIFICATIONCODES.get(mobile);
//		if(code != null && code.equals(verificationCode)){
//			VERIFICATIONCODES.remove(mobile);
//			return true;
//		}
//		return false;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/getUserByMobile",produces="application/json")
    public User getUserByMobile(@RequestParam(value="mobile") String mobile) {
    	List<User> users = repoUser.findByMobile(mobile);
    	if(users.size() > 0){
    		return users.get(0);
    	}
    	return null;
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
		return true;
	}
	
	private void registerVerificationCode(String mobile, String code){
		VERIFICATION_CODES.put(mobile, code);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/friends", produces="application/json")
    public List<Friend> getMyFriends(@PathVariable long userId){
		User user = repoUser.findOne(userId);
		return user.getFriends();
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
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/inmessages", produces="application/json")
    public List<Message> getReceivedMessages(@PathVariable long userId){
		return repoMessage.findReceivedMessages(userId);
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/outmessages", produces="application/json")
    public List<Message> getSendMessages(@PathVariable long userId){
		User user = repoUser.findOne(userId);
		if(user != null){
			return user.getOutmessages();
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/{userId}/comments", produces="application/json")
    public List<MessageComment> getMyComments(@PathVariable long userId){
		User user = repoUser.findOne(userId);
		return user.getComments();
	}
}