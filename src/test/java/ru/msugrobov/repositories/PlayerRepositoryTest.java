package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

public class PlayerRepositoryTest {

    private final List<Player> storage = new ArrayList<>();
    private final PlayerRepository testRepository = new PlayerRepository();
    private Player initPlayer;
    private final SoftAssertions playerRepositoryAssertion = new SoftAssertions();

    @BeforeEach
    public void initEach() {
        this.initPlayer = new Player(1, "Maxim", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        this.testRepository.create(initPlayer);
    }

    @AfterEach
    public void cleanUpEach() {
        String DELETE_ALL_PLAYERS = "DELETE FROM players";
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_ALL_PLAYERS);
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test for creating new player")
    public void createPlayerTest() {
        Player player = new Player(2, "Maxim", "Sugrobov",
                "Snow", "Pass", Role.USER);
        this.testRepository.create(player);
        playerRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        playerRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        playerRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating player with existing login")
    public void createPlayerWithExistingLoginTest() {
        Player playerWithExistingLogin = new Player(2, "Some",
                "Surname", "Max", "123", Role.USER);
        playerRepositoryAssertion.assertThatThrownBy(() -> this.testRepository.create(playerWithExistingLogin))
                .isInstanceOf(LoginAlreadyExistsException.class).hasMessageContaining("login");
        playerRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(1);
        playerRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating player with existing Id")
    public void createPlayerWithExistingIdTest() {
        Player playerWithExistingId = new Player(1, "Patrik",
                "Starfish", "Patrik", "678", Role.USER);
        playerRepositoryAssertion.assertThatThrownBy(() -> this.testRepository.create(playerWithExistingId))
                .isInstanceOf(PlayerIdAlreadyExistsException.class).hasMessageContaining("id");
        playerRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(1);
        playerRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading player info by id")
    public void readByIdTest() {
        Player newPlayer = testRepository.readById(1);
        assertThat(newPlayer).usingRecursiveComparison().isEqualTo(this.initPlayer);
    }

    @Test
    @DisplayName("Test for reading player info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading player by login")
    public void readyByLoginTest() {
        Player newPlayer = testRepository.readByLogin("Max");
        assertThat(newPlayer).usingRecursiveComparison().isEqualTo(this.initPlayer);
    }

    @Test
    @DisplayName("Test for reading player by not existing login")
    public void readByNotExistingLogin() {
        assertThatThrownBy(() -> this.testRepository.readByLogin("Login"))
                .isInstanceOf(LoginNotFoundException.class).hasMessageContaining("login");
    }

    @Test
    @DisplayName("Test for updating player")
    public void updateTest() {
        Player updatedPlayer = new Player(1, "Max", "Snow",
                "Max", "password", Role.ADMIN);
        this.testRepository.update(1, updatedPlayer);
        assertThat(this.testRepository.readById(1)).usingRecursiveComparison().isEqualTo(updatedPlayer);
    }

    @Test
    @DisplayName("Test for updating player with wrong parameters")
    public void updateWithWrongParametersTest() {
        Player updatedPlayer = new Player(1, "Max", "Snow",
                "MaxSnow", "password", Role.ADMIN);
        this.testRepository.update(1, updatedPlayer);
        assertThat(this.testRepository.readById(1)).usingRecursiveComparison().isNotEqualTo(updatedPlayer);
    }

    @Test
    @DisplayName("Test for updating player by not existing id")
    public void updateByNotExistingIdTest() {
        Player updatedPlayer = new Player(1, "Max", "Snow",
                "Max", "password", Role.ADMIN);
        assertThatThrownBy(() -> this.testRepository.update(2, updatedPlayer))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting player")
    public void deleteTest() {
        this.testRepository.delete(1);
        assertThat(this.testRepository.readAll()).hasSize(0);
    }

    @Test
    @DisplayName("Test for deleting player by not existing id")
    public void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.delete(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all players from storage")
    public void readAllTest() {
        Player newPlayer = new Player(2, "Someone", "Sometwo",
                "Somelogin", "Somepass", Role.USER);
        this.testRepository.create(newPlayer);
        this.storage.add(initPlayer);
        this.storage.add(newPlayer);

        List<Player> allPlayersInStorage = this.testRepository.readAll();

        playerRepositoryAssertion.assertThat(allPlayersInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        playerRepositoryAssertion.assertThat(allPlayersInStorage).hasSize(2);
        playerRepositoryAssertion.assertAll();
    }
}