package ru.msugrobov.exceptions;

/**
 * Exception for handle incorrect transaction type
 */
public class TransactionTypeIsNotCorrect extends RuntimeException {

    public TransactionTypeIsNotCorrect(String errorMessage) {
        super(errorMessage);
    }
}
