package ru.msugrobov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.msugrobov.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;

/**
 * Entity for wallet
 */
@Data
@AllArgsConstructor
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

    /**
     * Update wallet balance by transaction type and value
     *
     * @param direction wallet balance change direction {@link Direction}
     * @param value value of the transaction {@see Transaction.value}
     */
    public void updateBalance(Direction direction, BigDecimal value) {
        BigDecimal updatedBalance;
        if (direction.equals(Direction.NEGATIVE) && (this.balance.subtract(value).signum() != -1)) {
            updatedBalance = this.balance.subtract(value);
        } else if (direction.equals(Direction.POSITIVE)) {
            updatedBalance = this.balance.add(value);
        } else {
            throw new InsufficientBalanceException(String
                    .format("Balance of the wallet %s is not sufficient for this transaction", this.getId()));
        }
        this.setBalance(updatedBalance);
    }
}
