package com.crm_for_bot.exception;

/**
 * Custom exception class used for authentication-related errors.
 * This exception extends {@link RuntimeException} and is thrown when
 * authentication processes encounter issues.
 */
public class AuthException extends RuntimeException {

    /**
     * Constructs a new {@code AuthException} with the specified detail message.
     *
     * @param message the detail message to be saved for later retrieval by the {@link #getMessage()} method
     */
    public AuthException(String message) {
        super(message);
    }
}
