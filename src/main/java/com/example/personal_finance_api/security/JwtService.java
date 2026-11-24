package com.example.personal_finance_api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.personal_finance_api.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final SecretKey key;
	private final long expiration;

	public JwtService(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration}") Long expiration) {
		this.key = Keys.hmacShaKeyFor(
				Decoders.BASE64.decode(secret));

		this.expiration = expiration;
	}

	public String generateToken(User user) {
		Date now = new Date();

		return Jwts.builder()
				.subject(user.getId().toString())
				.claim("role", user.getRole().name()).issuedAt(now)
				.expiration(new Date(now.getTime() + expiration))
				.signWith(key)
				.compact();
	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public Integer extractUserId(String token) {

		return Integer.valueOf(
				extractAllClaims(token).getSubject());
	}

	public String extractRole(String token) {

		return extractAllClaims(token).get("role", String.class);
	}

	// 5. Token haqiqiyligini tekshirish
	public boolean isTokenValid(String token, User user) {

		try {
			Claims claims = extractAllClaims(token);

			String tokenUserId = claims.getSubject();

			return tokenUserId.equals(user.getId().toString());

		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
