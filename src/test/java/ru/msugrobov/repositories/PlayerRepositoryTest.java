package ru.msugrobov.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.Player;
import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.LoginNotFoundException;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

public class PlayerRepositoryTest {

    private final List<Player> storage = new ArrayList<>();
    private final PlayerRepository testRepository = new PlayerRepository(storage);
    private Player initPlayer;

    @BeforeEach
    void initEach() {
        this.initPlayer = new Player(1, "Maxim", "Sugrobov",
                "Max", "Pass", Role.ADMIN);
        this.testRepository.create(initPlayer);
    }

    @AfterEach
    void cleanUpEach() {
        this.storage.clear();
    }

    @Test
    @DisplayName("Test for creating new player")
    void createPlayerTest() {
        Player player = new Player(2, "Maxim", "Sugrobov",
                "Snow", "Pass", Role.ADMIN);
        testRepository.create(player);
        assertThat(storage).isNotNull().contains(player);
    }

    @Test
    @DisplayName("Test for creating player with existing login")
    void createPlayerWithExistingLoginTest() {
        Player playerWithExistingLogin = new Player(2, "Some",
                "Surname", "Max", "123", Role.USER);
        assertThatThrownBy(() -> testRepository.create(playerWithExistingLogin))
                .isInstanceOf(LoginNotFoundException.class).hasMessageContaining("login");
        assertThat(storage).hasSize(1);
    }

    @Test
    @DisplayName("Test for creating player with existing Id")
    void createPlayerWithExistingIdTest() {
        Player playerWithExistingId = new Player(1, "Patrik",
                "Starfish", "Patrik", "678", Role.USER);
        assertThatThrownBy(() -> testRepository.create(playerWithExistingId))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("id");
        assertThat(storage).hasSize(1);
    }

    @Test
    @DisplayName("Test for reading player info by id")
    void readByIdTest() {
        Player newPlayer = testRepository.readById(1);
        assertThat(newPlayer).usingRecursiveComparison().isEqualTo(this.initPlayer);
    }

    @Test
    @DisplayName("Test for reading player info by not existing id")
    void readByNotExistingIdTest() {
        assertThatThrownBy(() -> testRepository.readById(2))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for updating player")
    void updateTest() {
        Player updatedPlayer = new Player(1, "Max", "Snow",
                "Max", "password", Role.ADMIN);
        testRepository.update(1, updatedPlayer);
        assertThat(this.initPlayer).usingRecursiveComparison().isEqualTo(updatedPlayer);
    }

    @Test
    @DisplayName("Test for updating player by not existing id")
    void updateByNotExistingIdTest() {
        Player updatedPlayer = new Player(1, "Max", "Snow",
                "Max", "password", Role.ADMIN);
        assertThatThrownBy(() -> testRepository.update(2, updatedPlayer))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting player")
    void deleteTest() {
        testRepository.delete(1);
        assertThat(storage).hasSize(0);
    }

    @Test
    @DisplayName("Test for deleting player by not existing id")
    void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> testRepository.delete(2))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("id");
    }
}