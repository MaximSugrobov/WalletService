package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.Transaction;
import ru.msugrobov.entities.Type;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

public class TransactionRepositoryTest {

    private final List<Transaction> storage = new ArrayList<>();
    private final TransactionRepository testRepository = new TransactionRepository(storage);
    private Transaction initTransaction;
    private final Wallet initWallet = new Wallet(1, 1, new BigDecimal(10000));
    private final SoftAssertions transactionRepositoryAssertion = new SoftAssertions();

    @BeforeEach
    void initEach() {
        this.initTransaction = new Transaction(1, initWallet.getId(), Type.DEBIT, new BigDecimal(1000));
        this.testRepository.create(initTransaction);
    }

    @AfterEach
    void cleanUpEach() {
        this.storage.clear();
    }

    @Test
    @DisplayName("Test for creating transaction")
    public void createTest() {
        Transaction testTransaction = new Transaction(2, 2, Type.DEBIT, new BigDecimal(1000));
        this.testRepository.create(testTransaction);
        transactionRepositoryAssertion.assertThat(this.storage).isNotNull().contains(testTransaction);
        transactionRepositoryAssertion.assertThat(this.storage).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating transaction with existing id")
    public void createTransactionWithExistingIdTest() {
        Transaction testTransactionWithExistingId = new Transaction(1, initWallet.getId(),
                Type.DEBIT, new BigDecimal(1000));
        assertThatThrownBy(() -> this.testRepository.create(testTransactionWithExistingId))
                .isInstanceOf(IdAlreadyExistsException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading transaction info by id")
    public void readByIdTest() {
        Transaction newTransaction = this.testRepository.readById(1);
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
        Transaction newTransaction = new Transaction(2, initWallet.getId(), Type.CREDIT, new BigDecimal(1000));
        this.testRepository.create(newTransaction);
        List<Transaction> testTransaction = this.testRepository.readAllTransactionsByWalletId(initWallet);
        transactionRepositoryAssertion.assertThat(testTransaction)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(testTransaction).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for updating transaction")
    public void updateTest() {
        Transaction newTransaction = new Transaction(1, initWallet.getId(), Type.DEBIT, new BigDecimal(5000));
        this.testRepository.update(1, newTransaction);
        assertThat(newTransaction).usingRecursiveComparison().isEqualTo(initTransaction);
    }

    @Test
    @DisplayName("Test for updating transaction with wrong parameters")
    public void updateWithWrongParametersTest() {
        Transaction newTransaction = new Transaction(1, initWallet.getId(), Type.CREDIT, new BigDecimal(5000));
        this.testRepository.update(1, newTransaction);
        assertThat(newTransaction).usingRecursiveComparison().isNotEqualTo(initTransaction);
    }

    @Test
    @DisplayName("Test for updating transaction by not existing id")
    public void updateByNotExistingIdTest() {
        Transaction newTransaction = new Transaction(1, initWallet.getId(), Type.DEBIT, new BigDecimal(5000));
        assertThatThrownBy(() -> this.testRepository.update(2, newTransaction))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting transaction")
    public void deleteTest() {
        this.testRepository.delete(1);
        assertThat(this.storage).hasSize(0);
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
        Transaction newTransaction = new Transaction(2, initWallet.getId(), Type.CREDIT, new BigDecimal(1000));
        this.testRepository.create(newTransaction);
        List<Transaction> allTransactionsInStorage = this.testRepository.readAll();
        transactionRepositoryAssertion.assertThat(allTransactionsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(allTransactionsInStorage).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }
}
