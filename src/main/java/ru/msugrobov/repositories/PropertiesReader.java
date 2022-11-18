package ru.msugrobov.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Implements method for reading properties
 */
public class PropertiesReader {

    static final Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = PropertiesReader.class
                    .getResourceAsStream("/application.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String url = properties.getProperty("url");
    public static final String userName = properties.getProperty("username");
    public static final String passwordProp = properties.getProperty("password");
    public static final String tokenValidityDuration = properties.getProperty("tokenValidityDuration");
}
