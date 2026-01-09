\
package com.deskreserve.reservation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class SecurityUtil {
    private SecurityUtil() {}

    public static String username(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Object preferred = jwtAuth.getTokenAttributes().get("preferred_username");
            if (preferred != null) return preferred.toString();
            Object sub = jwtAuth.getTokenAttributes().get("sub");
            if (sub != null) return sub.toString();
        }
        return authentication.getName();
    }

    public static boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
