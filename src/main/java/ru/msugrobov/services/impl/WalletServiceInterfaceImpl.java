package ru.msugrobov.services.impl;

import ru.msugrobov.DTO.WalletDTO;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.mapper.WalletMapper;
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
    private final WalletMapper walletMapper = new WalletMapper();
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
     * Find wallet by player id
     *
     * @param idNumber identifier of the player
     * @return wallet by player id
     */
    public Wallet findByPlayerId(Integer idNumber) {
        return walletRepository.readByPlayerId(idNumber);
    }

    /**
     * Create new wallet
     *
     * @param walletDTO DTO for wallet creation
     */
    public void createWallet(WalletDTO walletDTO) {
        Wallet newWallet = walletMapper.entityFromDto(walletDTO);
        if (playerRepositoryForWalletService.readById(newWallet.getPlayerId()) != null) {
            walletRepository.create(newWallet);
        } else {
            throw new IdNotFoundException(String.format("Player with id %s does not exist", newWallet.getPlayerId()));
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
