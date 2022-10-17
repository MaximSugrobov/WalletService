package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AuditEventRepositoryTest {

    private final List<AuditEvent> storage = new ArrayList<>();
    private final PlayerRepository testPlayerRepository = new PlayerRepository();
    private final AuditEventRepository testRepository = new AuditEventRepository();
    private AuditEvent initEvent;
    private Player initPlayer;
    private final SoftAssertions auditEventRepositoryAssertion = new SoftAssertions();
    private final LocalDateTime testDate = LocalDateTime.of(2022, 4, 28,
            12, 30, 30);

    @BeforeEach
    void initEach() {
        this.initPlayer = new Player(1, "Max",
                "Snow", "Snow", "Pass", Role.ADMIN);
        this.testPlayerRepository.create(initPlayer);
        this.initEvent = new AuditEvent(1, initPlayer.getId(), "someAction", testDate, ActionResult.SUCCESS);
        this.testRepository.create(initEvent);
    }

    @AfterEach
    void cleanUpEach() {
        String DELETE_ALL_AUDIT_EVENTS = "DELETE FROM audit_events";
        String DELETE_ALL_PLAYERS = "DELETE FROM players";
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statementClearAuditEvents = connection.prepareStatement(DELETE_ALL_AUDIT_EVENTS);
            statementClearAuditEvents.executeUpdate();
            PreparedStatement statementClearPlayers = connection.prepareStatement(DELETE_ALL_PLAYERS);
            statementClearPlayers.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test for creating event")
    public void createTest() {
        AuditEvent testEvent = new AuditEvent(2, 1, "anotherAction", testDate, ActionResult.SUCCESS);
        this.testRepository.create(testEvent);
        auditEventRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        auditEventRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for creating event with existing id")
    public void createEventWithExistingIdTest() {
        AuditEvent testEventWithExistingId = new AuditEvent(1, initPlayer.getId(), "action",
                testDate, ActionResult.SUCCESS);
        assertThatThrownBy(() -> this.testRepository.create(testEventWithExistingId))
                .isInstanceOf(IdAlreadyExistsException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading event info by id")
    public void readByIdTest() {
        AuditEvent testEvent = this.testRepository.readById(1);
        assertThat(testEvent).usingRecursiveComparison().isEqualTo(initEvent);
    }

    @Test
    @DisplayName("Test for reading event info by not existing id")
    public void readByNotExistingIdTest() {
        assertThatThrownBy(() -> this.testRepository.readById(2))
                .isInstanceOf(IdNotFoundException.class).hasMessageContaining("id");
    }

    @Test
    @DisplayName("Test for reading all events by player id")
    public void readAllEventsByPlayerIdTest() {
        AuditEvent testEvent = new AuditEvent(2, initPlayer.getId(), "anotherAction",
                testDate, ActionResult.FAIL);
        this.testRepository.create(testEvent);
        this.storage.add(initEvent);
        this.storage.add(testEvent);

        List<AuditEvent> allEventsByPlayerId = this.testRepository.readAllEventsByPlayerId(initPlayer.getId());

        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading all events from storage")
    public void readAllTest() {
        AuditEvent testEvent = new AuditEvent(2, initPlayer.getId(), "anotherAction",
                testDate, ActionResult.FAIL);
        this.testRepository.create(testEvent);
        this.storage.add(initEvent);
        this.storage.add(testEvent);

        List<AuditEvent> allEventsInStorage = this.testRepository.readAll();

        auditEventRepositoryAssertion.assertThat(allEventsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsInStorage).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }
}
