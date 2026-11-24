package com.example.personal_finance_api.dto.user;


import jakarta.validation.constraints.NotNull;

public record UserReq(
		@NotNull(message = "Ism bo'sh bo'lmasligi kerak.") String firstName,

		@NotNull(message = "Telfon raqam bo'sh bo'lmasligi kerak!") String phone,

		@NotNull(message = "Parol kiritish majburiy!") String password) {

}
