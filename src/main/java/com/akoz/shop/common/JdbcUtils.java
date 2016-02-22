package com.akoz.shop.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author akoz
 */
public class JdbcUtils {

    private JdbcUtils() {}

    public static Integer getGeneratedKey(Statement statement) throws SQLException {
        ResultSet generatedKeys = statement.getGeneratedKeys();

        if (!generatedKeys.next()) {
            return null;
        }

        return generatedKeys.getInt(1);
    }
}
