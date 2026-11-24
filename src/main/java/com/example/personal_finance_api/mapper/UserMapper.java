package com.example.personal_finance_api.mapper;

import org.springframework.stereotype.Component;

import com.example.personal_finance_api.dto.user.UserRes;
import com.example.personal_finance_api.entity.User;

@Component 
public class UserMapper {
	public UserRes toResponse(User user) {
		return new UserRes(
				user.getId(),
				user.getFirstName(),
				user.getPhone(),
				user.getRole(),
				user.getStatus(),
				user.getCreatedAt(),
				user.getUpdatedAt());
	}
}
