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

    public AuditEventDTO() {}

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

    /**
     * Set attributes of DTO according to given params
     *
     * @param playerIdFromUIS player identifier
     * @param actionFromUIS action description
     * @param actionResultFromUIS result of the action {@link ActionResult}
     */
    public void updateAuditEventDTO(int playerIdFromUIS, String actionFromUIS, ActionResult actionResultFromUIS) {
        this.playerId = playerIdFromUIS;
        this.action = actionFromUIS;
        this.dateTime = LocalDateTime.now();
        this.actionResult = actionResultFromUIS;
    }
}
