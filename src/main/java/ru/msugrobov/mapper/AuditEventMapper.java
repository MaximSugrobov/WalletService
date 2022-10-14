package ru.msugrobov.mapper;

import ru.msugrobov.DTO.AuditEventDTO;
import ru.msugrobov.entities.AuditEvent;

/**
 * Mapper for AuditEventDTO and AuditEvent entity conversion
 * {@inheritDoc}
 */
public class AuditEventMapper implements MapperInterface<AuditEvent, AuditEventDTO> {

    /**
     * Converts entity to dto
     *
     * @param auditEvent entity to convert
     * @return dto from the entity
     */
    public AuditEventDTO dtoFromEntity(AuditEvent auditEvent) {
        return new AuditEventDTO(auditEvent.getId(), auditEvent.getPlayerId(), auditEvent.getAction(),
                auditEvent.getDateTime(), auditEvent.getActionResult());
    }

    /**
     * Converts dto to entity
     *
     * @param auditEventDTO dto to convert
     * @return entity from the dto
     */
    public AuditEvent entityFromDto(AuditEventDTO auditEventDTO) {
        return new AuditEvent(auditEventDTO.getId(), auditEventDTO.getPlayerId(), auditEventDTO.getAction(),
                auditEventDTO.getDateTime(), auditEventDTO.getActionResult());
    }
}
