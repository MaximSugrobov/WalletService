package ru.msugrobov.exceptions;

/**
 * Exception for already existing id
 */
public class IdAlreadyExistsException extends RuntimeException {
    public IdAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
