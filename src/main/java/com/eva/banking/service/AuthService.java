package com.eva.banking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eva.banking.dto.RegisterRequest;
import com.eva.banking.exception.DuplicationException;
import com.eva.banking.model.UserEntity;
import com.eva.banking.repository.AuthRepository;
import com.eva.banking.util.AuthUtils;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity register(RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }

        if (authRepository.findByUsername(request.getUsername()) != null) {
            throw new DuplicationException("Can't register user with this username");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐
        user.setRole("USER");
        return authRepository.save(user);
    }

    public UserEntity getCurrentUser() {
        String username = AuthUtils.getUsername();
        return username != null ? authRepository.findByUsername(username) : null;
    }
}
