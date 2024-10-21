package com.crm_for_bot.mapper;

import com.crm_for_bot.dto.UserDto;
import com.crm_for_bot.entity.Role;
import com.crm_for_bot.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A mapper service class for converting between {@link User} entities and {@link UserDto} DTOs.
 * This class extends {@link DtoMapperFacade} to provide custom mapping logic for {@link User} and {@link UserDto}.
 */
@Service
public class UserDtoMapper extends DtoMapperFacade<User, UserDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code UserDtoMapper}.
     * Initializes the base class with {@link User} and {@link UserDto} classes.
     */
    public UserDtoMapper() {
        super(User.class, UserDto.class);
    }

    /**
     * Customizes the entity-to-DTO mapping.
     * Encodes the password and sets roles for the {@link User} entity.
     *
     * @param user the {@link User} entity to be decorated
     * @param dto the {@link UserDto} to be mapped
     */
    @Override
    protected void decorateEntity(User user, UserDto dto) {
        user.setUserName(dto.getUsername());
        user.setEncryptedPassword(passwordEncoder.encode(dto.getPassword()));
        Set<Role> roles = user.getRoles();
        user.setRoles(roles);
    }

    /**
     * Converts a {@link User} entity to a {@link UserDto} DTO.
     * Extracts roles and sets them in the DTO.
     *
     * @param user the {@link User} entity to be converted
     * @return the corresponding {@link UserDto} DTO
     */
    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUserName());
        userDto.setPassword(user.getEncryptedPassword());
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userDto.setRoles(roles);
        return userDto;
    }
}
