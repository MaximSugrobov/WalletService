package ru.msugrobov.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.MockitoSession;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.LoginAlreadyExistsException;
import ru.msugrobov.repositories.PlayerRepository;
import ru.msugrobov.services.impl.PlayerServiceInterfaceImpl;


import java.util.ArrayList;
import java.util.List;


public class PlayerServiceInterfaceImplTest {

    List<Player> storage = new ArrayList<>();
    PlayerRepository playerRepositoryMock = mock(PlayerRepository.class,
            withSettings().useConstructor(storage));
    private final PlayerServiceInterfaceImpl testPlayerService = new PlayerServiceInterfaceImpl(playerRepositoryMock);
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
    @DisplayName("Test for creating player via player service")
    public void createTest() {
        testPlayerService.createPlayer(1, "Max", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        Player testPlayer = new Player(1, "Max", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        doThrow(IdAlreadyExistsException.class).when(playerRepositoryMock).create(testPlayer);
        doThrow(LoginAlreadyExistsException.class).when(playerRepositoryMock).create(testPlayer);
        doNothing().when(playerRepositoryMock).create(testPlayer);
        verify(playerRepositoryMock).create(testPlayer);
    }

    @Test
    @DisplayName("Test for finding all players via player service")
    public void findAllPlayerTest() {
        testPlayerService.findAllPlayers();
        verify(playerRepositoryMock).readAll();
        doReturn(playerRepositoryMock.readAll()).when(playerRepositoryMock).readAll();
    }

    @Test
    @DisplayName("Test for finding player by id via player service")
    public void findByIdTest() {
        testPlayerService.findById(1);
        verify(playerRepositoryMock).readById(1);
        doThrow(IdNotFoundException.class).when(playerRepositoryMock).readById(1);
    }

    @Test
    @DisplayName("Test for updating player by id via player service")
    public void updateTest() {
        Player testPlayer = new Player(1, "Max", "Snow",
                "SomeLogin", "Pass", Role.USER);
        when(playerRepositoryMock.readById(1)).thenReturn(testPlayer);
        testPlayerService.updatePlayer(1, "Max", "Snow", "Pass");
        verify(playerRepositoryMock).update(1, testPlayer);
        doThrow(IdNotFoundException.class).when(playerRepositoryMock).update(1, testPlayer);
    }

    @Test
    @DisplayName("Test for deleting player via player service")
    public void deleteTest() {
        testPlayerService.deletePlayer(1);
        verify(playerRepositoryMock).delete(1);
        doThrow(IdNotFoundException.class).when(playerRepositoryMock).delete(1);
    }
}
