package ru.msugrobov.exceptions;

public class LoginNotFoundException extends RuntimeException {
    public LoginNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
