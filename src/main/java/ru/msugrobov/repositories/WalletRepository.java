package ru.msugrobov.repositories;

import ru.msugrobov.entities.Wallet;
import ru.msugrobov.exceptions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for wallet entity
 * {@inheritDoc}
 */
public class WalletRepository implements RepositoryInterface<Wallet> {

    public WalletRepository() {}
    private static final String SELECT_ALL_WALLETS = "SELECT * FROM wallets";
    private static final String SELECT_WALLET_BY_ID = "SELECT * FROM wallets WHERE id=?";
    private static final String SELECT_WALLET_BY_PLAYER_ID = "SELECT * FROM wallets WHERE player_id=?";
    private static final String CREATE_WALLET = "INSERT INTO wallets (player_id, balance) values (?, ?)";
    private static final String UPDATE_WALLET = "UPDATE wallets SET balance=? WHERE id=?";
    private static final String DELETE_WALLET_BY_ID = "DELETE FROM wallets WHERE id=?";

    /**
     * Read all wallets
     *
     * @return all entities in storage
     */
    public List<Wallet> readAll() {
        List<Wallet> allWalletsFromDB = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_WALLETS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Wallet walletFromQuery = walletFromResultSet(resultSet);
                allWalletsFromDB.add(walletFromQuery);
            }
            return allWalletsFromDB;
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about wallet by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Wallet readById(Integer idNumber) {
        Wallet walletById;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_WALLET_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                walletById = walletFromResultSet(resultSet);
                return walletById;
            } else throw new IdNotFoundException(String
                    .format("Wallet with id %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about wallet by player id
     *
     * @param idNumber identifier of the player
     * @return wallet by player id
     */
    public Wallet readByPlayerId(Integer idNumber) {
        Wallet walletByPlayerId;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_WALLET_BY_PLAYER_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                walletByPlayerId = walletFromResultSet(resultSet);
                return walletByPlayerId;
            } else throw new IdNotFoundException(String
                    .format("Wallet with playerId %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Create new wallet entity
     *
     * @param wallet creates entity if not already exists
     */
    public void create(Wallet wallet) {
        if (!existByPlayerId(wallet)) {
            try (Connection connection = DBconnection.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(CREATE_WALLET);
                statement.setInt(1, wallet.getPlayerId());
                statement.setBigDecimal(2, wallet.getBalance());
                statement.executeUpdate();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
                throw new DataBaseConnectionException("Database connection error, check properties");
            }
        } else if (existByPlayerId(wallet)) {
            throw new PlayerIdAlreadyExistsException(String
                    .format("Wallet with playerId %s already exists", wallet.getPlayerId()));
        }
    }

    /**
     * Update wallet entity
     *
     * @param idNumber identifier of an entity to be updated
     * @param wallet   updated context of the entity
     */
    public void update(int idNumber, Wallet wallet) {
        Wallet findWalletById = this.readById(idNumber);
        findWalletById.setBalance(wallet.getBalance());
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_WALLET);
            statement.setBigDecimal(1, findWalletById.getBalance());
            statement.setInt(2, findWalletById.getId());
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Delete wallet entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        this.readById(idNumber);
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_WALLET_BY_ID);
            statement.setInt(1, idNumber);
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    private boolean existByPlayerId(Wallet wallet) {
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_WALLET_BY_PLAYER_ID);
            statement.setInt(1, wallet.getPlayerId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    private Wallet walletFromResultSet(ResultSet resultSet) throws SQLException {
        Wallet walletFromResultSet;
        walletFromResultSet = new Wallet(resultSet.getInt("id"),
                    resultSet.getInt("player_id"),
                    resultSet.getBigDecimal("balance"));
        return walletFromResultSet;
    }
}
