package com.crm_for_bot.repository;

import com.crm_for_bot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Role} entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a {@link Role} entity by its name.
     *
     * @param name the name of the role
     * @return an {@link Optional} containing the found {@link Role}, or empty if no role is found with the given name
     */
    Optional<Role> findByName(String name);
}
