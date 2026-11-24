package com.example.personal_finance_api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RefreshReq(
		@NotBlank(message = "refreshToken majburiy!") String refreshToken) {
}
