package com.crm_for_bot.repository;

import com.crm_for_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} entity by its username.
     *
     * @param userName the username of the user
     * @return an {@link Optional} containing the found {@link User}, or empty if no user is found with the given username
     */
    Optional<User> findUsersByUserName(String userName);

    /**
     * Updates the password of a {@link User} entity identified by its username.
     *
     * @param username the username of the user to be updated
     * @param newPassword the new password to set
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.encryptedPassword = :newPassword WHERE u.userName = :username")
    void updateUserByUsername(@Param("username") String username,
                              @Param("newPassword") String newPassword);
}
