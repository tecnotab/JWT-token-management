package com.tecnotab.jwt.services;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.login.AppConfigurationEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tecnotab.jwt.model.JwtToken;
import com.tecnotab.jwt.model.TokenRequestBody;
import com.tecnotab.jwt.util.ApplicationConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenService {
	
	@Autowired
	private ApplicationConfig appConfig;

	public JwtToken generateToken(TokenRequestBody body, HttpHeaders headers) {
		
		String jwtToken = Jwts.builder()
		        .claim("accountNumber", body.getAccountNumber())
		        .claim("accountType", body.getAccountType())
		        .claim("userType", body.getUserType())
		        .setId(UUID.randomUUID().toString())
		        .setIssuedAt(Date.from(Instant.now()))
		        .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
		        .compact();
		
		JwtToken jwtTokenObject = new JwtToken();
		jwtTokenObject.setJwtToken(jwtToken);
		jwtTokenObject.setAccountType(headers.getFirst("accountType"));
		return jwtTokenObject;
		
	}
	
		
	
public JwtToken generateTokenUsingSecret(TokenRequestBody body, HttpHeaders headers) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		
	
		String jwtToken = Jwts.builder()
		        .claim("account Number", body.getAccountNumber())
		        .claim("account Type", body.getAccountType())
		        .claim("user Type", body.getUserType())
		        .setId(UUID.randomUUID().toString())
		        .setIssuedAt(Date.from(Instant.now()))
		        .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
		        .signWith(SignatureAlgorithm.RS256, getPrivateKey())
		        .compact();
		
		JwtToken jwtTokenObject = new JwtToken();
		jwtTokenObject.setJwtToken(jwtToken);
		jwtTokenObject.setAccountType(headers.getFirst("accountType"));
		return jwtTokenObject;
		
	}



public Claims parseJwt(String jwtToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
	Claims jwt = Jwts.parser()
    		.setSigningKey(getPublicKey())
    		.parseClaimsJws(jwtToken)
    		.getBody();
    
     return jwt;
}

private  PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String rsaPrivateKey = appConfig.getPrivateKey();

    rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
    rsaPrivateKey = rsaPrivateKey.replace("-----END PRIVATE KEY-----", "");
    rsaPrivateKey = rsaPrivateKey.replace("\n", "");

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey privKey = kf.generatePrivate(keySpec);
    return privKey;
}	
private  PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String rsaPublicKey = appConfig.getPublicKey();
    rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
    rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
    rsaPublicKey = rsaPublicKey.replace("\n", "");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey publicKey = kf.generatePublic(keySpec);
    return publicKey;
}

}
