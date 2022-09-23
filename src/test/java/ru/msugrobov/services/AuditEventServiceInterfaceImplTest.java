package ru.msugrobov.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import ru.msugrobov.entities.ActionResult;
import ru.msugrobov.entities.AuditEvent;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.repositories.AuditEventRepository;
import ru.msugrobov.services.impl.AuditEventServiceInterfaceImpl;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class AuditEventServiceInterfaceImplTest {

    private final AuditEvent testEvent = new AuditEvent(1, 1, "testAction",
            LocalDateTime.of(1990, 10, 10, 20, 20), ActionResult.SUCCESS);
    private final AuditEventRepository auditEventRepositoryMock = mock(AuditEventRepository.class);
    private final AuditEventServiceInterfaceImpl testAuditEventService = new AuditEventServiceInterfaceImpl
            (auditEventRepositoryMock);
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
    @DisplayName("Test for creating event via auditEvent service")
    public void createTest() {
        doThrow(IdAlreadyExistsException.class).when(auditEventRepositoryMock).create(testEvent);
        doNothing().when(auditEventRepositoryMock).create(testEvent);

        testAuditEventService.createEvent(1, 1, "testAction",
                LocalDateTime.of(1990, 10, 10, 20, 20), ActionResult.SUCCESS);

        verify(auditEventRepositoryMock).create(testEvent);
    }

    @Test
    @DisplayName("Test for finding all events via auditEvent service")
    public void findAllEventsTest() {
        doReturn(auditEventRepositoryMock.readAll()).when(auditEventRepositoryMock).readAll();

        testAuditEventService.findAllEvents();

        verify(auditEventRepositoryMock, times(2)).readAll();
    }

    @Test
    @DisplayName("Test for finding event by id via auditEvent service")
    public void findByIdTest() {
        doThrow(IdNotFoundException.class).when(auditEventRepositoryMock).readById(2);

        testAuditEventService.findById(1);

        verify(auditEventRepositoryMock).readById(1);
    }

    @Test
    @DisplayName("Test for finding all events by player id via auditEvent service")
    public void findByPlayerIdTest() {
        doThrow(IdNotFoundException.class).when(auditEventRepositoryMock).readById(2);

        testAuditEventService.findAllEventsByPlayerId(1);

        verify(auditEventRepositoryMock).readAllEventsByPlayerId(1);
    }
}
