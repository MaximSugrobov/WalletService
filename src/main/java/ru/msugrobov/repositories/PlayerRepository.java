package ru.msugrobov.repositories;

import ru.msugrobov.entities.Role;
import ru.msugrobov.exceptions.IdAlreadyExistsException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.entities.Player;
import ru.msugrobov.exceptions.LoginAlreadyExistsException;
import ru.msugrobov.exceptions.LoginNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for player entity
 * {@inheritDoc}
 */
public class PlayerRepository implements RepositoryInterface<Player> {

    String url = "jdbc:postgresql://localhost:5432/WalletServiceDB";
    String login = "postgres";
    String password = "postgres";
    public PlayerRepository() {}
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    /**
     * Read information about all players
     *
     * @return all entities in storage
     */
    public List<Player> readAll() {
        String SELECT_ALL_PLAYERS = "SELECT * FROM players";
        List<Player> allPlayersFromDB = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PLAYERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Player playerFromQuery = new Player(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()));
                allPlayersFromDB.add(playerFromQuery);
            }
            return allPlayersFromDB;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return allPlayersFromDB;
    }

    /**
     * Read information about player by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Player readById(Integer idNumber) {
        String SELECT_PLAYER_BY_ID = "SELECT * FROM players WHERE id=?";
        Player playerById = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                 playerById = new Player(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()));
                 return playerById;
            } else throw new IdNotFoundException(String
                    .format("Player with id %s does not exist", idNumber));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playerById;
    }

    public Player readByLogin(String login) {
        String SELECT_PLAYER_BY_LOGIN = "SELECT * FROM players WHERE login=?";
        Player playerByLogin = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playerByLogin = new Player(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()));
                return playerByLogin;
            } else throw new LoginNotFoundException(String
                    .format("Player with login %s does not exist", login));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playerByLogin;
    }

    /**
     * Create new player entity
     *
     * @param player creates entity if not already exists
     */
    public void create(Player player) {
        String CREATE_PLAYER = "INSERT INTO players (id, first_name, last_name, login, password, role)" +
                "values (?, ?, ?, ?, ?, CAST(? AS role))";
        if (!existById(player.getId()) && !existByLogin(player.getLogin())) {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(CREATE_PLAYER);
                statement.setInt(1, player.getId());
                statement.setString(2, player.getFirstName());
                statement.setString(3, player.getLastName());
                statement.setString(4, player.getLogin());
                statement.setString(5, player.getPassword());
                statement.setString(6, player.getRole().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else if (existByLogin(player.getLogin())) {
            throw new LoginAlreadyExistsException(String.
                    format("Player with login %s already exists", player.getLogin()));
        } else if (existById(player.getId())) {
            throw new IdAlreadyExistsException(String
                    .format("Player with id %s already exists", player.getId()));
        }
    }

    /**
     * Update players entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param player   updated context of the entity
     */
    public void update(int idNumber, Player player) {
        String UPDATE_PLAYER = "UPDATE players SET id=?, first_name=?, last_name=?, password=?";
        Player findPlayerById = this.readById(idNumber);
        findPlayerById.updateFrom(player);
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER);
            statement.setInt(1, findPlayerById.getId());
            statement.setString(2, findPlayerById.getFirstName());
            statement.setString(3, findPlayerById.getLastName());
            statement.setString(4, findPlayerById.getPassword());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Delete players entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        String DELETE_PLAYER_BY_ID = "DELETE FROM players WHERE id=?";
        this.readById(idNumber);
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER_BY_ID);
            statement.setInt(1, idNumber);
            statement.executeUpdate();
        } catch (SQLException | IdNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private boolean existById(int idNumber) {
        String SELECT_PLAYER_BY_ID = "SELECT * FROM players WHERE id=?";
        Player playerById = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playerById = new Player(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playerById != null;
    }

    private boolean existByLogin(String login) {
        String SELECT_PLAYER_BY_LOGIN = "SELECT * FROM players WHERE login=?";
        Player playerByLogin = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                playerByLogin = new Player(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playerByLogin != null;
    }
}
