package com.studentwallet.repository;

import com.studentwallet.model.BudgetCycle;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetCycleRepository extends CassandraRepository<BudgetCycle, UUID> {

    @Query("SELECT * FROM budget_cycles WHERE user_id = ?0 ALLOW FILTERING")
    List<BudgetCycle> findByUserId(String userId);
}
