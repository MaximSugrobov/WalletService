package ru.msugrobov.repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.msugrobov.repositories.PropertiesReader.*;

/**
 * Implements method for establish connection to PSQL DB
 */
public class DBconnection {

    /**
     * Establishes connection to PSQL DB
     *
     * @return connection to DB
     */
    public static Connection getConnection() throws IOException, SQLException {
        return DriverManager.getConnection(url, userName, passwordProp);
    }
}
