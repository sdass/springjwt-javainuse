package com.subra.springjwtjavainuse.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class User  {

	String uid;
	String password;
	List<String> roles = new ArrayList<>(); //
	
}