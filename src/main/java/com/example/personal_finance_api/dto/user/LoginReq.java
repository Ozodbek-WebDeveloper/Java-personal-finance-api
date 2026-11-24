package com.example.personal_finance_api.dto.user;

import jakarta.validation.constraints.NotNull;

public record LoginReq(
		@NotNull(message = "Phone majburiy!") String phone,
		@NotNull(message = "Password majburiy!") String password) {

}
