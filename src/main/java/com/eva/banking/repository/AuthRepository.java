package com.eva.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eva.banking.model.UserEntity;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
