package ru.msugrobov.services.impl;

import ru.msugrobov.DTO.PlayerDTO;
import ru.msugrobov.entities.Player;
import ru.msugrobov.mapper.PlayerMapper;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.PlayerServiceInterface;

import java.util.List;

/**
 * Contract for player service
 * {@inheritDoc}
 */
public class PlayerServiceInterfaceImpl implements PlayerServiceInterface {

    public static PlayerRepository playerRepository;
    private final PlayerMapper playerFromDto = new PlayerMapper();
    public PlayerServiceInterfaceImpl(PlayerRepository playerRepository) {
        PlayerServiceInterfaceImpl.playerRepository = playerRepository;
    }

    /**
     * Find all players in storage
     *
     * @return all players in storage
     */
    public List<Player> findAllPlayers() {
        return playerRepository.readAll();
    }

    /**
     * Find player by id
     *
     * @param idNumber of the player
     * @return player entity by id
     */
    public Player findById(Integer idNumber) {
        return playerRepository.readById(idNumber);
    }

    /**
     * Create new player
     *
     * @param playerDTO DTO for player creation
     */
    public void createPlayer(PlayerDTO playerDTO) {
        Player newPlayer = playerFromDto.entityFromDto(playerDTO);
        playerRepository.create(newPlayer);
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
        Player playerToBeUpdated = playerRepository.readById(idNumber);
        Player updatedPlayer = new Player(idNumber, updatedFirstName, updatedLastName, playerToBeUpdated.getLogin(),
                updatedPassword, playerToBeUpdated.getRole());
        playerRepository.update(idNumber, updatedPlayer);
    }

    /**
     * Delete player by id
     *
     * @param idNumber of the deleted player
     */
    public void deletePlayer(int idNumber) {
        playerRepository.delete(idNumber);
    }
}
