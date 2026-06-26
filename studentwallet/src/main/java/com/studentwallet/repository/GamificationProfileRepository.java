package com.studentwallet.repository;

import com.studentwallet.model.GamificationProfile;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamificationProfileRepository extends CassandraRepository<GamificationProfile, String> {

    @Query("SELECT * FROM gamification_profiles WHERE hostel_name = ?0 ALLOW FILTERING")
    List<GamificationProfile> findByHostelName(String hostelName);
}
