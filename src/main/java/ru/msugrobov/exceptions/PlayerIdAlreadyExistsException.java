package ru.msugrobov.exceptions;

public class PlayerIdAlreadyExistsException extends RuntimeException {

    public PlayerIdAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
