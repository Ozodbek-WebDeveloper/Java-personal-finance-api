package com.example.personal_finance_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.personal_finance_api.enums.TransactionType;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotBlank
	@Column(nullable = false, length = 100)
	private String title;

	@NotNull
	@DecimalMin(value = "0.1")
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@NotNull
	@Column(nullable = false)
	private LocalDate transactionDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	protected Transaction() {
	}

	public Transaction(String title, BigDecimal amount, LocalDate transactionDate, TransactionType type,
			Category category, User user) {
		this.title = title;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.category = category;
		this.type = type;
		this.user = user;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = null;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	// get
	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public TransactionType getType() {
		return type;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Category getCategory() {
		return category;
	}

	public User getUser() {
		return user;
	}

	// set
	public void setCategory(Category category) {
		this.category = category;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

}
