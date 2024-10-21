package com.crm_for_bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class used to indicate that a requested resource was not found.
 * This exception extends {@link RuntimeException} and is annotated with {@link ResponseStatus}
 * to return a 404 Not Found HTTP status code when thrown.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecourseNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code RecourseNotFoundException} with the specified detail message.
     *
     * @param message the detail message to be saved for later retrieval by the {@link #getMessage()} method
     */
    public RecourseNotFoundException(String message) {
        super(message);
    }
}
