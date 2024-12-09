package com.example.dao;

import com.example.model.User;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 添加用户
    public boolean addUser(User user) {
        // 不需要传入id，数据库会自动生成
        String query = "INSERT INTO users (username, password, email, status, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 设置用户信息，role和status使用默认值
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, "active");  // 默认status为"active"
            stmt.setString(5, "customer");  // 默认role为"customer"

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 根据用户名查询用户
    public User getUserByUsername(String username) {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setRegistrationDate(rs.getTimestamp("registration_date"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 根据用户名和密码查询用户（用于登录验证）
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setRegistrationDate(rs.getTimestamp("registration_date"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 根据ID获取用户
    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setRegistrationDate(rs.getTimestamp("registration_date"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 更新用户信息
    public boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, email = ?, status = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getStatus());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除用户
 // 删除用户
    public boolean deleteUser(int id) {
        String deleteOrderItemsSql = "DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE user_id = ?)";
        String deleteOrdersSql = "DELETE FROM orders WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection()) {
            conn.setAutoCommit(false); // 开启事务

            try (PreparedStatement deleteOrderItemsStmt = conn.prepareStatement(deleteOrderItemsSql);
                 PreparedStatement deleteOrdersStmt = conn.prepareStatement(deleteOrdersSql);
                 PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql)) {

                // 删除用户关联的订单项
                deleteOrderItemsStmt.setInt(1, id);
                deleteOrderItemsStmt.executeUpdate();

                // 删除用户关联的订单
                deleteOrdersStmt.setInt(1, id);
                deleteOrdersStmt.executeUpdate();

                // 删除用户
                deleteUserStmt.setInt(1, id);
                int rowsAffected = deleteUserStmt.executeUpdate();

                conn.commit(); // 提交事务
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback(); // 发生异常时回滚事务
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    // 检查用户名是否存在
    public boolean checkUsernameExists(String username) {
        String query = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 检查邮箱是否存在
    public boolean checkEmailExists(String email) {
        String query = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获取所有用户
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRegistrationDate(rs.getTimestamp("registration_date"));
                user.setStatus(rs.getString("status"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e)            {
            e.printStackTrace();
        }
        return users;
    }

    // 验证用户登录
    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // 如果找到匹配的记录，返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

