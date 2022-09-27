package ru.msugrobov.services;

import ru.msugrobov.entities.Wallet;

import java.math.BigDecimal;
import java.util.List;

/**
 * Describes contract for wallet service
 */
public interface WalletServiceInterface {

    /**
     * Find all wallets in storage
     *
     * @return all wallets in storage
     */
    List<Wallet> findAllWallets();

    /**
     * Find wallet by id
     *
     * @param id identifier
     * @return wallet by id
     */
    Wallet findById(Integer id);

    /**
     * Find wallet by player id
     *
     * @param id identifier of the player
     * @return wallet by player id
     */
    Wallet findByPlayerId(Integer id);

    /**
     * Create new wallet
     *
     * @param id identifier
     * @param playerId {@see Player.id} Identifier of a player who owns the wallet
     * @param balance of the wallet
     */
    void createWallet(Integer id, int playerId, BigDecimal balance);

    /**
     * Update wallet by id
     *
     * @param id identifier
     * @param balance of the wallet
     */
    void updateWallet(Integer id, BigDecimal balance);

    /**
     * Delete wallet by id
     *
     * @param id identifier
     */
    void deleteWallet(Integer id);
}
