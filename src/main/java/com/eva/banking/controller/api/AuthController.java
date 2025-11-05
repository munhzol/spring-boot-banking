package com.eva.banking.controller.api;

import java.util.HashMap;
import java.util.Map;

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

import com.eva.banking.dto.LoginRequest;
import com.eva.banking.model.UserEntity;
import com.eva.banking.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// @Controller
// public class AuthController {

//     private final UserService userService;

//     public AuthController(UserService userService) {
//         this.userService = userService;
//     }

//     @GetMapping("/register")
//     public String registerForm() {
//         return "register"; // templates/register.html
//     }

//     @PostMapping("/register")
//     public String register(@RequestParam String username,
//             @RequestParam String password) {
//         userService.register(username, password, "USER");
//         return "redirect:/login?registered";
//     }
// }

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        userService.register(username, password, "USER");
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. Username/Password-тэй token үүсгэнэ
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword());

        // 2. AuthenticationManager ашиглаж authenticate хийнэ
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. SecurityContext үүсгээд Authentication-ээ хадгална
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 4. Session үүсгээд SecurityContext-ээ session дотор хадгална
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        // 5. Клиент рүү энгийн JSON буцаая (username + roles)
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("roles", authentication.getAuthorities());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 1️⃣ Clear the security context
        SecurityContextHolder.clearContext();

        // 2️⃣ Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 3️⃣ Return JSON response
        Map<String, String> res = new HashMap<>();
        res.put("message", "Logout successful");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserEntity user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}