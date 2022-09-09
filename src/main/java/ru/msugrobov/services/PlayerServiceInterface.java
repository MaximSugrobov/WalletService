package ru.msugrobov.services;

import bsh.util.GUIConsoleInterface;
import ru.msugrobov.entities.Player;

import java.io.IOException;
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
    void deletePlayer(int id);
}
