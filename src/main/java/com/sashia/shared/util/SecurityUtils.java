package com.sashia.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    // ============================ CURRENT USER ============================

    public static SashiaUser getCurrentUser() {
        return (SashiaUser) getRequiredAuthentication().getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUserVipGroup() {
        return getCurrentUser().getVipGroup();
    }

    public static String getCurrentUserUsername() {
        return getCurrentUser().getUsername();
    }

    public static Collection<GrantedAuthority> getAuthorities() {
        return getCurrentUser().getAuthorities();
    }

    // ============================ AUTHENTICATION ============================

    public static boolean isAuthenticated() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof SashiaUser;
    }

    public static boolean isAnonymous() {
        return !isAuthenticated();
    }

    // ============================ AUTHORITIES ============================

    public static boolean hasAuthority(String authority) {
        return getAuthorities().stream()
                .anyMatch(granted -> Objects.equals(granted.getAuthority(), authority));
    }

    public static boolean hasAnyAuthority(String... authorities) {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(List.of(authorities)::contains);
    }

    public static boolean hasNoneOfAuthorities(String... authorities) {
        return !hasAnyAuthority(authorities);
    }

    // ============================ INTERNAL ============================

    private static Authentication getRequiredAuthentication() {
        Authentication authentication = getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof SashiaUser)) {
            throw new IllegalStateException("No authenticated user");
        }

        return authentication;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}