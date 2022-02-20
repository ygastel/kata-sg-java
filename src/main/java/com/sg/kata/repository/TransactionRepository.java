package com.sg.kata.repository;


import com.sg.kata.entity.TransactionEntity;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

/**
 * Use spring data key-value to in-memory persist data.
 */
@Repository
public interface TransactionRepository extends KeyValueRepository<TransactionEntity, String> {
}
