package com.example.personal_finance_api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "token_hash", nullable = false)
	private String tokenHash;

	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;

	private boolean revoked = false;

	private LocalDateTime createdAt;

	public Integer getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public User getUser() {
		return user;
	}

	public boolean getRevoked(){
		return  revoked;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
