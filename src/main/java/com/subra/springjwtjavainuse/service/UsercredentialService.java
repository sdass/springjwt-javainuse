package com.subra.springjwtjavainuse.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.subra.springjwtjavainuse.model.User;



@Service
public class UsercredentialService {

 static Map<String, User> allUsers = new HashMap<String, User>();

 Logger log = LoggerFactory.getLogger(UsercredentialService.class);
	
 public UsercredentialService() { //pretend-database
	System.out.println("---------should once 1 only---------");
	/*
	allUsers.put("sdass", new User("sdass", "123", Arrays.asList("USER", "ADMIN") ) );
	allUsers.put("other", new User("other", "222", Arrays.asList("USER")));
	allUsers.put("guest", new User("guest", "new", new ArrayList<String>()));
	*/
	//do bycrypt
	allUsers.put("sdass", new User("sdass", "$2a$10$DV5vMtB3o4g5fwXl.qtiWesRCiaEtQN.mgYh1H1JSjlM/Fl2hvoxG", Arrays.asList("USER", "ADMIN")));//123
	allUsers.put("other", new User("other", "$2a$10$ZeJOx7BZQUXZWKWvmKULj.2ZJCCbL97DjQ5vY2ei8IrvSpagwWOq2", Arrays.asList("USER")));//222
	allUsers.put("guest", new User("guest", "$2a$10$4NjpezpyAWcPV9cwioFU1utvcyQM4P0OUJlV2hn/MkvgKMUOp5tv2", new ArrayList<String>()));//new	
	//allUsers.put("try", new User("try", "plain", new ArrayList<String>()));//new	
 }
	public User getUserCredentials(String user){
		
		User c = allUsers.get(user);
		String customerStr = c==null? "null" : c.toString();
		log.info("UsercredentialService---" + customerStr);
		return c;
		
		
	}
	
	
}



