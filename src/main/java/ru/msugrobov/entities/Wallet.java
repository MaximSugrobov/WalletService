package ru.msugrobov.entities;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity for wallet
 */
@Data
public class Wallet {
    /**
     * Wallet's identifier
     */
    private Integer id;

    /**
     * Identifier of a player who owns the wallet
     * {@see Player.id}
     */
    private int playerId;

    /**
     * Balance of a wallet
     */
    private BigDecimal balance;
}
