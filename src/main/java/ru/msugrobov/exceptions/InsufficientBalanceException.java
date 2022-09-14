package ru.msugrobov.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String errorMessage) {
        super(errorMessage);
    }
}
