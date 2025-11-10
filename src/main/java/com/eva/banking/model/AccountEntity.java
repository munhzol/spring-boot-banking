package com.eva.banking.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNo;

    @Column(nullable = false)
    private Double balance = 0.0;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Enum<AccountType> accountType; // debit, credit, savings

    // Энэ дансаас илгээгдсэн бүх гүйлгээ
    @OneToMany(mappedBy = "fromAccount")
    private List<TransactionEntity> sentTransactions;

    // Энэ дансанд орж ирсэн бүх гүйлгээ
    @OneToMany(mappedBy = "toAccount")
    private List<TransactionEntity> receivedTransactions;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public AccountEntity() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Enum<AccountType> getAccountType() {
        return accountType;
    }

    public void setAccountType(Enum<AccountType> accountType) {
        this.accountType = accountType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<TransactionEntity> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<TransactionEntity> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<TransactionEntity> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<TransactionEntity> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
}
