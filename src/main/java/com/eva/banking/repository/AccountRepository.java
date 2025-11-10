package com.eva.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eva.banking.model.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllAccountsByUserId(Long userId);
    AccountEntity findAccountByIdAndUserId(Long id, Long userId);
}
