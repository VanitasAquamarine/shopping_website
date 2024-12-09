package com.example.dao;

import com.example.model.Log;
import com.example.model.User;
import com.example.model.Product;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    private UserDAO userDAO = new UserDAO();  // 引入 UserDAO
    private ProductDAO productDAO = new ProductDAO();  // 引入 ProductDAO

    // 获取所有日志
    public List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<>();
        String query = "SELECT * FROM logs";  // 获取所有日志的查询语句

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setProductId(rs.getInt("product_id"));
                log.setAction(rs.getString("action"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                log.setPaymentSuccessful(rs.getBoolean("is_payment_successful"));

                // 获取关联的用户信息
                int userId = rs.getInt("user_id");
                User user = userDAO.getUserById(userId);  // 根据 userId 获取用户信息
                log.setUser(user);

                // 获取关联的商品信息
                int productId = rs.getInt("product_id");
                Product product = productDAO.getProductById(productId);  // 根据 productId 获取商品信息
                log.setProduct(product);

                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    // 根据 ID 获取某个日志
    public Log getLogById(int id) {
        Log log = null;
        String query = "SELECT * FROM logs WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    log = new Log();
                    log.setId(rs.getInt("id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setProductId(rs.getInt("product_id"));
                    log.setAction(rs.getString("action"));
                    log.setTimestamp(rs.getTimestamp("timestamp"));
                    log.setPaymentSuccessful(rs.getBoolean("is_payment_successful"));

                    // 获取关联的用户信息
                    int userId = rs.getInt("user_id");
                    User user = userDAO.getUserById(userId);  // 根据 userId 获取用户信息
                    log.setUser(user);

                    // 获取关联的商品信息
                    int productId = rs.getInt("product_id");
                    Product product = productDAO.getProductById(productId);  // 根据 productId 获取商品信息
                    log.setProduct(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return log;
    }

    // 添加日志
    public boolean addLog(Log log) {
        String query = "INSERT INTO logs (user_id, product_id, action, is_payment_successful) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, log.getUserId());
            stmt.setInt(2, log.getProductId());
            stmt.setString(3, log.getAction());
            stmt.setBoolean(4, log.isPaymentSuccessful());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除日志
    public boolean deleteLog(int id) {
        String query = "DELETE FROM logs WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 根据用户ID获取日志
    public List<Log> getLogsByUserId(int userId) {
        List<Log> logs = new ArrayList<>();
        String query = "SELECT * FROM logs WHERE user_id = ?";  // 根据 userId 获取所有日志

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Log log = new Log();
                    log.setId(rs.getInt("id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setProductId(rs.getInt("product_id"));
                    log.setAction(rs.getString("action"));
                    log.setTimestamp(rs.getTimestamp("timestamp"));
                    log.setPaymentSuccessful(rs.getBoolean("is_payment_successful"));

                    // 获取关联的用户信息
                    User user = userDAO.getUserById(userId);
                    log.setUser(user);

                    // 获取关联的商品信息
                    Product product = productDAO.getProductById(rs.getInt("product_id"));
                    log.setProduct(product);

                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
