package com.example.personal_finance_api.entity;


import com.example.personal_finance_api.entity.components.BaseEntity;
import com.example.personal_finance_api.enums.RoleType;
import com.example.personal_finance_api.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
	@NotNull(message = "Ism bo'sh bo'lmasligi kerak.")
	@Column(nullable = false)
	private String firstName;

	@NotNull(message = "Telfon raqam bo'sh bo'lmasligi kerak!")
	@Column(unique = true, nullable = false)
	private String phone;

	@NotNull(message = "Parol kiritish majburiy!")
	@Column(nullable = false)
	private String password;

	@NotNull(message = "Role kiritish majburiy!")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleType role;

	@NotNull(message = "Status kiritilishi majburiy!")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	protected User() {

	}

	public User(String firstName, String phone, String password, RoleType role, UserStatus status) {
		this.firstName = firstName;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.status = status;
	};

	public UserStatus getStatus() {
		return status;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPhone() {
		return phone;
	}

	public RoleType getRole() {
		return role;
	}

	public String getPassword() {
		return password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
