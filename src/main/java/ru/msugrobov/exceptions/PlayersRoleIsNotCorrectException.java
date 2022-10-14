package ru.msugrobov.exceptions;

/**
 * Exception for handle incorrect player's role
 */
public class PlayersRoleIsNotCorrectException extends RuntimeException {

    public PlayersRoleIsNotCorrectException(String errorMessage) {
        super(errorMessage);
    }
}
