package ru.msugrobov.services;

import ru.msugrobov.DTO.AuditEventDTO;
import ru.msugrobov.entities.AuditEvent;

import java.util.List;

/**
 * Describes contract for AuditEvent service
 */
public interface AuditEventServiceInterface {

    /**
     * Find all events in storage
     *
     * @return all events in storage
     */
    List<AuditEvent> findAllEvents();

    /**
     * Find event by id
     *
     * @param id identifier
     * @return event by id
     */
    AuditEvent findById(Integer id);

    /**
     * Find all events by player id
     *
     * @param playerId identifier of the player
     * @return all events by player id
     */
    List<AuditEvent> findAllEventsByPlayerId(Integer playerId);

    /**
     * Create and audit new event
     *
     * @param auditEventDTO DTO for auditEvent creation
     */
    void createEvent(AuditEventDTO auditEventDTO);

    /**
     * Authorization of a player
     *
     * @param login of a player
     * @param password of a player
     */
    void authorizeAdmin(String login, String password);
}
