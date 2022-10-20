package ru.msugrobov.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for wallet
 */
@Data
@AllArgsConstructor
public class WalletDTO {

    public WalletDTO(int playerId, BigDecimal balance) {
        this.playerId = playerId;
        this.balance = balance;
    }

    /**
     * Wallet's identifier
     */
    private Integer id;

    /**
     * Identifier of a player who owns the wallet
     * {@see PlayerDTO.id}
     */
    private int playerId;

    /**
     * Balance of a wallet
     */
    private BigDecimal balance;
}
