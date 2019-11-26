package com.subra.springjwtjavainuse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.subra.springjwtjavainuse.config.JwtTokenUtil;
import com.subra.springjwtjavainuse.model.LoginCredential;
import com.subra.springjwtjavainuse.service.BridgeBetweenService;

@RestController
public class HelloWorldController {

	private Logger log = LoggerFactory.getLogger(HelloWorldController.class);
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDetailsService bridgeBetweenService;
	
	@RequestMapping({ "/hello" })
	public String firstPage() {
		log.info("in /hello url");
		return "Hello World";
	}
	
	@RequestMapping({ "/index" })
	public String indexUrl() {
		log.info("in /index url");
		return "index endpoint ";
	}
	
	 @RequestMapping("/login")  
	    public ModelAndView login() {  
		 	ModelAndView mv = new ModelAndView("login");
		 	mv.addObject("test", "found");
	        return mv;  
	    } 
	 
	 @RequestMapping("/home")  
	    public ModelAndView home() {  
		 log.info("You are in home page");
		 	ModelAndView mv = new ModelAndView("home");
		 	mv.addObject("test", "found");
	        return mv;  
	    } 
	 
	 
	 @RequestMapping("/error-login")  
	    public ModelAndView errrlogin() {  
		 	ModelAndView mv = new ModelAndView("login");
		 	log.info("in erro-login...");
		 	mv.addObject("test", "found");
		 	mv.addObject("loginError", true);
	        return mv;  
	    } 	 
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> loginORauthenticate(@RequestBody LoginCredential udata) throws Exception{
	
		log.info("in /authenticate url" + udata.toString());
		
	Authentication authentication =	authenticate(udata);
	log.info("---does it work? " + authentication.getPrincipal().toString());
	//at this point authenitcation successful
	UserDetails userDetails = bridgeBetweenService.loadUserByUsername(udata.getUid());
	String jWTtoken = jwtTokenUtil.generateToken(userDetails);
	 //ResponseEntity<String> resp = new ResponseEntity<String>("ThisIsTest111",HttpStatus.OK);
	ResponseEntity<String> resp = new ResponseEntity<String>(jWTtoken,HttpStatus.OK);
		return resp;
	}
	
	
	//later use Authentication AuthenticationProvider.authenticate(Authentication);
	private Authentication authenticate(LoginCredential u ) throws Exception{
		
		Authentication authenticationAfter = null;
		UsernamePasswordAuthenticationToken unamePasswdAuthTokenbefore = 
				new UsernamePasswordAuthenticationToken(u.getUid(), u.getPassword());
		try{
		 authenticationAfter = authenticationManager.authenticate(unamePasswdAuthTokenbefore);
		 log.info("before:" + unamePasswdAuthTokenbefore + " after:" + authenticationAfter);
		}catch(Exception e){
			throw new Exception("Unsccessful Authentication", e);
		}
		return authenticationAfter;
	}

}
