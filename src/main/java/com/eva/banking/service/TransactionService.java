package com.eva.banking.service;

import org.springframework.stereotype.Service;

import com.eva.banking.repository.TransactionRepository;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    

    
}
