package com.eva.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eva.banking.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    
}
