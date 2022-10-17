package ru.msugrobov.repositories;

import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for auditEvent entity
 */
public class AuditEventRepository {

    public AuditEventRepository() {}
    private static final String SELECT_ALL_EVENTS = "SELECT * FROM audit_events";
    private static final String SELECT_EVENT_BY_ID = "SELECT * FROM audit_events WHERE id=?";
    private static final String SELECT_EVENTS_BY_PLAYER_ID = "SELECT * FROM audit_events WHERE player_id=?";
    private static final String CREATE_EVENT = "INSERT INTO audit_events " +
            "(id, player_id, action, date_time, action_result)" +
            "values (?, ?, ?, ?, CAST(? AS action_result))";

    /**
     * Read all events
     *
     * @return all entities in storage
     */
    public List<AuditEvent> readAll() {
        List<AuditEvent> allEventsFromDB = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_EVENTS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AuditEvent eventFromQuery = auditEventFromResultSet(resultSet);
                allEventsFromDB.add(eventFromQuery);
            }
            return allEventsFromDB;
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about event if exists
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public AuditEvent readById(Integer idNumber) {
        AuditEvent eventById;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_EVENT_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                eventById = auditEventFromResultSet(resultSet);
                return eventById;
            } else throw new IdNotFoundException(String
                    .format("Event with id %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read all events by player id
     *
     * @param idNumber identifier of the player
     * @return all commands by player
     */
    public List<AuditEvent> readAllEventsByPlayerId(Integer idNumber) {
        List<AuditEvent> eventsByPlayerId = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_EVENTS_BY_PLAYER_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    AuditEvent eventFromQuery = auditEventFromResultSet(resultSet);
                    eventsByPlayerId.add(eventFromQuery);
                }
                return eventsByPlayerId;
            } else throw new IdNotFoundException(String
                    .format("Event with playerId %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Create new auditEvent entity
     *
     * @param auditEvent creates entity if not already exists
     */
    public void create(AuditEvent auditEvent) {
        if (!existById(auditEvent.getId())) {
            try (Connection connection = DBconnection.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(CREATE_EVENT);
                statement.setInt(1, auditEvent.getId());
                statement.setInt(2, auditEvent.getPlayerId());
                statement.setString(3, auditEvent.getAction());
                statement.setTimestamp(4, Timestamp.valueOf(auditEvent.getDateTime()));
                statement.setString(5, auditEvent.getActionResult().toString());
                statement.executeUpdate();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
                throw new DataBaseConnectionException("Database connection error, check properties");
            }
        } else {
            throw new IdAlreadyExistsException(String.format("Event with id %s already exists", auditEvent.getId()));
        }
    }

    private boolean existById(int idNumber) {
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_EVENT_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    private AuditEvent auditEventFromResultSet(ResultSet resultSet) throws SQLException {
        AuditEvent auditEventFromResultSet;
        String actionResultFromResultSet = resultSet.getString("action_result");
        if (!ActionResult.contains(actionResultFromResultSet)) {
            throw new ActionResultIsNotCorrect(String
                    .format("Action result %s is not correct", actionResultFromResultSet));
        } else {
            auditEventFromResultSet = new AuditEvent(resultSet.getInt("id"),
                    resultSet.getInt("player_id"),
                    resultSet.getString("action"),
                    resultSet.getTimestamp("date_time").toLocalDateTime(),
                    ActionResult.valueOf(resultSet.getString("action_result").toUpperCase()));
            return auditEventFromResultSet;
        }
    }
}
