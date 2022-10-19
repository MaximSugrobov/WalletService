package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

public class TransactionRepositoryTest {

    private final List<Transaction> storage = new ArrayList<>();
    private final WalletRepository testWalletRepository = new WalletRepository();
    private final TransactionRepository testRepository = new TransactionRepository();
    private Transaction initTransaction;
    private Wallet adminWallet;
    private int initTransactionId;
    private final SoftAssertions transactionRepositoryAssertion = new SoftAssertions();

    @BeforeEach
    void initEach() {
        this.adminWallet = this.testWalletRepository.readById(1);
        this.initTransaction = new Transaction(adminWallet.getId(), Type.DEBIT, new BigDecimal(1000));
        this.testRepository.create(initTransaction);
        this.initTransactionId = this.testRepository.readAllTransactionsByWalletId
                (this.adminWallet).get(0).getId();
        this.initTransaction.setId(initTransactionId);
    }

    @AfterEach
    void cleanUpEach() {
        String DELETE_ALL_TRANSACTIONS = "DELETE FROM transactions";
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statementClearTransactions = connection.prepareStatement(DELETE_ALL_TRANSACTIONS);
            statementClearTransactions.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test for creating transaction")
    public void createTest() {
        Transaction testTransaction = new Transaction(1, Type.DEBIT, new BigDecimal(1000));
        this.testRepository.create(testTransaction);
        transactionRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        transactionRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading transaction info by id")
    public void readByIdTest() {
        Transaction newTransaction = this.testRepository.readById(initTransactionId);
        assertThat(newTransaction).usingRecursiveComparison().isEqualTo(initTransaction);
    }

    @Test
    @DisplayName("Test for reading transaction info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all transactions by wallet id")
    public void readAllTransactionsByWalletIdTest() {
        Transaction newTransaction = new Transaction(adminWallet.getId(), Type.CREDIT, new BigDecimal(2000));
        this.testRepository.create(newTransaction);
        newTransaction.setId(this.testRepository.readAllTransactionsByWalletId(adminWallet).get(1).getId());
        this.storage.add(initTransaction);
        this.storage.add(newTransaction);

        List<Transaction> testTransaction = this.testRepository.readAllTransactionsByWalletId(adminWallet);

        transactionRepositoryAssertion.assertThat(testTransaction)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(testTransaction).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for updating transaction")
    public void updateTest() {
        Transaction newTransaction = new Transaction(initTransactionId,
                adminWallet.getId(), Type.DEBIT, new BigDecimal(5000));
        this.testRepository.update(initTransactionId, newTransaction);
        assertThat(this.testRepository.readById(initTransactionId))
                .usingRecursiveComparison().isEqualTo(newTransaction);
    }

    @Test
    @DisplayName("Test for updating transaction with wrong parameters")
    public void updateWithWrongParametersTest() {
        Transaction newTransaction = new Transaction(initTransactionId,
                adminWallet.getId(), Type.CREDIT, new BigDecimal(5000));
        this.testRepository.update(initTransactionId, newTransaction);
        assertThat(this.testRepository.readById(initTransactionId))
                .usingRecursiveComparison().isNotEqualTo(newTransaction);
    }

    @Test
    @DisplayName("Test for updating transaction by not existing id")
    public void updateByNotExistingIdTest() {
        Transaction newTransaction = new Transaction(1, adminWallet.getId(), Type.DEBIT, new BigDecimal(5000));
        assertThatThrownBy(() -> this.testRepository.update(2, newTransaction))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting transaction")
    public void deleteTest() {
        this.testRepository.delete(initTransactionId);
        assertThat(this.testRepository.readAll()).hasSize(0);
    }

    @Test
    @DisplayName("Test for deleting transaction by not existing id")
    public void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.delete(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all transactions from storage")
    public void readAllTest() {
        Transaction newTransaction = new Transaction(adminWallet.getId(), Type.CREDIT, new BigDecimal(1000));
        this.testRepository.create(newTransaction);
        newTransaction.setId(this.testRepository.readAllTransactionsByWalletId(adminWallet).get(1).getId());
        this.storage.add(initTransaction);
        this.storage.add(newTransaction);

        List<Transaction> allTransactionsInStorage = this.testRepository.readAll();

        transactionRepositoryAssertion.assertThat(allTransactionsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(allTransactionsInStorage).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }
}
