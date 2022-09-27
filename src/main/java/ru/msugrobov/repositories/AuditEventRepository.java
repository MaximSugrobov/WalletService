package ru.msugrobov.repositories;

import ru.msugrobov.entities.AuditEvent;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repository for auditEvent entity
 */
public class AuditEventRepository {

    private final List<AuditEvent> storage;
    public AuditEventRepository(List<AuditEvent> storage) {this.storage = storage;}

    /**
     * Read all events
     *
     * @return all entities in storage
     */
    public List<AuditEvent> readAll() {
        return this.storage.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Read information about event if exists
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public AuditEvent readById(Integer idNumber) {
        return storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getId(), idNumber))
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Event with id %s does not exist", idNumber)));
    }

    /**
     * Read all events by player id
     *
     * @param idNumber identifier of the player
     * @return all commands by player
     */
    public List<AuditEvent> readAllEventsByPlayerId(Integer idNumber) {
        return this.storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getPlayerId(), idNumber))
                .collect(Collectors.toList());
    }

    /**
     * Create new auditEvent entity
     *
     * @param auditEvent creates entity if not already exists
     */
    public void create(AuditEvent auditEvent) {
        if (!existById(auditEvent.getId())) {
            this.storage.add(auditEvent);
        } else {
            throw new IdAlreadyExistsException(String.format("Event with id %s already exists", auditEvent.getId()));
        }
    }

    private boolean existById(int idNumber) {
        return storage.stream().anyMatch(currentRecord -> Objects.equals(currentRecord.getId(), idNumber));
    }
}
