package com.uedsonreis.ecommerce.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.uedsonreis.ecommerce.utils.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenManager {

	public static final long TOKEN_VALIDITY = 5 * 60 * 60; // 5h

	@Value("${token.secret}")
	private String secret;

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		JwtParser parser = Jwts.parser().setSigningKey(secret);
		final Claims claims = parser.parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getClaimFromToken(token, Claims::getExpiration);
		return expiration.before(new Date());
	}

	public String getLoggedUsername(String authorizationHeader) throws Exception {

		if (authorizationHeader != null && !authorizationHeader.startsWith("Baerer ")) {
			String token = authorizationHeader.substring(7);
			
			if (this.isTokenExpired(token)) {
				throw new Exception(Util.getTokenExpired());
			}
			
			try {
				return this.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(Util.getUnableToGetToken());
			}
		} else {
			throw new Exception(Util.getUnableToGetToken());
		}
	}

}