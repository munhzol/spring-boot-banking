package com.eva.banking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eva.banking.model.UserEntity;
import com.eva.banking.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity register(String username, String rawPassword, String role) {
        System.out.println(">>> /register called with: " + username);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // 🔐
        user.setRole(role);
        return repo.save(user);
    }
}