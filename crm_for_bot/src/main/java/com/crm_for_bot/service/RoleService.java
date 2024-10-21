package com.crm_for_bot.service;

import com.crm_for_bot.entity.Role;

import java.util.Optional;

/**
 * Service interface for managing user roles.
 */
public interface RoleService {

    /**
     * Finds a role by its name.
     *
     * @param roleName the name of the role
     * @return an Optional containing the role if found, otherwise empty
     */
    Optional<Role> findByName(String roleName);

    /**
     * Saves a new role or updates an existing role.
     *
     * @param role the role to be saved
     * @return the saved role
     */
    Role save(Role role);
}
