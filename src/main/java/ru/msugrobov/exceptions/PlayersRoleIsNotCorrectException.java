package ru.msugrobov.exceptions;

public class PlayersRoleIsNotCorrectException extends RuntimeException {

    public PlayersRoleIsNotCorrectException(String errorMessage) {
        super(errorMessage);
    }
}
