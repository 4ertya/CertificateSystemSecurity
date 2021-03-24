package com.epam.esm.security;

import com.epam.esm.exception.security.InvalidTokenException;
import com.epam.esm.exception.security.TokenExpiredException;
import com.epam.esm.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final static String BEARER = "Bearer ";
    private final UserService userService;

    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expirationInMinutes;

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plusMinutes(expirationInMinutes);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("40002");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("40004");
        }

    }


    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(authorizationHeader);
        if (bearerToken == null) {
            return null;
        }
        return bearerToken.substring(BEARER.length());
    }

}
