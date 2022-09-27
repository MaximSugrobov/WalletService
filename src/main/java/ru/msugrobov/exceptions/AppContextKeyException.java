package ru.msugrobov.exceptions;

/**
 * Exception for wrong key in application context map
 */
public class AppContextKeyException extends RuntimeException {

    public AppContextKeyException(String errorMessage) {
        super(errorMessage);
    }
}
