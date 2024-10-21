package com.crm_for_bot.service;

import com.crm_for_bot.entity.Role;
import com.crm_for_bot.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component for generating and validating JWT tokens.
 */
@Slf4j
@Component
public class JwtProvider {

    public static final int ACCESS_LEAVE_MINUTES = 30;
    public static final int ACCESS_LEAVE_HOURS = 24;
    public static final int REFRESH_LEAVE_DAYS = 30;
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    /**
     * Constructor to initialize JwtProvider with secret keys for access and refresh tokens.
     *
     * @param jwtAccessSecret the secret key for access tokens
     * @param jwtRefreshSecret the secret key for refresh tokens
     */
    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    /**
     * Generates an access token for a given user.
     *
     * @param user the user for whom the access token is generated
     * @return the generated access token
     */
    public String generateAccessToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusHours(ACCESS_LEAVE_MINUTES).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUserName())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("userId", user.getUserId())
                .claim("roles", roles)
                .compact();
    }

    /**
     * Generates a refresh token for a given user.
     *
     * @param user the user for whom the refresh token is generated
     * @return the generated refresh token
     */
    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(REFRESH_LEAVE_DAYS).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUserName())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    /**
     * Validates an access token.
     *
     * @param accessToken the access token to be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    /**
     * Validates a refresh token.
     *
     * @param refreshToken the refresh token to be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    /**
     * Extracts claims from an access token.
     *
     * @param token the access token from which claims are extracted
     * @return the claims extracted from the token
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }


    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
