package ru.msugrobov.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.InsufficientBalanceException;
import ru.msugrobov.repositories.TransactionRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.impl.TransactionServiceInterfaceImpl;
import ru.msugrobov.services.impl.WalletServiceInterfaceImpl;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class TransactionServiceInterfaceImplTest {

    private final TransactionRepository transactionRepositoryMock = mock(TransactionRepository.class);
    private final Transaction testTransaction = new Transaction(1, 1, Type.DEBIT, new BigDecimal(5000));
    private final WalletRepository walletRepositoryMock = mock(WalletRepository.class);
    private final Wallet testWallet = new Wallet(1, 1, new BigDecimal(15000));
    private final WalletServiceInterfaceImpl walletServiceInterfaceImplMock = mock(WalletServiceInterfaceImpl.class,
            withSettings().useConstructor(walletRepositoryMock));
    private final TransactionServiceInterfaceImpl testTransactionService = new TransactionServiceInterfaceImpl
            (transactionRepositoryMock);
    MockitoSession session;

    @BeforeEach
    public void initEach() {
        session = mockitoSession().initMocks(this).startMocking();
    }

    @AfterEach
    public void cleanUpEach() {
        session.finishMocking();
    }

    @Test
    @DisplayName("Test for creating transaction via transaction service")
    public void createTest() {
        when(walletServiceInterfaceImplMock.findById(1)).thenReturn(testWallet);
        when(walletRepositoryMock.readById(1)).thenReturn(testWallet);
        doThrow(IdAlreadyExistsException.class).when(transactionRepositoryMock).create(testTransaction);
        doThrow(InsufficientBalanceException.class).when(transactionRepositoryMock).create(testTransaction);
        doThrow(IdNotFoundException.class).when(transactionRepositoryMock).create(testTransaction);
        doNothing().when(transactionRepositoryMock).create(testTransaction);

        testTransactionService.createTransaction(1, 1, Type.DEBIT, new BigDecimal(5000));

        verify(transactionRepositoryMock).create(testTransaction);
    }

    @Test
    @DisplayName("Test for finding all transactions via transaction service")
    public void findAllTransactionsTest() {
        doReturn(transactionRepositoryMock.readAll()).when(transactionRepositoryMock).readAll();

        testTransactionService.findAllTransactions();

        verify(transactionRepositoryMock, times(2)).readAll();
    }

    @Test
    @DisplayName("Test for finding transaction by id via transaction service")
    public void findByIdTest() {
        doThrow(IdNotFoundException.class).when(transactionRepositoryMock).readById(2);

        testTransactionService.findById(1);

        verify(transactionRepositoryMock).readById(1);
    }

    @Test
    @DisplayName("Test for finding transaction by wallet id via transaction service")
    public void findByWalletIdTest() {
        Wallet walletWithNotExistingPlayerId = new Wallet(2, 2 ,new BigDecimal(10000));
        when(walletRepositoryMock.readById(1)).thenReturn(testWallet);
        doThrow(IdNotFoundException.class).when(transactionRepositoryMock)
                .readAllTransactionsByWalletId(walletWithNotExistingPlayerId);

        testTransactionService.findAllTransactionsByWalletId(1);

        verify(transactionRepositoryMock).readAllTransactionsByWalletId(testWallet);
    }

    @Test
    @DisplayName("Test for updating transaction by id via transaction service")
    public void updateTest() {
        Transaction transactionWithHighValue = new Transaction(2, 1, Type.DEBIT, new BigDecimal(30000));
        when(transactionRepositoryMock.readById(1)).thenReturn(testTransaction);
        when(walletRepositoryMock.readById(1)).thenReturn(testWallet);
        doThrow(IdNotFoundException.class).when(transactionRepositoryMock).update(2, testTransaction);
        doThrow(InsufficientBalanceException.class).when(transactionRepositoryMock)
                .update(1, transactionWithHighValue);

        testTransactionService.updateTransaction(1, new BigDecimal(10000));

        verify(transactionRepositoryMock).update(1, testTransaction);
    }

    @Test
    @DisplayName("Test for deleting transaction via transaction service")
    public void deleteTest() {
        when(transactionRepositoryMock.readById(1)).thenReturn(testTransaction);
        when(walletRepositoryMock.readById(1)).thenReturn(testWallet);
        doThrow(IdNotFoundException.class).when(transactionRepositoryMock).delete(2);

        testTransactionService.deleteTransaction(1);

        verify(transactionRepositoryMock).delete(1);
    }
}
