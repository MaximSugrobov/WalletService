package ru.msugrobov.repositories;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Implements methods for reading properties and establish connection to PSQL DB
 */
public class DBconnection {

    static final FileReader fileReader;

    static {
        try {
            fileReader = new FileReader("src/main/resources/liquibase.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static final Properties properties = new Properties();

    /**
     * Establishes connection to PSQL DB
     *
     * @return connection to DB
     */
    public static Connection getConnection() throws IOException, SQLException {
        properties.load(fileReader);
        String url = properties.getProperty("url");
        String userName = properties.getProperty("username");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, userName, password);
    }
}
