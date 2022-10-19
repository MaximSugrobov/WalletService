package ru.msugrobov.repositories;

import ru.msugrobov.entities.*;
import ru.msugrobov.exceptions.DataBaseConnectionException;
import ru.msugrobov.exceptions.IdNotFoundException;
import ru.msugrobov.exceptions.TransactionTypeIsNotCorrect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for transaction entity
 * {@inheritDoc}
 */
public class TransactionRepository implements RepositoryInterface<Transaction> {

    public TransactionRepository() {}
    private static final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM transactions";
    private static final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM transactions WHERE id=?";
    private static final String SELECT_TRANSACTIONS_BY_WALLET_ID = "SELECT * FROM transactions WHERE wallet_id=?";
    private static final String CREATE_TRANSACTION = "INSERT INTO transactions (wallet_id, type, value)" +
            "values (?, CAST(? AS transaction_type), ?)";
    private static final String UPDATE_TRANSACTION = "UPDATE transactions SET value=? WHERE id=?";
    private static final String DELETE_TRANSACTION_BY_ID = "DELETE FROM transactions WHERE id=?";

    /**
     * Read all transactions
     *
     * @return all entities in storage
     */
    public List<Transaction> readAll() {
        List<Transaction> allTransactionsFromDB = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transactionFromQuery = transactionFromResultSet(resultSet);
                allTransactionsFromDB.add(transactionFromQuery);
            }
            return allTransactionsFromDB;
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read information about transaction by its id
     *
     * @param idNumber identifier
     * @return stored entity by id if exists
     */
    public Transaction readById(Integer idNumber) {
        Transaction transactionById;
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID);
            statement.setInt(1, idNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                transactionById = transactionFromResultSet(resultSet);
                return transactionById;
            } else throw new IdNotFoundException(String
                    .format("Transaction with id %s does not exist", idNumber));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Read all transactions by wallet id
     *
     * @param wallet entity
     * @return All transactions by wallet
     */
    public List<Transaction> readAllTransactionsByWalletId(Wallet wallet) {
        List<Transaction> transactionsByWalletId = new ArrayList<>();
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_WALLET_ID);
            statement.setInt(1, wallet.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    Transaction transactionFromQuery = transactionFromResultSet(resultSet);
                    transactionsByWalletId.add(transactionFromQuery);
                }
                return transactionsByWalletId;
            } else throw new IdNotFoundException(String
                    .format("Transaction with walletId %s does not exist", wallet.getId()));
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Create new transaction entity
     *
     * @param transaction creates entity if not already exists
     */
    public void create(Transaction transaction) {
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_TRANSACTION);
            statement.setInt(1, transaction.getWalletId());
            statement.setString(2, transaction.getType().toString());
            statement.setBigDecimal(3, transaction.getValue());
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Update transaction entity
     *
     * @param idNumber    identifier of an entity to be updated
     * @param transaction updated context of the entity
     */
    public void update(int idNumber, Transaction transaction) {
        Transaction findTransactionById = this.readById(idNumber);
        findTransactionById.updateFrom(transaction);
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION);
            statement.setBigDecimal(1, findTransactionById.getValue());
            statement.setInt(2, findTransactionById.getId());
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    /**
     * Delete transaction entity
     *
     * @param idNumber identifier
     */
    public void delete(int idNumber) {
        this.readById(idNumber);
        try (Connection connection = DBconnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_BY_ID);
            statement.setInt(1, idNumber);
            statement.executeUpdate();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            throw new DataBaseConnectionException("Database connection error, check properties");
        }
    }

    private Transaction transactionFromResultSet(ResultSet resultSet) throws SQLException {
        Transaction transactionFromResultSet;
        String typeFromResultSet = resultSet.getString("type");
        if (!Type.contains(typeFromResultSet)) {
            throw new TransactionTypeIsNotCorrect(String
                    .format("Transaction type %s is not correct", typeFromResultSet));
        } else {
            transactionFromResultSet = new Transaction(resultSet.getInt("id"),
                    resultSet.getInt("wallet_id"),
                    Type.valueOf(resultSet.getString("type").toUpperCase()),
                    resultSet.getBigDecimal("value"));
            return transactionFromResultSet;
        }
    }
}
