package ru.msugrobov.services.impl;

import ru.msugrobov.ApplicationContext;
import ru.msugrobov.entities.ActionResult;
import ru.msugrobov.entities.AuditEvent;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.CredentialsErrorException;
import ru.msugrobov.repositories.AuditEventRepository;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.AuditEventServiceInterface;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract for auditEvent service
 * {@inheritDoc}
 */
public class AuditEventServiceInterfaceImpl implements AuditEventServiceInterface {

    public static AuditEventRepository eventRepository;
    private final PlayerRepository playerRepositoryForAuditEventService = PlayerServiceInterfaceImpl.playerRepository;
    public AuditEventServiceInterfaceImpl(AuditEventRepository eventRepository) {
        AuditEventServiceInterfaceImpl.eventRepository = eventRepository;
    }

    /**
     * Find all events in storage
     *
     * @return all events in storage
     */
    public List<AuditEvent> findAllEvents() {
        return eventRepository.readAll();
    }

    /**
     * Find event by id
     *
     * @param idNumber identifier
     * @return event by id
     */
    public AuditEvent findById(Integer idNumber) {
        return eventRepository.readById(idNumber);
    }

    /**
     * Find all events by player id
     *
     * @param playerId identifier of the player
     * @return all events by player id
     */
    public List<AuditEvent> findAllEventsByPlayerId (Integer playerId) {
        return eventRepository.readAllEventsByPlayerId(playerId);
    }

    /**
     * Create and audit new event
     *
     * @param id       identifier
     * @param playerId identifier of the player
     * @param action   performed action by player
     * @param dateTime date and time of performed action
     */
    public void createEvent(Integer id, int playerId, String action,
                            LocalDateTime dateTime, ActionResult actionResult) {
        AuditEvent newEventToSave = new AuditEvent(id, playerId, action, dateTime, actionResult);
        eventRepository.create(newEventToSave);
    }

    /**
     * Authorization of a player
     *
     * @param login    of a player
     * @param password of a player
     */
    public void authorizeAdmin(String login, String password) {
        Player playerToValidateAccess = playerRepositoryForAuditEventService.readByLogin(login);
        if (playerToValidateAccess.getPassword().equals(password)) {
            ApplicationContext appContext = ApplicationContext.initInstance();
            appContext.saveUserInfo(playerToValidateAccess);
        } else {
           throw new CredentialsErrorException(String
                   .format("Player with login %s and password %s does not exist", login, password));
        }
    }
}
