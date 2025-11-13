package com.eva.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 👈 apply to all endpoints
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    // CorsConfiguration c = new CorsConfiguration();
    // c.setAllowedOrigins(List.of("http://localhost:3000")); // "*" БОЛОХГҮЙ ❌
    // c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    // c.setAllowedHeaders(List.of("Content-Type","Authorization"));
    // c.setAllowCredentials(true); // ✅
    // UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
    // s.registerCorsConfiguration("/**", c);
    // return s;
    // }

}