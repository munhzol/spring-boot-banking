package com.eva.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping("/api/demo")
public class DemoController {

    // @GetMapping
    // public String hello() {
    //     return "Hello, welcome to the EVA Banking API!";
    // }


     @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public 🌱";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "User page (needs login)";
    }

    @GetMapping("/admin/home")
    public String adminEndpoint() {
        return "Admin page (needs ADMIN role)";
    }
}
