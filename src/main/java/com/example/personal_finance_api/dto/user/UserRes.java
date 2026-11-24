package com.example.personal_finance_api.dto.user;

import java.time.LocalDateTime;

import com.example.personal_finance_api.enums.RoleType;
import com.example.personal_finance_api.enums.UserStatus;

public record UserRes(
		Integer id,
		String firstName,
		String phone,
		RoleType role,
		UserStatus status,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
