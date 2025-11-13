package com.eva.banking.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.banking.dto.ApiResponse;
import com.eva.banking.dto.LoginRequest;
import com.eva.banking.dto.RegisterRequest;
import com.eva.banking.exception.DuplicationException;
import com.eva.banking.model.UserEntity;
import com.eva.banking.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
// @CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    // private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {

            UserEntity user = authService.register(request);

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("password", "********"); // Hide password

            return ResponseEntity.ok(
                    new ApiResponse("SUCCESS", "User registered successfully", response));

        } catch (IllegalArgumentException | DuplicationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("ERROR", "Unknown error occurred"));
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // 1) session устгах (шинэ үүсгэхгүй!)
        HttpSession s = request.getSession(false);
        if (s != null)
            s.invalidate();
        SecurityContextHolder.clearContext();

        // 2) JSESSIONID-г УСТГАХ — анх тавьсан атрибуттайгаа яг ИЖИЛ байх ёстой!
        ResponseCookie deleteJsid = ResponseCookie.from("JSESSIONID", "")
                .path("/") // анхны path юу байсан, яг тэр
                .httpOnly(true)
                .secure(false) // DEV http бол false, PROD https бол true
                .sameSite("Lax") // эсвэл None/Strict — анхны утгатай тааруул
                .maxAge(0) // устгах
                .build();
        response.addHeader("Set-Cookie", deleteJsid.toString());

        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "User logged out"));

        // // 1. Clear the security context
        // SecurityContextHolder.clearContext();

        // // 2. Invalidate the session
        // HttpSession session = request.getSession(false);
        // if (session != null) {
        // session.invalidate();
        // }

        // return ResponseEntity.ok(
        // new ApiResponse("SUCCESS", "User logged out successfully"));

    }

    // add try catch
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. Create a token with Username/Password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword());

        // 2. Authenticate using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. Create a SecurityContext and store the Authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 4. Create a session and store the SecurityContext in it
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        // 5. Return a simple JSON response to the client (username + roles)
        Map<String, Object> response = new HashMap<>();
        response.put("email", authentication.getName());
        response.put("roles", authentication.getAuthorities());

        // response.put("firstName",
        // ((UserEntity) authentication.getPrincipal()).getFirstName());
        // response.put("lastName",
        // ((UserEntity) authentication.getPrincipal()).getLastName());

        response.put("user", authService.getCurrentUser());

        response.put("message", "Login successful");

        return ResponseEntity.ok(
                new ApiResponse("SUCCESS", "Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserEntity user = authService.getCurrentUser();
        return ResponseEntity.ok(user);

    }

    @GetMapping("/islogged")
    public ResponseEntity<?> isLoggedIn() {

        try {
            UserEntity user = authService.getCurrentUser();
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("ERROR", "User is not logged in"));
            }
            return ResponseEntity.ok(
                    new ApiResponse("SUCCESS", "User is logged in", user));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("ERROR", "Unknown error occurred"));
        }

        // UserEntity user = authService.getCurrentUser();
        // return ResponseEntity.ok(user);
    }
}