package com.crm_for_bot.controller;

import com.crm_for_bot.entity.JwtRequest;
import com.crm_for_bot.entity.JwtResponse;
import com.crm_for_bot.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related endpoints.
 * Provides methods for user login and token revocation.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    /**
     * Service for handling authentication operations.
     */
    private final AuthService authService;

    /**
     * Handles user login requests.
     *
     * @param authRequest the request payload containing username and password.
     * @return ResponseEntity<JwtResponse> - the response containing the JWT token if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        log.info("Attempting login for user: {}", authRequest.getUsername());
        try {
            final JwtResponse token = authService.login(authRequest);
            log.info("Login successful for user: {}", authRequest.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Login failed for user: {}", authRequest.getUsername(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Handles token revocation requests.
     *
     * @param accessToken the token to be revoked.
     * @return ResponseEntity<?> - the response indicating whether the token revocation was successful or not.
     */
    @PostMapping("/revoke")
    public ResponseEntity<?> revokeToken(@RequestParam String accessToken) {
        log.info("Attempting to revoke token");
        boolean isRevokeSuccess = authService.revokeToken(accessToken);
        if (isRevokeSuccess) {
            log.info("Token revoked successfully");
            return ResponseEntity.ok("Token was revoked successfully");
        } else {
            log.warn("Token revocation failed");
            return ResponseEntity.badRequest().body("Token was not revoked");
        }
    }
}
