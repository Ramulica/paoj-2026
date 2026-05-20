package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                // driver furnizat prin classpath
            }
        }
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getResourceAsStream("/com/pao/laboratory12/resources/db.properties")) {
            if (is == null) {
                throw new IOException("Nu gasesc db.properties (classpath: com/pao/laboratory12/resources/db.properties)");
            }
            props.load(is);
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user", "");
        String pass = props.getProperty("db.password", "");
        connection = DriverManager.getConnection(
                url,
                user.isEmpty() ? null : user,
                pass.isEmpty() ? null : pass);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException ignored) {
            // nu există pe MySQL
        }
    }

    public static synchronized DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
