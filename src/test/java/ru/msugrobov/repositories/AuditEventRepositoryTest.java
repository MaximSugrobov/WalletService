package ru.msugrobov.repositories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.msugrobov.entities.*;
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
    private final int playerIdForTest = this.testPlayerRepository.readByLogin("admin").getId();
    private int initEventId;
    private final SoftAssertions auditEventRepositoryAssertion = new SoftAssertions();
    private final LocalDateTime testDate = LocalDateTime.of(2022, 4, 28,
            12, 30, 30);

    @BeforeEach
    void initEach() {
        this.initEvent = new AuditEvent(playerIdForTest, "someAction", testDate, ActionResult.SUCCESS);
        this.testRepository.create(initEvent);
        this.initEventId = this.testRepository.readAllEventsByPlayerId(playerIdForTest).get(0).getId();
        this.initEvent.setId(initEventId);
    }

    @AfterEach
    void cleanUpEach() {
        String DELETE_ALL_AUDIT_EVENTS = "DELETE FROM audit_events";
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statementClearAuditEvents = connection.prepareStatement(DELETE_ALL_AUDIT_EVENTS);
            statementClearAuditEvents.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test for creating event")
    public void createTest() {
        AuditEvent testEvent = new AuditEvent(2, playerIdForTest, "anotherAction",
                testDate, ActionResult.SUCCESS);
        this.testRepository.create(testEvent);
        auditEventRepositoryAssertion.assertThat(this.testRepository).isNotNull();
        auditEventRepositoryAssertion.assertThat(this.testRepository.readAll()).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading event info by id")
    public void readByIdTest() {
        AuditEvent testEvent = this.testRepository.readById(initEventId);
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
        AuditEvent testEvent = new AuditEvent(playerIdForTest, "anotherAction",
                testDate, ActionResult.FAIL);
        this.testRepository.create(testEvent);
        testEvent.setId(this.testRepository.readAllEventsByPlayerId(playerIdForTest).get(1).getId());
        this.storage.add(initEvent);
        this.storage.add(testEvent);

        List<AuditEvent> allEventsByPlayerId = this.testRepository.readAllEventsByPlayerId(playerIdForTest);

        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading all events from storage")
    public void readAllTest() {
        AuditEvent testEvent = new AuditEvent(playerIdForTest, "anotherAction",
                testDate, ActionResult.FAIL);
        this.testRepository.create(testEvent);
        testEvent.setId(this.testRepository.readAllEventsByPlayerId(playerIdForTest).get(1).getId());
        this.storage.add(initEvent);
        this.storage.add(testEvent);

        List<AuditEvent> allEventsInStorage = this.testRepository.readAll();

        auditEventRepositoryAssertion.assertThat(allEventsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsInStorage).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }
}
