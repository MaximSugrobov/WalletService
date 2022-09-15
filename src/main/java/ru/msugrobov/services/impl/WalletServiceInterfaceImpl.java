package ru.msugrobov.services.impl;

import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.WalletServiceInterface;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contract for wallet service
 * {@inheritDoc}
 */
public class WalletServiceInterfaceImpl implements WalletServiceInterface {

    public static WalletRepository walletRepository;
    private final PlayerRepository playerRepositoryForWalletService = PlayerServiceInterfaceImpl.playerRepository;

    public WalletServiceInterfaceImpl(WalletRepository walletRepository) {
        WalletServiceInterfaceImpl.walletRepository = walletRepository;
    }

    /**
     * Find all wallets in storage
     *
     * @return all wallets in storage
     */
    public List<Wallet> findAllWallets() {
        return walletRepository.readAll();
    }

    /**
     * Find wallet by id
     *
     * @param idNumber identifier
     * @return wallet entity by id
     */
    public Wallet findById(Integer idNumber) {
        return walletRepository.readById(idNumber);
    }

    /**
     * Create new wallet
     *
     * @param idNumber       identifier
     * @param playerId {@see Player.id} Identifier of a player who owns the wallet
     * @param balance  of the wallet
     */
    public void createWallet(Integer idNumber, int playerId, BigDecimal balance) {
        Wallet newWallet = new Wallet(idNumber, playerId, balance);
        if (playerRepositoryForWalletService.readById(playerId) != null) {
            walletRepository.create(newWallet);
        } else {
            throw new IdNotFoundException(String.format("Player with id %s does not exist", playerId));
        }
    }

    /**
     * Update wallet by id
     *
     * @param idNumber      identifier
     * @param balance of the wallet
     */
    public void updateWallet(Integer idNumber, BigDecimal balance) {
        Wallet walletToBeUpdated = this.findById(idNumber);
        walletRepository.update(idNumber, walletToBeUpdated);
    }

    /**
     * Delete wallet by id
     *
     * @param idNumber identifier
     */
    public void deleteWallet(Integer idNumber) {
        walletRepository.delete(idNumber);
    }
}
