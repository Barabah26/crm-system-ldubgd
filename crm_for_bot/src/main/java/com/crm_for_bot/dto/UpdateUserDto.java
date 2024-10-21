package com.crm_for_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for updating user information.
 * Used to transfer data required to update a user's password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    /**
     * The new password to be set for the user.
     */
    private String newPassword;
}
