package com.studentwallet.repository;

import com.studentwallet.model.DailyAggregate;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DailyAggregateRepository extends CassandraRepository<DailyAggregate, UUID> {

    @Query("SELECT * FROM daily_aggregates WHERE user_id = ?0 AND cycle_id = ?1 ALLOW FILTERING")
    List<DailyAggregate> findByUserIdAndCycleId(String userId, UUID cycleId);
}
