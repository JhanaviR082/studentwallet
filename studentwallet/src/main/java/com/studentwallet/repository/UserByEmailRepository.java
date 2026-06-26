package com.studentwallet.repository;

import com.studentwallet.model.UserByEmail;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserByEmailRepository extends CassandraRepository<UserByEmail, String> {
}
