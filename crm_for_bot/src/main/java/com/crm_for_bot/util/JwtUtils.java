package com.crm_for_bot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Utility class for handling JWT operations.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret.access}")
    private String accessTokenSecret;

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token, accessTokenSecret).getSubject();
    }

    /**
     * Extracts the roles from the JWT token.
     *
     * @param token the JWT token
     * @return a list of roles from the token
     */
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token, accessTokenSecret);
        return (List<String>) claims.get("roles");
    }

    /**
     * Retrieves all claims from the JWT token.
     *
     * @param token the JWT token
     * @param secret the secret key used to sign the token
     * @return the claims from the token
     */
    private Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates the JWT token.
     *
     * @param token the JWT token
     * @param username the username to validate against
     * @return true if the token is valid and matches the username, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token, accessTokenSecret).getExpiration();
        return expiration.before(new Date());
    }
}
