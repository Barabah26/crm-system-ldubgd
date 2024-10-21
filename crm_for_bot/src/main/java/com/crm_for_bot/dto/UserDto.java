package com.crm_for_bot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for transferring user data.
 * Used for carrying user information between layers of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * The username of the user.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * The password of the user.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Password cannot be blank")
    private String password;

    /**
     * The set of roles assigned to the user.
     * This field can be empty if no roles are assigned.
     */
    private Set<String> roles;
}
