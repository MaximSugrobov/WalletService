package ru.msugrobov.services.impl;

import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.PlayerServiceInterface;

import java.util.List;

/**
 * Contract for player service
 * {@inheritDoc}
 */
public class PlayerServiceInterfaceImpl implements PlayerServiceInterface {

    private final PlayerRepository playerRepository;

    public PlayerServiceInterfaceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Find all players in storage
     *
     * @return all players in storage
     */
    public List<Player> findAllPlayers() {
        return this.playerRepository.readAll();
    }

    /**
     * Find player by id
     *
     * @param idNumber of the player
     * @return player by id
     */
    public Player findById(Integer idNumber) {
        return this.playerRepository.readById(idNumber);
    }

    /**
     * Create new player
     *
     * @param idNumber  of the new player
     * @param firstName of the new player
     * @param lastName  of the new player
     * @param login     of the new player
     * @param password  of the new player
     * @param role      of the new player
     */
    public void createPlayer(int idNumber, String firstName,
                             String lastName, String login, String password, Role role) {
        Player newPlayer = new Player(idNumber, firstName, lastName, login, password, role);
        this.playerRepository.create(newPlayer);
    }

    /**
     * Update player by id
     *
     * @param idNumber         of the updated player
     * @param updatedFirstName of the player
     * @param updatedLastName  of the player
     * @param updatedPassword  of the player
     */
    public void updatePlayer(Integer idNumber, String updatedFirstName, String updatedLastName,
                             String updatedPassword) {
        Player playerToBeUpdated = new Player(idNumber, updatedFirstName, updatedLastName, "anyLogin",
                updatedPassword, Role.USER);
        this.playerRepository.update(idNumber, playerToBeUpdated);
    }

    /**
     * Delete player by id
     *
     * @param idNumber of the deleted player
     */
    public void deletePlayer(int idNumber) {
        this.playerRepository.delete(idNumber);
    }
}
