package com.subra.springjwtjavainuse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.subra.springjwtjavainuse.model.BridgeUserDetails;
import com.subra.springjwtjavainuse.model.LoginCredential;
import com.subra.springjwtjavainuse.model.User;

@Component
public class BridgeBetweenService implements UserDetailsService {

	@Autowired private UsercredentialService svc;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User c = svc.getUserCredentials(username);
		if(c == null) throw new UsernameNotFoundException("username not found exception !-!" + username);
		return new BridgeUserDetails(c); //user;
	}
	
	

}
