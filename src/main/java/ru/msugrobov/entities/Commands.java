package ru.msugrobov.entities;

import lombok.Data;

import java.util.Date;

/**
 * Entity for commands
 */
@Data
public class Commands {
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
    private Date date;
}
