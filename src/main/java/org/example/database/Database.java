package org.example.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final String URL;
    private static final String PASSWORD;
    private static final String USER;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/resources/application.properties"));
            URL = properties.getProperty("quiz.database.url");
            USER = properties.getProperty("quiz.database.user");
            PASSWORD = properties.getProperty("quiz.database.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection() throws SQLException {



        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
