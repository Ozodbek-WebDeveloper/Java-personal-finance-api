package com.example.personal_finance_api.dto.user;

public record LoginRes(
		UserRes user,
		String accessToken,
		String refreshToken) {

}
