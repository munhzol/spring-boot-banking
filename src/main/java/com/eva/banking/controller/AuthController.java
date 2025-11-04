package com.eva.banking.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.banking.service.UserService;

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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        userService.register(username, password, "USER");
        return ResponseEntity.ok("User registered successfully!");
    }
}