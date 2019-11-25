package com.subra.springjwtjavainuse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.subra.springjwtjavainuse.model.LoginCredential;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService bridgeBetweenService;
	
	@Autowired
	AuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Override //2nd way works-2
	protected void configure(AuthenticationManagerBuilder auth)	throws Exception { //AuthenticationManager bean is prepared internally
		auth.userDetailsService(bridgeBetweenService).passwordEncoder(passwordEncoder());
		//auth.userDetailsService(bridgeBetweenService).passwordEncoder(NoOpPasswordEncoder.getInstance()); plain works
	}

	
	/*works -1
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable CSRF
		http.csrf().disable()
		// dont authenticate /authenticate endpoint
		.authorizeRequests().antMatchers("/authenticate").permitAll()
		.and().authorizeRequests().antMatchers("/index").permitAll()
		.and().authorizeRequests().antMatchers("/login").permitAll()
		//all other request authenticate
		.anyRequest().authenticated().and()
		.formLogin().and()
		.httpBasic();
	}	
	*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable CSRF
		http.csrf().disable()
		// dont authenticate /authenticate endpoint
		.authorizeRequests().antMatchers("/authenticate").permitAll()
		.and().authorizeRequests().antMatchers("/index").permitAll()
		//.and().authorizeRequests().antMatchers("/login").permitAll()
		//all other request authenticate
		.anyRequest().authenticated().and()
		.formLogin().loginPage("/login").permitAll()
		.failureUrl("/error-login").permitAll()
		.defaultSuccessUrl("/home");
		//.and()
		//.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedPage("/login").and() cannot coexist with .failureUrl("/error-login")
		//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	

	}
	
	/*works-1
	@Bean
	public AuthenticationProvider authProvider(){ //has a method called authenticate()
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		daoAuthProvider.setUserDetailsService(bridgeBetweenService);
		//daoAuthProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return daoAuthProvider;
	}
	*/
	
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

/*
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
*/

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
