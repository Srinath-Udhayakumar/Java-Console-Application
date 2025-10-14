package com.consoleapps.bankingapp.dao;

import com.consoleapps.bankingapp.db.DBConnectionUtil;
import java.sql.*;
import java.util.*;

public class TransactionDAO {
    public static void insertTransaction(String userName, int amount, String type) throws SQLException {
        String sql = "INSERT INTO transaction (user_name, amount, transaction_type) " +
                "VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setInt(2, amount);
            ps.setString(3, type);
            ps.executeUpdate();
        }
    }

    public static List<String> getTransactionsByUserName(String userName) {
        List<String> transactions = new ArrayList<>();
        String sql = "SELECT amount, transaction_type, transaction_time" +
                " FROM transaction WHERE user_name = ? ORDER BY transaction_time DESC";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("transaction_type");
                    int amount = rs.getInt("amount");
                    Timestamp time = rs.getTimestamp("transaction_time");

                    transactions.add(String.format("%s: %s %d at %s",
                            time.toLocalDateTime().toLocalDate(),
                            type,
                            amount,
                            time.toLocalDateTime().toLocalTime().withNano(0)));
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error retrieving transactions: " + e.getMessage());
        }
        return transactions;
    }
}