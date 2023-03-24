package com.tecnotab.jwt.model;

import lombok.Data;

@Data
public class JwtToken {
	
	private String accountType;
	private String jwtToken;
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	
	

}
