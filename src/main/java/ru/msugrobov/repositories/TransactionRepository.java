package ru.msugrobov.repositories;

import ru.msugrobov.entities.Transaction;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repository for transaction entity
 * {@inheritDoc}
 */
public class TransactionRepository implements RepositoryInterface<Transaction> {

    private final List<Transaction> storage;
    public TransactionRepository(List<Transaction> storage) {
        this.storage = storage;
    }

    /**
     * Read all transactions
     *
     * @return all entities in storage
     */
    public List<Transaction> readAll() {
        return this.storage.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Read information about transaction by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Transaction readById(Integer idNumber) {
        return this.storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getId(), idNumber))
                .findAny()
                .orElseThrow(() -> new IdNotFoundException(String
                        .format("Transaction with id %s does not exist", idNumber)));
    }

    /**
     * Read all transactions by wallet id
     *
     * @param wallet entity
     * @return All transactions by wallet
     */
    public List<Transaction> readAllTransactionsByWalletId(Wallet wallet) {
        return this.storage.stream()
                .filter(currentRecord -> Objects.equals(currentRecord.getWalletId(), wallet.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Create new transaction entity
     *
     * @param transaction creates entity if not already exists
     */
    public void create(Transaction transaction) {
        if (!existById(transaction.getId())) {
            this.storage.add(transaction);
        } else {
            throw new IdAlreadyExistsException(String
                    .format("Transaction with id %s already exists", transaction.getId()));
        }
    }

    /**
     * Update transaction entity
     *
     * @param idNumber    identifier of an entity to be updated
     * @param transaction updated context of the entity
     */
    public void update(int idNumber, Transaction transaction) {
        Transaction findTransactionById = this.readById(idNumber);
        findTransactionById.updateFrom(transaction);
    }

    /**
     * Delete transaction entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        Transaction findTransactionById = this.readById(idNumber);
        this.storage.remove(findTransactionById);
    }

    private boolean existById(int idNumber) {
        return this.storage.stream().anyMatch(currentRecord -> Objects
                .equals(currentRecord.getId(), idNumber));
    }
}
