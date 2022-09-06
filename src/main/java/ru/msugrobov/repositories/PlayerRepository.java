package ru.msugrobov.repositories;

import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.LoginNotFoundException;

import java.util.List;
import java.util.Objects;

/**
 * Repository for player entity
 * {@inheritDoc}
 */
public class PlayerRepository implements RepositoryInterface<Player> {

    private final List<Player> storage;

    public PlayerRepository(List<Player> storage) {
        this.storage = storage;
    }

    private boolean existByLogin(String login) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getLogin(), login));
    }

    private boolean existById(int idNumber) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getId(), idNumber));
    }

    /**
     * Read the information about player by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Player readById(int idNumber) {
        return storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Player with id %s does not exists", idNumber)));
    }

    /**
     * Create new players entity
     *
     * @param player creates entity if not already exists
     */
    public void create(Player player) {
        if (!existByLogin(player.getLogin()) && !existById(player.getId())) {
            storage.add(player);
        } else if (existByLogin(player.getLogin())) {
            throw new LoginNotFoundException(String.
                    format("Player with login %s already exists", player.getLogin()));
        } else if (existById(player.getId())) {
            throw new IdNotFoundException(String
                    .format("Player with id %s does not exists", player.getId()));
        }
    }

    /**
     * Update players entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param player   updated context of the entity
     */
    public void update(int idNumber, Player player) {
        Player findPlayerById = storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Player with id %s does not exists", idNumber)));
        findPlayerById.updateFrom(player);
    }

    /**
     * Delete players entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        Player findPlayerById = storage.stream()
                .filter(currentRecord -> currentRecord.getId() == idNumber)
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Player with id %s does not exists", idNumber)));
        storage.remove(findPlayerById);
    }
}
