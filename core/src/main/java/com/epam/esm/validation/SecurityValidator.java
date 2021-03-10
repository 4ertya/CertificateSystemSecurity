package com.epam.esm.validation;

import com.epam.esm.model.Role;
import com.epam.esm.security.JwtUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {
    public void validateUserAccess(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        boolean isAdmin = jwtUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(Role.ROLE_ADMIN.name()::equalsIgnoreCase);
        if (!isAdmin && !jwtUser.getId().equals(userId)) {
            throw new AccessDeniedException("40003");
        }
    }
}
