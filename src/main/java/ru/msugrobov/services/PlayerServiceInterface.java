package ru.msugrobov.services;

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
     * @return player by id
     */
    Player findById(int id);

    /**
     * Create player
     */
    void createPlayer();

    /**
     * Update player
     */
    void updatePlayer();

    /**
     * Delete player
     */
    void deletePlayer();
}
