package com.example.personal_finance_api.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

import com.example.personal_finance_api.entity.RefreshToken;
import com.example.personal_finance_api.entity.User;
import com.example.personal_finance_api.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	private final SecureRandom secureRandom = new SecureRandom();

	private static final int TOKEN_SIZE = 32;
	private static final int EXPIRATION_DAY = 7;

	@Transactional
	public String createRefreshToken(User user) {
		byte[] randomBytes = new byte[TOKEN_SIZE];
		secureRandom.nextBytes(randomBytes);

		String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(user);
		refreshToken.setTokenHash(hashToken(rawToken));
		refreshToken.setExpiryDate(LocalDateTime.now().plusDays(EXPIRATION_DAY));
		refreshToken.setRevoked(false);
		refreshToken.setCreatedAt(LocalDateTime.now());

		refreshTokenRepository.save(refreshToken);

		return rawToken;

	}



	public String hashToken(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hash = digest.digest(
					token.getBytes(StandardCharsets.UTF_8));

			return HexFormat.of().formatHex(hash);

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"SHA-256 algorithm not available", e);
		}
	}
}
