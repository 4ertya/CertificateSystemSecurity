package com.epam.esm.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityService {

    public void validateUserAccess(Long userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
    boolean isAdmin = jwtUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).allMatch("ROLE_ADMIN"::equalsIgnoreCase);
        if (!isAdmin && !jwtUser.getId().equals(userId)) {

        throw new AccessDeniedException("The user is not allowed to access other user's resources");
    }
}
}
