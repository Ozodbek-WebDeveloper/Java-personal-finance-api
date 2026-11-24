package com.example.personal_finance_api.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.personal_finance_api.dto.user.LoginReq;
import com.example.personal_finance_api.dto.user.LoginRes;
import com.example.personal_finance_api.dto.user.RefreshRes;
import com.example.personal_finance_api.dto.user.UserReq;
import com.example.personal_finance_api.dto.user.UserRes;
import com.example.personal_finance_api.entity.RefreshToken;
import com.example.personal_finance_api.entity.User;
import com.example.personal_finance_api.enums.RoleType;
import com.example.personal_finance_api.enums.UserStatus;
import com.example.personal_finance_api.exception.ResourceNotFoundException;
import com.example.personal_finance_api.mapper.UserMapper;
import com.example.personal_finance_api.repository.RefreshTokenRepository;
import com.example.personal_finance_api.repository.UserRepository;
import com.example.personal_finance_api.security.JwtService;
import com.example.personal_finance_api.security.RefreshTokenService;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final RefreshTokenRepository refreshTokenRepository;

	public UserService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			UserMapper userMapper,
			JwtService jwtService,
			RefreshTokenService refreshTokenService,
			RefreshTokenRepository refreshTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public UserRes create(UserReq userReq) {

		if (userRepository.existsByPhone(userReq.phone())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Bu telfon raqami allaqachon ro'yxatdan o'tgan");
		}

		String hashedPassword = passwordEncoder.encode(userReq.password());

		return userMapper.toResponse(userRepository
				.save(
						new User(
								userReq.firstName(),
								userReq.phone(),
								hashedPassword,
								RoleType.USER,
								UserStatus.ACTIVE)));
	}

	public LoginRes login(LoginReq loginReq) {
		User user = userRepository.findByPhone(loginReq.phone())
				.orElseThrow(() -> new ResourceNotFoundException("user topilmadi!"));

		if (!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login yoki parol x	xato!");
		}

		String accessToken = jwtService.generateToken(user);
		String refreshToken = refreshTokenService.createRefreshToken(user);

		return new LoginRes(userMapper.toResponse(user), accessToken, refreshToken);
	}

	@Transactional
	public RefreshRes refresh(String token) {
		String normalizedToken = token == null ? "" : token.trim();
		if (normalizedToken.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh-token topilmadi!");
		}

		String hashToken = refreshTokenService.hashToken(normalizedToken);
		RefreshToken findToken = refreshTokenRepository.findByTokenHash(hashToken)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh-token topilmadi!"));

		if (findToken.getRevoked() || findToken.getExpiryDate().isBefore(LocalDateTime.now())
				|| findToken.getUser().getStatus() == UserStatus.DELETE) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token  muddati o'tgan yoki yaroqsiz!");
		}

		User user = findToken.getUser();

		String accessToken = jwtService.generateToken(user);
		String refreshToken = refreshTokenService.createRefreshToken(user);

		findToken.setRevoked(true);
		refreshTokenRepository.save(findToken);

		return new RefreshRes(accessToken, refreshToken);
	}

	public String logout(String token) {
		String normalizedToken = token == null ? "" : token.trim();
		String hashToken = refreshTokenService.hashToken(normalizedToken);
		RefreshToken findToken = refreshTokenRepository.findByTokenHash(hashToken)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh-token topilmadi!"));

		if (findToken.getRevoked() || findToken.getExpiryDate().isBefore(LocalDateTime.now())
				|| findToken.getUser().getStatus() == UserStatus.DELETE) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token  muddati o'tgan yoki yaroqsiz!");
		}

		findToken.setRevoked(true);
		refreshTokenRepository.save(findToken);

		return "Tizimdan chiqdingiz!";
	}
}
