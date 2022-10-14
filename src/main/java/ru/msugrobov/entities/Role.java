package ru.msugrobov.entities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Possible roles for a player
 */
public enum Role {
        USER, ADMIN;

        static final Set<String> listOfRoles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        /**
         * Validate the given string to an ENUM values
         *
         * @param roleToValidate string to validate
         * @return true if string equals role
         */
        public static boolean contains(String roleToValidate) {
                return listOfRoles.contains(roleToValidate);
        }
}

