package com.crm_for_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Entity representing the response returned after a successful JWT authentication.
 * Contains the JWT tokens and user role information.
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    /**
     * The type of the token. Always set to "Bearer" for JWT tokens.
     */
    private final String type = "Bearer";

    /**
     * The access token issued upon successful authentication.
     * This token is used to access protected resources.
     */
    private String accessToken;

    /**
     * The refresh token issued along with the access token.
     * This token is used to obtain a new access token when the current one expires.
     */
    private String refreshToken;

    /**
     * The role assigned to the user.
     * This field indicates the user's role in the application, such as "USER" or "ADMIN".
     */
    private String role;
}
