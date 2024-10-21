package com.crm_for_bot.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crm_for_bot.entity.JwtResponse;
import com.crm_for_bot.entity.User;
import com.crm_for_bot.exception.AuthException;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for handling JWT token operations such as validation, decoding, and checking expiration.
 */
@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    private final UserServiceImpl userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final Map<String, List<String>> accessStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    /**
     * Validates an access token using JwtProvider.
     *
     * @param accessToken the access token to be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean validateAccessToken(@NonNull String accessToken) {
        return jwtProvider.validateAccessToken(accessToken);
    }

    /**
     * Validates a refresh token using JwtProvider.
     *
     * @param refreshToken the refresh token to be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return jwtProvider.validateRefreshToken(refreshToken);
    }

    /**
     * Decodes a JWT token to retrieve its header and payload.
     *
     * @param token the token to be decoded
     * @return a string containing the header and payload of the token
     * @throws AuthException if the token is invalid
     */
    public String decodeToken(@NonNull String token) {
        if (jwtProvider.validateAccessToken(token)) {
            String[] chunks = token.split("\\.");

            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));

            return header + "\n" + payload;
        } else {
            throw new AuthException("JWT token is invalid");
        }
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token the token to be checked
     * @return true if the token is expired, false otherwise
     */
    public boolean isJwtExpired(@NonNull String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt.before(new Date());
    }
}
