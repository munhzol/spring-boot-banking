package com.eva.banking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eva.banking.exception.AccessDeniedException;
import com.eva.banking.model.UserEntity;
import com.eva.banking.repository.UserRepository;
import com.eva.banking.util.AuthUtils;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity register(String username, String rawPassword, String role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // 🔐
        user.setRole(role);
        return repo.save(user);
    }

    public UserEntity login(String username, String rawPassword) {
        UserEntity user = repo.findByUsername(username).orElse(null);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    public UserEntity getCurrentUser() {

        System.out.println("-----------------> Getting current user");

        String username = AuthUtils.getUsername();

        if (username == null) {
            throw new AccessDeniedException("No logged-in user");
        }

        UserEntity user = repo.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("No logged-in use"));

        return user;
    }
}