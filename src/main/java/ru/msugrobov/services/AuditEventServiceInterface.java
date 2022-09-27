package ru.msugrobov.services;

import ru.msugrobov.entities.ActionResult;
import ru.msugrobov.entities.AuditEvent;

import java.time.LocalDateTime;
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
     * @param id identifier
     * @param playerId identifier of the player
     * @param action performed action by player
     * @param dateTime date and time of performed action
     */
    void createEvent(Integer id, int playerId, String action, LocalDateTime dateTime, ActionResult actionResult);

    /**
     * Authorization of a player
     *
     * @param login of a player
     * @param password of a player
     */
    void authorizeAdmin(String login, String password);
}
