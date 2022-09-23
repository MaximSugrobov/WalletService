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

public class AuditEventRepositoryTest {

    private final List<AuditEvent> storage = new ArrayList<>();
    private final AuditEventRepository testRepository = new AuditEventRepository(storage);
    private AuditEvent initEvent;
    private final Player initPlayer = new Player(1, "Max",
            "Snow", "Snow", "Pass", Role.ADMIN);
    private final SoftAssertions auditEventRepositoryAssertion = new SoftAssertions();
    private final LocalDateTime testDate = LocalDateTime.of(2022, 4, 28, 12, 30);

    @BeforeEach
    void initEach() {
        this.initEvent = new AuditEvent(1, initPlayer.getId(), "someAction", testDate, ActionResult.SUCCESS);
        this.testRepository.create(initEvent);
    }

    @AfterEach
    void cleanUpEach() {
        this.storage.clear();
    }

    @Test
    @DisplayName("Test for creating event")
    public void createTest() {
        AuditEvent testEvent = new AuditEvent(2, 2, "anotherAction", testDate, ActionResult.SUCCESS);
        this.testRepository.create(testEvent);
        auditEventRepositoryAssertion.assertThat(this.storage).isNotNull().contains(testEvent);
        auditEventRepositoryAssertion.assertThat(this.storage).hasSize(2);
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
        List<AuditEvent> allEventsByPlayerId = this.testRepository.readAllEventsByPlayerId(initPlayer.getId());
        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsByPlayerId).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }

    @Test
    @DisplayName("Test for reading all commands from storage")
    public void readAllTest() {
        AuditEvent testEvent = new AuditEvent(2, initPlayer.getId(), "anotherAction",
                testDate, ActionResult.FAIL);
        this.testRepository.create(testEvent);
        List<AuditEvent> allEventsInStorage = this.testRepository.readAll();
        auditEventRepositoryAssertion.assertThat(allEventsInStorage)
                .usingRecursiveFieldByFieldElementComparator().isEqualTo(this.storage);
        auditEventRepositoryAssertion.assertThat(allEventsInStorage).hasSize(2);
        auditEventRepositoryAssertion.assertAll();
    }
}
