package com.studentwallet.repository;

import com.studentwallet.model.Expense;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CassandraRepository<Expense, UUID> {

    @Query("SELECT * FROM expenses WHERE user_id = ?0 ALLOW FILTERING")
    List<Expense> findByUserId(String userId);

    @Query("SELECT * FROM expenses WHERE user_id = ?0 AND expense_date = ?1 ALLOW FILTERING")
    List<Expense> findByUserIdAndExpenseDate(String userId, LocalDate expenseDate);
}
