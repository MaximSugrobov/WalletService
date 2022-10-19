package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity for commands
 */
@Data
@AllArgsConstructor
public class AuditEvent {
    public AuditEvent(int playerId, String action, LocalDateTime dateTime, ActionResult actionResult) {
        this.playerId = playerId;
        this.action = action;
        this.dateTime = dateTime;
        this.actionResult = actionResult;
    }

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
