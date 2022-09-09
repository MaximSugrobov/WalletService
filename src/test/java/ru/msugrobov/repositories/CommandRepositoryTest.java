package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommandRepositoryTest {

    private final List<Command> storage = new ArrayList<>();
    private final CommandRepository testRepository = new CommandRepository(storage);
    private Command initCommand;
    private final Player initPlayer = new Player(1, "Max",
            "Snow", "Snow", "Pass", Role.ADMIN);
    private final SoftAssertions transactionRepositoryAssertion = new SoftAssertions();
    private final LocalDateTime testDate = LocalDateTime.of(2022, 4, 28, 12, 30);

    @BeforeEach
    void initEach() {
        this.initCommand = new Command(1, initPlayer.getId(), "someAction", testDate);
        this.testRepository.create(initCommand);
    }

    @AfterEach
    void cleanUpEach() {
        this.storage.clear();
    }

    @Test
    @DisplayName("Test for creating command")
    public void createTest() {
        Command testCommand = new Command(2, 2, "anotherAction", testDate);
        this.testRepository.create(testCommand);
        transactionRepositoryAssertion.assertThat(this.storage).isNotNull().contains(testCommand);
        transactionRepositoryAssertion.assertThat(this.storage).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating command with existing id")
    public void createCommandWithExistingIdTest() {
        Command testCommandWithExistingId = new Command(1, initPlayer.getId(), "action", testDate);
        assertThatThrownBy(() -> this.testRepository.create(testCommandWithExistingId))
                .isInstanceOf(IdAlreadyExistsException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading command info by id")
    public void readByIdTest() {
        Command testCommand = this.testRepository.readById(1);
        assertThat(testCommand).usingRecursiveComparison().isEqualTo(initCommand);
    }

    @Test
    @DisplayName("Test for reading command info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all commands by player id")
    public void readAllCommandsByPlayerIdTest() {
        Command testCommand = new Command(2, initPlayer.getId(), "anotherAction", testDate);
        this.testRepository.create(testCommand);
        List<Command> allCommandsByPlayerId = this.testRepository.readAllCommandsByPlayerId(initPlayer);
        transactionRepositoryAssertion.assertThat(allCommandsByPlayerId)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(allCommandsByPlayerId).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for updating command")
    public void updateTest() {
        Command testCommand = new Command(1, initPlayer.getId(),
                "updatedAction", LocalDateTime.of(2022, 4, 30, 12, 40));
        this.testRepository.update(1, testCommand);
        assertThat(testCommand).usingRecursiveComparison().isEqualTo(initCommand);
    }

    @Test
    @DisplayName("Test for updating command by not existing id")
    public void updateByNotExistingIdTest() {
        Command testCommand = new Command(1, initPlayer.getId(),
                "updatedAction", LocalDateTime.of(2022, 4, 30, 12, 40));
        assertThatThrownBy(() -> this.testRepository.update(2, testCommand))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for deleting command")
    public void deleteTest() {
        this.testRepository.delete(1);
        assertThat(this.storage).hasSize(0);
    }

    @Test
    @DisplayName("Test for deleting command by not existing id")
    public void deleteByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.delete(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all commands from storage")
    public void readAllTest() {
        Command testCommand = new Command(2, initPlayer.getId(), "anotherAction", testDate);
        this.testRepository.create(testCommand);
        List<Command> allCommandsInStorage = this.testRepository.readAll();
        transactionRepositoryAssertion.assertThat(allCommandsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        transactionRepositoryAssertion.assertThat(allCommandsInStorage).hasSize(2);
        transactionRepositoryAssertion.assertAll();
    }
}
