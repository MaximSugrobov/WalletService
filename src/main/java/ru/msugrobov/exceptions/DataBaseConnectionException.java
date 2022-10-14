package ru.msugrobov.exceptions;

/**
 * Exception for database connection
 */
public class DataBaseConnectionException extends RuntimeException {
    public DataBaseConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
