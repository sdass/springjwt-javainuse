package com.subra.springjwtjavainuse.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class BridgeUserDetails implements UserDetails {

	User c;
	
	public BridgeUserDetails(User c) {	this.c = c;  }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		for(String r : c.getRoles()){
			roles.add(new SimpleGrantedAuthority(r));
		}		
		return roles;
	}

	@Override public String getPassword() {	return c.getPassword(); }
	@Override public String getUsername() { return c.getUid(); 	}

	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() {	return true; }

}
