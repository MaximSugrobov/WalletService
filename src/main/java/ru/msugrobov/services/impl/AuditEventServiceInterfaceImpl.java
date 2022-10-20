package ru.msugrobov.services.impl;

import ru.msugrobov.ApplicationContext;
import ru.msugrobov.DTO.AuditEventDTO;
import ru.msugrobov.entities.AuditEvent;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.CredentialsErrorException;
import ru.msugrobov.mapper.AuditEventMapper;
import ru.msugrobov.repositories.AuditEventRepository;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.AuditEventServiceInterface;

import java.util.List;

/**
 * Contract for auditEvent service
 * {@inheritDoc}
 */
public class AuditEventServiceInterfaceImpl implements AuditEventServiceInterface {

    public static AuditEventRepository eventRepository;
    private final AuditEventMapper auditEventMapper = new AuditEventMapper();
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
     * @param auditEventDTO DTO for auditEvent creation
     */
    public void createEvent(AuditEventDTO auditEventDTO) {
        AuditEvent auditEvent = auditEventMapper.entityFromDto(auditEventDTO);
        eventRepository.create(auditEvent);
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
            ApplicationContext appContext = ApplicationContext.INSTANCE;
            appContext.saveInfo("player", playerToValidateAccess);
        } else {
           throw new CredentialsErrorException(String
                   .format("Player with login %s and password %s does not exist", login, password));
        }
    }
}
