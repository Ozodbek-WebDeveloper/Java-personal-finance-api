package com.example.personal_finance_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.personal_finance_api.dto.user.LoginReq;
import com.example.personal_finance_api.dto.user.LoginRes;
import com.example.personal_finance_api.dto.user.RefreshReq;
import com.example.personal_finance_api.dto.user.RefreshRes;
import com.example.personal_finance_api.dto.user.UserReq;
import com.example.personal_finance_api.dto.user.UserRes;
import com.example.personal_finance_api.entity.User;
import com.example.personal_finance_api.mapper.UserMapper;
import com.example.personal_finance_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@PostMapping("/register")
	public UserRes createUser(@Valid @RequestBody UserReq userReq) {
		return userService.create(userReq);
	}

	@PostMapping("/login")
	public LoginRes login(@Valid @RequestBody LoginReq loginReq) {
		return userService.login(loginReq);
	}

	@GetMapping("/me")
	public UserRes me(@AuthenticationPrincipal User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Foydalanuvchi tizimga kirmagan");
		}
		return userMapper.toResponse(user);
	}

	@PostMapping("/refresh")
	public RefreshRes refresh(@Valid @RequestBody RefreshReq request) {
		return userService.refresh(request.refreshToken());
	}

	@PostMapping("/logout")
	public  String logout(@Valid @RequestBody RefreshReq request){
		return  userService.logout(request.refreshToken());
	}
}
