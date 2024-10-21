package com.crm_for_bot.controller;

import com.crm_for_bot.dto.UpdateUserDto;
import com.crm_for_bot.dto.UserDto;
import com.crm_for_bot.entity.User;
import com.crm_for_bot.exception.RecourseNotFoundException;
import com.crm_for_bot.mapper.UserDtoMapper;
import com.crm_for_bot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling user management operations.
 * Provides endpoints for registering, retrieving, updating, and deleting users.
 */
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/admin")
public class UserController {

    /**
     * Service for handling user operations.
     */
    private final UserService userService;

    /**
     * Mapper for converting User entities to UserDto objects.
     */
    private final UserDtoMapper userDtoMapper;

    /**
     * Registers a new user.
     *
     * @param userDto the user information to register.
     * @return ResponseEntity<?> - the response containing the registered user details or error messages.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        log.info("Attempting to register user: {}", userDto.getUsername());
        try {
            UserDto registeredUser = userService.registerUser(userDto);
            log.info("User registered successfully: {}", registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (RecourseNotFoundException e) {
            log.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("User registration conflict: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Internal server error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity<List<UserDto>> - the response containing a list of all users.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getUser() {
        log.info("Fetching all users");
        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> userDtos = users.stream()
                    .map(userDtoMapper::mapToDto)
                    .collect(Collectors.toList());
            log.info("Successfully fetched {} users", userDtos.size());
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username of the user to delete.
     * @return ResponseEntity<?> - the response indicating the result of the deletion operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteByUsername/{username}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
        log.info("Attempting to delete user: {}", username);
        try {
            userService.deleteUserByUsername(username);
            log.info("User deleted successfully: {}", username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", username, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Updates a user's password by username.
     *
     * @param username the username of the user to update.
     * @param updateUserDto the updated user information.
     * @return ResponseEntity<?> - the response indicating the result of the update operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateByUsername/{username}")
    public ResponseEntity<?> updateUserByUsername(@PathVariable String username, @RequestBody UpdateUserDto updateUserDto) {
        log.info("Attempting to update user: {}", username);
        try {
            UpdateUserDto currentUser = userService.updateUserPassword(username, updateUserDto);
            if (currentUser != null) {
                log.info("User updated successfully: {}", username);
                return ResponseEntity.ok().build();
            } else {
                log.warn("User with username {} not found", username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid user update attempt: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        } catch (RuntimeException e) {
            log.error("Error updating user: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user");
        }
    }
}
