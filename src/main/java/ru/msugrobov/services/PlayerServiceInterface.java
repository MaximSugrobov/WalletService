package ru.msugrobov.services;

import ru.msugrobov.DTO.PlayerDTO;
import ru.msugrobov.entities.Player;

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
     * @param playerDTO DTO for player creation
     */
    void createPlayer(PlayerDTO playerDTO);

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
