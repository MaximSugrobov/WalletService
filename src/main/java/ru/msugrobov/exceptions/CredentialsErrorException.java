package ru.msugrobov.exceptions;

/**
 * Exception for credentials error
 */
public class CredentialsErrorException extends RuntimeException {

    public CredentialsErrorException(String errorMessage) {
        super(errorMessage);
    }
}
