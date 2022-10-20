package ru.msugrobov.DTO;

import lombok.Data;
import ru.msugrobov.entities.Role;

/**
 * DTO for player
 */
@Data
public class PlayerDTO {
    /**
     * Player's identifier
     */
    private Integer id;

    /**
     * Player's first name
     */
    private String firstName;

    /**
     * Player's last name
     */
    private String lastName;

    /**
     * Player's login
     */
    private String login;

    /**
     * Player's password
     */
    private String password;

    /**
     * Player's role ENUM {@link Role}
     */
    private Role role;

    public PlayerDTO(String firstName, String lastName, String login, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
