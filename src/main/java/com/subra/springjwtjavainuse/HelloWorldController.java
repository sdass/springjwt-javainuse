package com.subra.springjwtjavainuse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subra.springjwtjavainuse.config.JwtTokenUtil;
import com.subra.springjwtjavainuse.model.LoginCredential;

@RestController
public class HelloWorldController {

	private Logger log = LoggerFactory.getLogger(HelloWorldController.class);
	@Autowired
	AuthenticationManager authenticationManager;
	
	@RequestMapping({ "/hello" })
	public String firstPage() {
		log.info("in /hello url");
		return "Hello World";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> loginORauthenticate(@RequestBody LoginCredential udata) throws Exception{
	
		log.info("in /authenticate url" + udata.toString());
		
	Authentication authentication =	authenticate(udata);
	log.info("---does it work? " + authentication.getPrincipal().toString());
	 ResponseEntity<String> resp = new ResponseEntity<String>("ThisIsTest111",HttpStatus.OK);
		return resp;
	}
	
	
	//later use Authentication AuthenticationProvider.authenticate(Authentication);
	private Authentication authenticate(LoginCredential u ) throws Exception{
		
		Authentication authenticationAfter = null;
		UsernamePasswordAuthenticationToken unamePasswdAuthTokenbefore = 
				new UsernamePasswordAuthenticationToken(u.getUid(), u.getPassword());
		try{
		 authenticationAfter = authenticationManager.authenticate(unamePasswdAuthTokenbefore);
		}catch(Exception e){
			throw new Exception("Unsccessful Authentication", e);
		}
		return authenticationAfter;
	}

}
