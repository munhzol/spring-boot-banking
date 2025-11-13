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

        if (request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty() ||
                request.getFirstName() == null || request.getFirstName().isEmpty() ||
                request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Email, password, first name, and last name cannot be null or empty");
        }

        if (authRepository.findByEmail(request.getEmail()) != null) {
            throw new DuplicationException("Can't register user with this email");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐
        user.setRole("USER");
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return authRepository.save(user);
    }

    public UserEntity getCurrentUser() {
        String username = AuthUtils.getUsername();
        UserEntity user = username != null ? authRepository.findByEmail(username) : null;
        if (user != null) {
            user.setPassword("********"); // Hide password
        }
        return user;
    }
}
