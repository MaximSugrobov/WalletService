package ru.msugrobov.exceptions;

/**
 * Exception for not existing login
 */
public class LoginNotFoundException extends RuntimeException {
    public LoginNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
