package ru.msugrobov.services;

import ru.msugrobov.DTO.TransactionDTO;
import ru.msugrobov.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Describes contract for transaction service
 */
public interface TransactionServiceInterface {

    /**
     * Find all transactions in storage
     *
     * @return all transactions in storage
     */
    List<Transaction> findAllTransactions();

    /**
     * Find transaction by id
     *
     * @param id identifier
     * @return transaction by id
     */
    Transaction findById(Integer id);

    /**
     * Find all transactions by wallet id
     *
     * @param walletId identifier of the wallet
     * @return all transactions by wallet id
     */
    List<Transaction> findAllTransactionsByWalletId(Integer walletId);

    /**
     * Create new transaction
     *
     * @param transactionDTO DTO for transaction creation
     */
    void createTransaction(TransactionDTO transactionDTO);

    /**
     * Update transaction by id
     *
     * @param id identifier
     * @param value updated value of transaction
     */
    void updateTransaction(Integer id, BigDecimal value);

    /**
     * Delete transaction by id
     *
     * @param id identifier
     */
    void deleteTransaction(Integer id);

}
