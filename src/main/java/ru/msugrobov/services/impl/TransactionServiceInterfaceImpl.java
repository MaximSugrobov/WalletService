package ru.msugrobov.services.impl;

import ru.msugrobov.entities.Transaction;
import ru.msugrobov.entities.Type;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.InsufficientBalanceException;
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
     * @param id       identifier
     * @param walletId identifier of the wallet
     * @param type     type of the transaction ENUM {@link Type}
     * @param value    value of transaction
     */
    public void createTransaction(Integer id, int walletId, Type type, BigDecimal value) {
        Wallet walletForTransactionCreation = walletRepositoryForTransactionService.readById(walletId);
        Transaction newTransaction = new Transaction(id, walletId, type, value);
        if (type.equals(Type.DEBIT) && (walletForTransactionCreation.getBalance().subtract(value).signum() != -1)) {
            transactionRepository.create(newTransaction);
        } else if (type.equals(Type.CREDIT)) {
            transactionRepository.create(newTransaction);
        } else {
            throw new InsufficientBalanceException(String
                    .format("Balance of the wallet %s is not sufficient for this transaction", walletId));
        }
    }

    /**
     * Update transaction by id
     *
     * @param idNumber identifier
     * @param value    updated value of transaction
     */
    public void updateTransaction(Integer idNumber, BigDecimal value) {
        Transaction transactionToUpdate = transactionRepository.readById(idNumber);
        transactionToUpdate.setValue(value);
        transactionRepository.update(idNumber, transactionToUpdate);
    }

    /**
     * Delete transaction by id
     *
     * @param idNumber identifier
     */
    public void deleteTransaction(Integer idNumber) {
        transactionRepository.delete(idNumber);
    }
}
