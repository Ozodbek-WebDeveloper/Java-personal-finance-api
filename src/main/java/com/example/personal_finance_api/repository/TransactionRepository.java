package com.example.personal_finance_api.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.personal_finance_api.dto.DashboardExpensesRes;
import com.example.personal_finance_api.dto.TransactionRes;
import com.example.personal_finance_api.entity.Transaction;
import com.example.personal_finance_api.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	boolean existsByCategoryId(Integer categoryId);
	Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);
	List<Transaction> findAllByCategoryIdAndUserId(Integer categoryId,Integer userId);
	List<Transaction> findAllByUserId(Integer userId);
	@Query("""
			    SELECT COALESCE(SUM(t.amount), 0)
			    FROM Transaction t
			    WHERE t.type = :type
			    AND t.transactionDate BETWEEN :from AND :to
			""")
	BigDecimal sumByTypeAndDateBetween(@Param("type") TransactionType type, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

	@Query("""
			    SELECT MIN(t.transactionDate)
			    from Transaction t
			""")
	Optional<LocalDate> findOldestTransactionDate();

	@Query("""
			SELECT
			    c.id AS categoryId,
			    c.name AS categoryName,
			    SUM(t.amount) AS totalAmount
			FROM Transaction t
			JOIN t.category c
			WHERE t.type = :type
			AND t.transactionDate BETWEEN :from AND :to
			GROUP BY c.id, c.name
			ORDER BY SUM(t.amount) DESC
			""")
	List<DashboardExpensesRes> findCategoryExpenses(
			@Param("type") TransactionType type,
			@Param("from") LocalDate from,
			@Param("to") LocalDate to);

	List<TransactionRes> findAllByTransactionDateBetween(LocalDate from, LocalDate to);

	List<TransactionRes> findTop10ByOrderByTransactionDateDesc();
}
