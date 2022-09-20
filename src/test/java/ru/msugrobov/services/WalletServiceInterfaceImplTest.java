package ru.msugrobov.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.PlayerIdAlreadyExistsException;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.repositories.WalletRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;
import ru.msugrobov.services.impl.WalletServiceInterfaceImpl;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class WalletServiceInterfaceImplTest {

    private final WalletRepository walletRepositoryMock = mock(WalletRepository.class);
    private final PlayerRepository playerRepositoryMock = mock(PlayerRepository.class);
    private final PlayerServiceInterfaceImpl playerServiceInterfaceImplMock = mock(PlayerServiceInterfaceImpl.class,
            withSettings().useConstructor(playerRepositoryMock));
    private final WalletServiceInterfaceImpl testWalletService = new WalletServiceInterfaceImpl(walletRepositoryMock);
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
    @DisplayName("Test for creating wallet via wallet service")
    public void createTest() {
        Player testPlayer = new Player(1, "Max", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        Wallet testWallet = new Wallet(1, 1, new BigDecimal(15000));
        when(playerServiceInterfaceImplMock.findById(1)).thenReturn(testPlayer);
        when(playerRepositoryMock.readById(1)).thenReturn(testPlayer);
        testWalletService.createWallet(1, 1, new BigDecimal(15000));
        doThrow(IdAlreadyExistsException.class).when(walletRepositoryMock).create(testWallet);
        doThrow(PlayerIdAlreadyExistsException.class).when(walletRepositoryMock).create(testWallet);
        doThrow(IdNotFoundException.class).when(walletRepositoryMock).create(testWallet);
        doNothing().when(walletRepositoryMock).create(testWallet);
        verify(walletRepositoryMock).create(testWallet);
    }

    @Test
    @DisplayName("Test for finding all wallets via wallet service")
    public void findAllWalletsTest() {
        testWalletService.findAllWallets();
        verify(walletRepositoryMock).readAll();
        doReturn(walletRepositoryMock.readAll()).when(walletRepositoryMock).readAll();
    }

    @Test
    @DisplayName("Test for finding wallet by id via wallet service")
    public void findByIdTest() {
        testWalletService.findById(1);
        verify(walletRepositoryMock).readById(1);
        doThrow(IdNotFoundException.class).when(walletRepositoryMock).readById(1);
    }

    @Test
    @DisplayName("Test for updating wallet by id via wallet service")
    public void updateTest() {
        Wallet testWallet = new Wallet(1, 1, new BigDecimal(15000));
        when(walletRepositoryMock.readById(1)).thenReturn(testWallet);
        testWalletService.updateWallet(1, new BigDecimal(10000));
        verify(walletRepositoryMock).update(1, testWallet);
        doThrow(IdNotFoundException.class).when(walletRepositoryMock).update(1, testWallet);
    }

    @Test
    @DisplayName("Test for deleting wallet via wallet service")
    public void deleteTest() {
        testWalletService.deleteWallet(1);
        verify(walletRepositoryMock).delete(1);
        doThrow(IdNotFoundException.class).when(walletRepositoryMock).delete(1);
    }
}
