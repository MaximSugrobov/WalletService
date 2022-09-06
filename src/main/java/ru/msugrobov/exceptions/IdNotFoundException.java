package ru.msugrobov.exceptions;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
