package ru.msugrobov.services;

import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;

import java.util.List;

/**
 * Describes contract for player service
 */
public interface PlayerServiceInterface {

    /**
     * Find all players in storage
     *
     * @return all players in storage
     */
    List<Player> findAllPlayers();

    /**
     * Find player by id
     *
     * @param id of the player
     * @return player by id
     */
    Player findById(Integer id);

    /**
     * Create new player
     *
     * @param id of the new player
     * @param firstName of the new player
     * @param lastName of the new player
     * @param login of the new player
     * @param password of the new player
     * @param role of the new player
     */
    void createPlayer(int id, String firstName, String lastName, String login, String password, Role role);

    /**
     * Update player by id
     *
     * @param id of the updated player
     * @param updatedFirstName of the player
     * @param updatedLastName of the player
     * @param updatedPassword of the player
     */
    void updatePlayer(Integer id, String updatedFirstName, String updatedLastName, String updatedPassword);

    /**
     * Delete player by id
     *
     * @param id of the deleted player
     */
    void deletePlayer(int id);
}
