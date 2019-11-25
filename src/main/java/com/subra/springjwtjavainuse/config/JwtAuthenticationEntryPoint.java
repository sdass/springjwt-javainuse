package com.subra.springjwtjavainuse.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	//this class commence method sends 401 to every unauthorized request.
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse resp,
			AuthenticationException authenticationException) throws IOException, ServletException {
		
		log.error("failed authentication set to 401 response-code");
		// send 401
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized-user"); //sadded to header
		
	}

}
