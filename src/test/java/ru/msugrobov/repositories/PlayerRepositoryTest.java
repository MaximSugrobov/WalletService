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
    private Player testAdminPlayer;
    private final SoftAssertions playerRepositoryAssertion = new SoftAssertions();

    @BeforeEach
    public void initEach() {
        this.testAdminPlayer = new Player("Maxim", "Sugrobov",
                "testAdmin", "admin", Role.ADMIN);
        this.testRepository.create(testAdminPlayer);
    }

    @AfterEach
    public void cleanUpEach() {
        String DELETE_ALL_PLAYERS = "DELETE FROM players WHERE login <> 'admin'";
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
        Player testPlayer = new Player("Maxim", "Sugrobov",
                "Snow", "Pass", Role.USER);

        this.testRepository.create(testPlayer);

        playerRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        playerRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(3);
        playerRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating player with existing login")
    public void createPlayerWithExistingLoginTest() {
        Player playerWithExistingLogin = new Player("Some",
                "Surname", "admin", "123", Role.USER);

        playerRepositoryAssertion.assertThatThrownBy(() -> this.testRepository.create(playerWithExistingLogin))
                .isInstanceOf(LoginAlreadyExistsException.class).hasMessageContaining("login");
        playerRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        playerRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading player info by id")
    public void readByIdTest() {
        Player adminPlayer = new Player(1, "Maxim", "Sugrobov",
                "admin", "admin", Role.ADMIN);
        Player newPlayer = testRepository.readById(1);

        assertThat(newPlayer).usingRecursiveComparison().isEqualTo(adminPlayer);
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
        Player adminPlayer = new Player(1, "Maxim", "Sugrobov",
                "admin", "admin", Role.ADMIN);
        Player newPlayer = testRepository.readByLogin("admin");

        assertThat(newPlayer).usingRecursiveComparison().isEqualTo(adminPlayer);
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
        Player updatedPlayer = new Player("Max", "Snow",
                "testAdmin", "admin", Role.ADMIN);
        updatedPlayer.setId(this.testRepository.readByLogin("testAdmin").getId());

        this.testRepository.update(this.testRepository.readByLogin("testAdmin").getId(), updatedPlayer);

        assertThat(this.testRepository.readByLogin("testAdmin")).usingRecursiveComparison().isEqualTo(updatedPlayer);
    }

    @Test
    @DisplayName("Test for updating player with wrong parameters")
    public void updateWithWrongParametersTest() {
        Player updatedPlayer = new Player("Max", "Snow",
                "MaxSnow", "admin", Role.ADMIN);
        updatedPlayer.setId(this.testRepository.readByLogin("testAdmin").getId());

        this.testRepository.update(this.testRepository.readByLogin("testAdmin").getId(), updatedPlayer);

        assertThat(this.testRepository.readByLogin("testAdmin")).usingRecursiveComparison().isNotEqualTo(updatedPlayer);
    }

    @Test
    @DisplayName("Test for updating player by not existing id")
    public void updateByNotExistingIdTest() {
        Player updatedPlayer = new Player("Max", "Snow",
                "Max", "password", Role.ADMIN);
        assertThatThrownBy(() -> this.testRepository.update(2, updatedPlayer))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting player")
    public void deleteTest() {
        Player testPlayer = new Player("Maxim", "Sugrobov",
                "Snow", "Pass", Role.USER);
        this.testRepository.create(testPlayer);
        testPlayer.setId(this.testRepository.readByLogin("Snow").getId());

        this.testRepository.delete(testPlayer.getId());

        assertThat(this.testRepository.readAll()).hasSize(2);
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
        Player adminPlayer = new Player(1, "Maxim", "Sugrobov",
                "admin", "admin", Role.ADMIN);
        Player newPlayer = new Player("Someone", "Sometwo",
                "Somelogin", "Somepass", Role.USER);
        this.testRepository.create(newPlayer);
        this.testAdminPlayer.setId(this.testRepository.readByLogin("testAdmin").getId());
        newPlayer.setId(this.testRepository.readByLogin("Somelogin").getId());
        this.storage.add(testAdminPlayer);
        this.storage.add(newPlayer);
        this.storage.add(adminPlayer);

        List<Player> allPlayersInStorage = this.testRepository.readAll();

        playerRepositoryAssertion.assertThat(allPlayersInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        playerRepositoryAssertion.assertThat(allPlayersInStorage).hasSize(3);
        playerRepositoryAssertion.assertAll();
    }
}