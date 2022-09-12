package ru.msugrobov.repositories;

import ru.msugrobov.entities.Command;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repository for command entity
 * {@inheritDoc}
 */
public class CommandRepository implements RepositoryInterface<Command> {

    private final List<Command> storage;
    public CommandRepository(List<Command> storage) {this.storage = storage;}

    /**
     * Read all commands
     *
     * @return all entities in storage
     */
    public List<Command> readAll() {
        return this.storage.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Read information about command if exists
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Command readById(Integer idNumber) {
        return storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getId(), idNumber))
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Command with id %s does not exist", idNumber)));
    }

    /**
     * Read all commands by playerId
     *
     * @param player entity
     * @return all commands by player
     */
    public List<Command> readAllCommandsByPlayerId(Player player) {
        return this.storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getPlayerId(), player.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Create new command entity
     *
     * @param command creates entity if not already exists
     */
    public void create(Command command) {
        if (!existById(command.getId())) {
            this.storage.add(command);
        } else {
            throw new IdAlreadyExistsException(String.format("Command with id %s already exists", command.getId()));
        }
    }

    /**
     * Update entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param command  updated context of the entity
     */
    public void update(int idNumber, Command command) {
        Command findCommandById = this.readById(idNumber);
        findCommandById.updateFrom(command);
    }

    /**
     * Delete entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        Command findCommandById = this.readById(idNumber);
        this.storage.remove(findCommandById);
    }

    private boolean existById(int idNumber) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getId(), idNumber));
    }
}
