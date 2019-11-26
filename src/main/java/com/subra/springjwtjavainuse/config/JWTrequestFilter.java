package com.subra.springjwtjavainuse.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


//@Primary
@Component("JwtrequestFilter")
public class JWTrequestFilter extends OncePerRequestFilter {
		
	@Autowired private UserDetailsService bridgeBetweenService;
	
	@Autowired JwtTokenUtil jwtTokenutil;
	private Logger log = LoggerFactory.getLogger(JWTrequestFilter.class);	

	@Override
	protected void doFilterInternal(HttpServletRequest request,	HttpServletResponse response, FilterChain filterChain)	
			throws ServletException, IOException {		
		
		String username = null;
		// 1.Once we get the token validate it.
		String reqAuthTokeninHeader = request.getHeader("Authorization");
		if (reqAuthTokeninHeader != null && reqAuthTokeninHeader.startsWith("Bearer ")) {
			try {
				username = jwtTokenutil.getUsernameFromToken(reqAuthTokeninHeader);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			log.warn("JWT Token is null or does not start with Bearer ");
		}

		//2. token-parsed valid user above && springNOTyetAuthenticated <--on condition do authentication
		if((username != null) && SecurityContextHolder.getContext().getAuthentication() == null){
			UserDetails userdetails = bridgeBetweenService.loadUserByUsername(username); //spring-security in play
			// if token is valid configure Spring Security to do and set  authentication
			if(jwtTokenutil.validateToken(reqAuthTokeninHeader, userdetails)){ 
				//1. passed: token-username found in database
				//now authenticate using password and after that get Roles
				//this is 2nd round. So user already is veryfied by posting on /login url. token valid. 
				//So user spring security manually populating 
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities()); //user came with get 2nd time. no passsword
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //???!!??
				
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); //???!!!!???
						
			}
			
		}
		
		filterChain.doFilter(request, response);
	}

}
