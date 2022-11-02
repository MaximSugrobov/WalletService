package ru.msugrobov.repositories;

import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.*;
import ru.msugrobov.entities.Player;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repository for player entity
 * {@inheritDoc}
 */
public class PlayerRepository implements RepositoryInterface<Player> {

    public PlayerRepository() {}
    private static final String SELECT_ALL_PLAYERS = "SELECT * FROM players";
    private static final String SELECT_PLAYER_BY_ID = "SELECT * FROM players WHERE id=?";
    private static final String SELECT_PLAYER_BY_LOGIN = "SELECT * FROM players WHERE login=?";
    private static final String CREATE_PLAYER = "INSERT INTO players " +
            "(first_name, last_name, login, password, role)" +
            "values (?, ?, ?, ?, CAST(? AS role))";
    private static final String UPDATE_PLAYER = "UPDATE players SET first_name=?, last_name=?, password=? WHERE id=? ";
    private static final String DELETE_PLAYER_BY_ID = "DELETE FROM players WHERE id=?";

    /**
     * Read information about all players
     *
     * @return all entities in storage
     */
    public List<Player> readAll() {
        List<Player> allPlayersFromDB = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PLAYERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Player playerFromQuery = playerFromResultSet(resultSet);
                allPlayersFromDB.add(playerFromQuery);
            }
            return allPlayersFromDB;
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about player by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Player readById(Integer idNumber) {
        Player playerById;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                 playerById = playerFromResultSet(resultSet);
                 return playerById;
            } else throw new IdNotFoundException(String
                    .format("Player with id %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about player by its login
     *
     * @param login player's unique login
     * @return store entity by login if exists
     */
    public Player readByLogin(String login) {
        Player playerByLogin;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playerByLogin = playerFromResultSet(resultSet);
                return playerByLogin;
            } else throw new LoginNotFoundException(String
                    .format("Player with login %s does not exist", login));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Create new player entity
     *
     * @param player creates entity if not already exists
     */
    public void create(Player player) {
        if (Objects.equals(player.getFirstName(), "") || Objects.equals(player.getLastName(), "")
                || Objects.equals(player.getLogin(), "") || Objects.equals(player.getPassword(), "")) {
            throw new CredentialsErrorException("All fields in registration form are required, try again");
        }
        if (!existByLogin(player.getLogin())) {
            try (Connection connection = DBconnection.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(CREATE_PLAYER);
                statement.setString(1, player.getFirstName());
                statement.setString(2, player.getLastName());
                statement.setString(3, player.getLogin());
                statement.setString(4, player.getPassword());
                statement.setString(5, player.getRole().toString());
                statement.executeUpdate();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
                throw new DataBaseConnectionException("Database connection error, check properties");
            }
        } else {
            throw new LoginAlreadyExistsException(String.
                    format("Player with login %s already exists", player.getLogin()));
        }
    }

    /**
     * Update players entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param player   updated context of the entity
     */
    public void update(int idNumber, Player player) {
        Player findPlayerById = this.readById(idNumber);
        findPlayerById.updateFrom(player);
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER);
            statement.setString(1, findPlayerById.getFirstName());
            statement.setString(2, findPlayerById.getLastName());
            statement.setString(3, findPlayerById.getPassword());
            statement.setInt(4, findPlayerById.getId());
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Delete players entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        this.readById(idNumber);
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER_BY_ID);
            statement.setInt(1, idNumber);
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    private boolean existByLogin(String login) {
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    public Player playerFromResultSet(ResultSet resultSet) throws SQLException {
        Player playerFromResultSet;
        String roleFromResultSet = resultSet.getString("role");
        if (!Role.contains(roleFromResultSet)) {
            throw new PlayersRoleIsNotCorrectException(String
                    .format("Player's role %s is not correct", roleFromResultSet));
        } else {
            playerFromResultSet = new Player(resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role").toUpperCase()));
            return playerFromResultSet;
        }
    }
}
