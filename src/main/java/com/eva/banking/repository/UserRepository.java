package com.eva.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eva.banking.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
}