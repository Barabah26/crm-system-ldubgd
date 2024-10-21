package com.crm_for_bot.service;

import com.crm_for_bot.dto.UpdateUserDto;
import com.crm_for_bot.dto.UserDto;
import com.crm_for_bot.entity.User;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing users.
 */
public interface UserService {

     /**
      * Retrieves a user by their login.
      *
      * @param login the login of the user
      * @return an Optional containing the user if found, otherwise empty
      */
     Optional<User> getByLogin(@NonNull String login);

     /**
      * Registers a new user.
      *
      * @param userDto the user details
      * @return the registered user details
      */
     UserDto registerUser(UserDto userDto);

     /**
      * Retrieves all users.
      *
      * @return a list of all users
      */
     List<User> getAllUsers();

     /**
      * Deletes a user by their username.
      *
      * @param username the username of the user to be deleted
      */
     void deleteUserByUsername(String username);

     /**
      * Updates the password for a user.
      *
      * @param username the username of the user
      * @param userDto the new password details
      * @return the updated user details
      */
     UpdateUserDto updateUserPassword(String username, UpdateUserDto userDto);
}
