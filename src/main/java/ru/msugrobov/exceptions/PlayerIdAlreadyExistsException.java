package ru.msugrobov.exceptions;

/**
 * Exception for already existing player id
 */
public class PlayerIdAlreadyExistsException extends RuntimeException {

    public PlayerIdAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
