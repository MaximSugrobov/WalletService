package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity for commands
 */
@Data
@AllArgsConstructor
public class Command {
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
     * Update command info by id
     *
     * @param command updated context of the command
     */
    public void updateFrom(Command command) {
        this.action = command.getAction();
        this.dateTime = command.getDateTime();
    }
}
