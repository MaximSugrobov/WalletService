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
            BigDecimal updatedBalance = walletForTransactionCreation.getBalance().subtract(value);
            walletForTransactionCreation.setBalance(updatedBalance);
            walletRepositoryForTransactionService.update(walletId, walletForTransactionCreation);
        } else if (type.equals(Type.CREDIT)) {
            transactionRepository.create(newTransaction);
            BigDecimal updatedBalance = walletForTransactionCreation.getBalance().add(value);
            walletForTransactionCreation.setBalance(updatedBalance);
            walletRepositoryForTransactionService.update(walletId, walletForTransactionCreation);
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
        int walletId = transactionToUpdate.getWalletId();
        Wallet walletForTransactionUpdate = walletRepositoryForTransactionService.readById(walletId);
        BigDecimal valueDiff = transactionToUpdate.getValue().subtract(value);
        BigDecimal updatedBalance;

        switch (valueDiff.signum()) {
            case (1):
                if (transactionToUpdate.getType().equals(Type.DEBIT)) {
                    updatedBalance = walletForTransactionUpdate.getBalance().add(valueDiff);
                } else {
                    updatedBalance = walletForTransactionUpdate.getBalance().subtract(valueDiff);
                }
                walletForTransactionUpdate.setBalance(updatedBalance);
                transactionToUpdate.setValue(value);
                transactionRepository.update(idNumber, transactionToUpdate);
                walletRepositoryForTransactionService.update(walletId, walletForTransactionUpdate);
                break;
            case (-1):
                if (transactionToUpdate.getType().equals(Type.DEBIT) &&
                        (walletForTransactionUpdate.getBalance().subtract(valueDiff)).signum() != -1) {
                    updatedBalance = walletForTransactionUpdate.getBalance().add(valueDiff);
                } else if (transactionToUpdate.getType().equals(Type.CREDIT)) {
                    updatedBalance = walletForTransactionUpdate.getBalance().subtract(valueDiff);
                } else {
                    throw new InsufficientBalanceException(String
                            .format("Balance of the wallet %s is not sufficient for this transaction",
                                    walletForTransactionUpdate.getId()));
                }
                walletForTransactionUpdate.setBalance(updatedBalance);
                transactionToUpdate.setValue(value);
                transactionRepository.update(idNumber, transactionToUpdate);
                walletRepositoryForTransactionService.update(walletId, walletForTransactionUpdate);
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
        BigDecimal updatedBalance;

        if (transactionToDelete.getType().equals(Type.DEBIT)) {
            updatedBalance = walletForTransactionDelete.getBalance().add(transactionToDelete.getValue());
        } else {
            updatedBalance = walletForTransactionDelete.getBalance().subtract(transactionToDelete.getValue());
        }
        walletForTransactionDelete.setBalance(updatedBalance);
        walletRepositoryForTransactionService.update(walletId, walletForTransactionDelete);
        transactionRepository.delete(idNumber);
    }
}
