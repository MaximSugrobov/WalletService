package ru.msugrobov.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.msugrobov.entities.Type;

import java.math.BigDecimal;

/**
 * DTO for transaction
 */
@Data
@AllArgsConstructor
public class TransactionDTO {
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
