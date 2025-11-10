package com.eva.banking.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private String description;

    // Гүйлгээг илгээгч данс
    @ManyToOne
    @JoinColumn(name = "from_account")
    private AccountEntity fromAccount;

    // Гүйлгээг хүлээн авагч данс
    @ManyToOne
    @JoinColumn(name = "to_account")
    private AccountEntity toAccount;

    public TransactionEntity() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AccountEntity getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(AccountEntity fromAccount) {
        this.fromAccount = fromAccount;
    }

    public AccountEntity getToAccount() {
        return toAccount;
    }

    public void setToAccount(AccountEntity toAccount) {
        this.toAccount = toAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
