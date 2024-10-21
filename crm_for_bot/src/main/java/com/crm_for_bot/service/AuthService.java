package com.crm_for_bot.service;

import com.crm_for_bot.entity.JwtRequest;
import com.crm_for_bot.entity.JwtResponse;
import com.crm_for_bot.entity.Role;
import com.crm_for_bot.entity.User;
import com.crm_for_bot.exception.AuthException;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling authentication-related operations such as user login, token management, and token revocation.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Handles user login by validating credentials and generating JWT tokens.
     *
     * @param authRequest the authentication request containing username and password
     * @return a {@link JwtResponse} containing access token, refresh token, and user role
     * @throws AuthException if the username is null or the user is not found, or the password is incorrect
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        if (authRequest.getUsername() == null) {
            throw new AuthException("Username is null");
        }
        final User user = userService.getByLogin(authRequest.getUsername())
                .orElseThrow(() -> new AuthException("User not found"));

        if (passwordEncoder.matches(authRequest.getPassword(), user.getEncryptedPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            jwtService.getRefreshStorage().put(user.getUserName(), refreshToken);

            List<String> accessTokens = jwtService.getAccessStorage().computeIfAbsent(user.getUserName(), k -> new ArrayList<>());
            accessTokens.add(accessToken);
            jwtService.getAccessStorage().put(user.getUserName(), accessTokens);

            String role = user.getRoles().stream()
                    .map(Role::getName)
                    .findFirst()
                    .orElse("USER");

            return new JwtResponse(accessToken, refreshToken, role);
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    /**
     * Revokes a given access token by removing it from the storage.
     *
     * @param accessToken the access token to be revoked
     * @return true if the token was successfully revoked, false otherwise
     */
    public boolean revokeToken(@NonNull String accessToken) {
        if (jwtProvider.validateAccessToken(accessToken)) {
            final Claims claims = jwtProvider.getAccessClaims(accessToken);
            final String login = claims.getSubject();
            List<String> tokens = jwtService.getAccessStorage().get(login);
            if (tokens != null) {
                tokens.remove(accessToken);
                if (tokens.isEmpty()) {
                    jwtService.getAccessStorage().remove(login);
                } else {
                    jwtService.getAccessStorage().put(login, tokens);
                }
                return true;
            }
        }
        return false;
    }
}
