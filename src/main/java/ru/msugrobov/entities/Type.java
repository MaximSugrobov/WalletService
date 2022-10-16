package ru.msugrobov.entities;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Possible transaction types
 */
public enum Type {
    CREDIT, DEBIT;

    static final Set<String> setOfTypes = Arrays.stream(Type.values()).map(Type::name).collect(Collectors.toSet());

    /**
     * Validate the given string to an ENUM values
     *
     * @param roleToValidate string to validate
     * @return true if string equals role
     */
    public static boolean contains(String roleToValidate) {
        return setOfTypes.contains(roleToValidate);
    }
}
