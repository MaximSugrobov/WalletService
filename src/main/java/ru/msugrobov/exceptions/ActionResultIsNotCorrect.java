package ru.msugrobov.exceptions;

/**
 * Exception for handle incorrect action result
 */
public class ActionResultIsNotCorrect extends RuntimeException {

    public ActionResultIsNotCorrect(String errorMessage) {super(errorMessage);}
}
