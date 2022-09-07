package ru.msugrobov.entities;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity for transaction
 */
@Data
public class Transaction {
    /**
     * Transaction identifier
     */
    private Integer id;

    /**
     * Identifier of a wallet which holds the transaction
     */
    private int walletId;

    /**
     * Type of the transaction ENUM {@link Type}
     */
    private Type type;

    /**
     * Transaction's value
     */
    private BigDecimal value;
}
