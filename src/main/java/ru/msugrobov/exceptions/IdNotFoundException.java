package ru.msugrobov.exceptions;

/**
 * Exception for not existing id
 */
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
