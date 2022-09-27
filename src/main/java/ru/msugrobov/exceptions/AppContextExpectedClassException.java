package ru.msugrobov.exceptions;

/**
 * Exception for wrong provided class in app context
 */
public class AppContextExpectedClassException extends RuntimeException {

    public AppContextExpectedClassException(String errorMessage) {
        super(errorMessage);
    }
}
