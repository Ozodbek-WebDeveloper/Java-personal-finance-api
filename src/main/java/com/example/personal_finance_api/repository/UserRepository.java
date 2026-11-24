package com.example.personal_finance_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.personal_finance_api.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	boolean existsByPhone(String phone);

	Optional<User> findByPhone(String phone);
}
