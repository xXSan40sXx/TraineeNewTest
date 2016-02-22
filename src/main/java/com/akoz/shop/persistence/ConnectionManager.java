package com.akoz.shop.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author akoz
 */
public enum ConnectionManager {
    /**
     * Singleton instance
     */
    INSTANCE;

    private static final String URL = "jdbc:mysql://localhost:3306/shop";
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
