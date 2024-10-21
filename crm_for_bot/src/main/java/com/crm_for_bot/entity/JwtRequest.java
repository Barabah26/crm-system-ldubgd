package com.crm_for_bot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a JWT authentication request.
 * Used to encapsulate the credentials required for JWT-based authentication.
 */
@Getter
@Setter
public class JwtRequest {

    /**
     * The username of the user requesting authentication.
     */
    private String username;

    /**
     * The password of the user requesting authentication.
     */
    private String password;
}
