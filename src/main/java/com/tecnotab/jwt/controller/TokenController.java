package com.tecnotab.jwt.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tecnotab.jwt.model.JwtToken;
import com.tecnotab.jwt.model.TokenRequestBody;
import com.tecnotab.jwt.services.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
public class TokenController {
	
	@Autowired
	private TokenService tokenService;

	@PostMapping("/generate/jwt/token")
	public ResponseEntity<JwtToken> generateJwtToken(
			@RequestBody TokenRequestBody body,
			@RequestHeader(value ="apiversion" , required=true) String apiVersion,
			@RequestHeader(value ="accountType" , required=true) String accountType,
			@RequestHeader(value ="messageId" , required=true) String messageIds) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("apiversion", apiVersion);
		headers.add("accountType", accountType);
		headers.add("messageId", messageIds);
		return new ResponseEntity<>(tokenService.generateTokenUsingSecret(body,headers),HttpStatus.OK);
		
	}

	@PostMapping("/validate/jwt/token")
	public Claims jwtTokenValidate(
			@RequestHeader(value ="token" , required=true) String jwtToken,
			@RequestHeader(value ="apiversion" , required=true) String apiVersion,
			@RequestHeader(value ="accountType" , required=true) String accountType,
			@RequestHeader(value ="messageId" , required=true) String messageIds) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
				
		return tokenService.parseJwt(jwtToken);
		
	}
}
