package com.subra.springjwtjavainuse.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
	public static final long JWT_TOKEN_VALIDITY = 3600 * 1000; //milliseconds = 1-hr
	private Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	@Value("${jwt.secret}")
	private String secret;
	
		//retrieve username from jwt token
		public String getUsernameFromToken(String token) throws Exception {		
			String username = getAllClaims(token).getSubject();
			return username;
		}
		//retrieve expiration date from jwt token
		public Date getExpirationDateFromToken(String token) throws Exception {
			Date expiredate = getAllClaims(token).getExpiration();
			return expiredate;
		}		
		private Claims getAllClaims(String token) throws Exception {
			Claims allclaims = null;
			try{
			allclaims = //Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
					Jwts.parser().setSigningKey(secret).parseClaimsJws(token.substring(7)).getBody(); //substring after "Bearer ". browser send
			}catch(Exception e){
				log.info("claim parsing problem...");
				throw new Exception(e);
			}
			return allclaims;
		}
		//-----------------------------------------------------------
		//generate a token for user per session	
		
		public String generateToken(UserDetails userDetails) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("roles", userDetails.getAuthorities());
			claims.put("locale", "en");
			//String newToken = 
			String token = Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)).signWith(SignatureAlgorithm.HS512, secret).compact();			
			return token;
		}

		//validate token. checks: 1. db-username == token-username 2. Must check against token tampering problem with Exception
		public Boolean validateToken(String token, UserDetails userDetails) { //2nd param reaching spring-security
			boolean allValid = false;
			try{
			boolean isParsed = parseToFind(token);	
			String userNameIntoken = getUsernameFromToken(token);
			String userNameInSpringSecCxt = userDetails.getUsername();
			boolean matchname = userNameInSpringSecCxt.equals(userNameIntoken);
			boolean localeMatch = getAllClaims(token).get("locale").equals("en");
			boolean isNotexpried = getExpirationDateFromToken(token).after(new Date());			
			allValid = matchname && isNotexpried && isParsed && localeMatch;
			}catch(Exception e){
				log.info("token is invalid...");
				allValid =  false;
			}
			
			return allValid;
		}
		
		private boolean parseToFind(String token) throws Exception{
			boolean status = true;
			try{
				//substring after "Bearer "
				Claims claims = getAllClaims(token);
			}catch(JwtException |ClassCastException e){
				log.error("token parsing failed");
			status = false;
			}catch(Exception e){
				status = false;
				throw new Exception(e);
			}
			return status;
		}		
}
