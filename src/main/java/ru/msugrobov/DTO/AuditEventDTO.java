package ru.msugrobov.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.msugrobov.entities.ActionResult;

import java.time.LocalDateTime;

/**
 * DTO for audit event
 */
@Data
@AllArgsConstructor
public class AuditEventDTO {
    /**
     * Identifier
     */
    private Integer id;

    /**
     * Identifier of a player who performed the command
     */
    private int playerId;

    /**
     * Performed command
     */
    private String action;

    /**
     * Date of performed command
     */
    private LocalDateTime dateTime;

    /**
     * Result of the player's action ENUM {@link ActionResult}
     */
    private ActionResult actionResult;
}
