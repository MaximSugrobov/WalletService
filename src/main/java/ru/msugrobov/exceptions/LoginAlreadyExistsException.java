package ru.msugrobov.exceptions;

/**
 * Exception for not existing login
 */
public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
