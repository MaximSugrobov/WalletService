package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity for transaction
 */
@Data
@AllArgsConstructor
public class Transaction {
    public Transaction(int walletId, Type type, BigDecimal value) {
        this.walletId = walletId;
        this.type = type;
        this.value = value;
    }

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

    /**
     * Update transaction info by id
     *
     * @param transaction updated context of the transaction
     */
    public void updateFrom(Transaction transaction) {
        this.value = transaction.getValue();
    }
}
