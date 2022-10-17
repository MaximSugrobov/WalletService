package ru.msugrobov.entities;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Possible user actions outcome
 */
public enum ActionResult {

    SUCCESS, FAIL;

    static final Set<String> setOfActionResults = Arrays.stream(ActionResult.values())
            .map(ActionResult::name).collect(Collectors.toSet());

    /**
     * Validate the given string to an ENUM values
     *
     * @param actionResultToValidate string to validate
     * @return true if string equals action result
     */
    public static boolean contains(String actionResultToValidate) {
        return setOfActionResults.contains(actionResultToValidate);
    }
}
