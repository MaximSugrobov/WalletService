package ru.msugrobov.repositories;

import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.PlayerIdAlreadyExistsException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repository for wallet entity
 * {@inheritDoc}
 */
public class WalletRepository implements RepositoryInterface<Wallet> {

    private final List<Wallet> storage;
    public WalletRepository(List<Wallet> storage) { this.storage = storage; }

    /**
     * Read all wallets
     *
     * @return all entities in storage
     */
    public List<Wallet> readAll() {
        return this.storage.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Read information about wallet by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Wallet readById(Integer idNumber) {
        return storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getId(), idNumber))
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Wallet with id %s does not exist", idNumber)));
    }

    /**
     * Create new wallet entity
     *
     * @param wallet creates entity if not already exists
     */
    public void create(Wallet wallet) {
        if (!existById(wallet.getId()) && !existByPlayerId(wallet)) {
            this.storage.add(wallet);
        } else if (existById(wallet.getId())) {
            throw new IdAlreadyExistsException(String
                    .format("Wallet with id %s already exists", wallet.getId()));
        } else if (existByPlayerId(wallet)) {
            throw new PlayerIdAlreadyExistsException(String
                    .format("Wallet with playerId %s already exists", wallet.getPlayerId()));
        }
    }

    /**
     * Update wallet entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param wallet   updated context of the entity
     */
    public void update(int idNumber, Wallet wallet) {
        Wallet findWalletById = this.readById(idNumber);
        findWalletById.setBalance(wallet.getBalance());
    }

    /**
     * Delete wallet entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        Wallet findWalletById = this.readById(idNumber);
        storage.remove(findWalletById);
    }

    private boolean existById(int idNumber) {
        return storage.stream().anyMatch(currentRecord -> Objects
                .equals(currentRecord.getId(), idNumber));
    }

    private boolean existByPlayerId(Wallet wallet) {
        return storage.stream().anyMatch(currentRecord -> Objects
                .equals(currentRecord.getPlayerId(), wallet.getPlayerId()));
    }
}
