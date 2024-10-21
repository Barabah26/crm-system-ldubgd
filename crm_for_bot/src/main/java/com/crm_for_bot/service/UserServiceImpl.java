package com.crm_for_bot.service;

import com.crm_for_bot.dto.UpdateUserDto;
import com.crm_for_bot.dto.UserDto;
import com.crm_for_bot.entity.Role;
import com.crm_for_bot.entity.User;
import com.crm_for_bot.exception.RecourseNotFoundException;
import com.crm_for_bot.repository.RoleRepository;
import com.crm_for_bot.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findUsersByUserName(login);
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        if (userRepository.findUsersByUserName(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUserName(userDto.getUsername());
        user.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RecourseNotFoundException("Role not found"));
            roles.add(role);
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getUserName(), savedUser.getEncryptedPassword(),
                savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findUsersByUserName(username)
                .orElseThrow(() -> new RecourseNotFoundException("User not found with username: " + username));
        userRepository.delete(user);
    }

    @Override
    public UpdateUserDto updateUserPassword(String username, UpdateUserDto userDto) {
        Optional<User> optionalUser = userRepository.findUsersByUserName(username);
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            currentUser.setEncryptedPassword(passwordEncoder.encode(userDto.getNewPassword()));
            userRepository.updateUserByUsername(username, passwordEncoder.encode(userDto.getNewPassword()));
            return userDto;
        } else {
            throw new RecourseNotFoundException("User not found");
        }
    }
}
