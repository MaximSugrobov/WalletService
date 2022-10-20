package ru.msugrobov.services.impl;

import ru.msugrobov.DTO.TransactionDTO;
import ru.msugrobov.entities.Direction;
import ru.msugrobov.entities.Transaction;
import ru.msugrobov.entities.Type;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.mapper.TransactionMapper;
import ru.msugrobov.repositories.TransactionRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.TransactionServiceInterface;

import java.math.BigDecimal;
import java.util.List;

/**
 * Describes transaction service
 * {@inheritDoc}
 */
public class TransactionServiceInterfaceImpl implements TransactionServiceInterface {

    public static TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper = new TransactionMapper();
    private final WalletRepository walletRepositoryForTransactionService = WalletServiceInterfaceImpl.walletRepository;
    public TransactionServiceInterfaceImpl(TransactionRepository transactionRepository) {
        TransactionServiceInterfaceImpl.transactionRepository = transactionRepository;
    }

    /**
     * Find all transactions in storage
     *
     * @return all transactions in storage
     */
    public List<Transaction> findAllTransactions() {
        return transactionRepository.readAll();
    }

    /**
     * Find transaction by id
     *
     * @param idNumber identifier
     * @return transaction entity by id
     */
    public Transaction findById(Integer idNumber) {
        return transactionRepository.readById(idNumber);
    }

    /**
     * Find all transactions by wallet id
     *
     * @param walletId identifier of the wallet
     * @return all transactions by wallet id
     */
    public List<Transaction> findAllTransactionsByWalletId(Integer walletId) {
        Wallet walletForTransactionSearch = walletRepositoryForTransactionService.readById(walletId);
        return transactionRepository.readAllTransactionsByWalletId(walletForTransactionSearch);
    }

    /**
     * Create new transaction
     *
     * @param transactionDTO DTO for transaction creation
     */
    public void createTransaction(TransactionDTO transactionDTO) {
        Wallet walletForTransactionCreation = walletRepositoryForTransactionService
                .readById(transactionDTO.getWalletId());
        Transaction newTransaction = transactionMapper.entityFromDto(transactionDTO);
        Direction direction;

        if (newTransaction.getType().equals(Type.DEBIT)) {
            direction = Direction.NEGATIVE;
        } else {
            direction = Direction.POSITIVE;
        }
        walletForTransactionCreation.updateBalance(direction, newTransaction.getValue());
        transactionRepository.create(newTransaction);
    }

    /**
     * Update transaction by id
     *
     * @param idNumber identifier
     * @param value    updated value of transaction
     */
    public void updateTransaction(Integer idNumber, BigDecimal value) {
        Transaction transactionToUpdate = transactionRepository.readById(idNumber);
        int walletId = transactionToUpdate.getWalletId();
        Wallet walletForTransactionUpdate = walletRepositoryForTransactionService.readById(walletId);
        BigDecimal valueDiff = transactionToUpdate.getValue().subtract(value);
        Direction direction;

        switch (valueDiff.signum()) {
            case (1):
                if (transactionToUpdate.getType().equals(Type.DEBIT)) {
                    direction = Direction.POSITIVE;
                } else {
                    direction = Direction.NEGATIVE;
                }
                transactionToUpdate.setValue(value);
                transactionRepository.update(idNumber, transactionToUpdate);
                walletForTransactionUpdate.updateBalance(direction, valueDiff);
                break;
            case (-1):
                if (transactionToUpdate.getType().equals(Type.DEBIT)) {
                    direction = Direction.NEGATIVE;
                } else {
                    direction = Direction.POSITIVE;
                }
                walletForTransactionUpdate.updateBalance(direction, valueDiff.abs());
                transactionToUpdate.setValue(value);
                transactionRepository.update(idNumber, transactionToUpdate);
                break;
        }
    }

    /**
     * Delete transaction by id
     *
     * @param idNumber identifier
     */
    public void deleteTransaction(Integer idNumber) {
        Transaction transactionToDelete = transactionRepository.readById(idNumber);
        int walletId = transactionToDelete.getWalletId();
        Wallet walletForTransactionDelete = walletRepositoryForTransactionService.readById(walletId);
        Direction direction;

        if (transactionToDelete.getType().equals(Type.DEBIT)) {
            direction = Direction.POSITIVE;
        } else {
            direction = Direction.NEGATIVE;
        }
        walletForTransactionDelete.updateBalance(direction, transactionToDelete.getValue());
        transactionRepository.delete(idNumber);
    }
}
