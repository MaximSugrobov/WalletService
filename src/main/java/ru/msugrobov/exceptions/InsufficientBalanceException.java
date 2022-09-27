package ru.msugrobov.exceptions;

/**
 * Exception for insufficient balance of wallet during transaction processing
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String errorMessage) {
        super(errorMessage);
    }
}
