package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entity for a player
 */
@Data
@AllArgsConstructor
public class Player {
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

    /**
     * Update player info by id
     *
     * @param player updated context of the player
     */
    public void updateFrom(Player player) {
        this.firstName = player.getFirstName();
        this.lastName = player.getLastName();
        this.password = player.getPassword();
    }
}
