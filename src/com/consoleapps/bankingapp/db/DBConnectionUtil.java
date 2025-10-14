package com.consoleapps.bankingapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    public static final String URL = "jdbc:postgresql://localhost:5432/bank_db";
    public static final String USER = "postgres";
    public static final String PASS = "231429";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}