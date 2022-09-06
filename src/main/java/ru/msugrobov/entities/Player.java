package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private Integer id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Role role;
}
