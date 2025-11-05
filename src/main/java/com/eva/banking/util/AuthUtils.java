package com.eva.banking.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {
    private AuthUtils() {
    } // prevent instantiation

    public static String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated())
            return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails user) {
            return user.getUsername();
        }
        return auth.getName();
    }
}
