package ru.msugrobov.repositories;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Implements methods for reading properties and establish connection to PSQL DB
 */
public class DBconnection {

    static final FileReader fileReader;
    static final Properties properties = new Properties();

    static {
        try {
            fileReader = new FileReader("src/main/resources/liquibase.properties");
            properties.load(fileReader);
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String url = properties.getProperty("url");
    private static final String userName = properties.getProperty("username");
    private static final String password = properties.getProperty("password");

    /**
     * Establishes connection to PSQL DB
     *
     * @return connection to DB
     */
    public static Connection getConnection() throws IOException, SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
