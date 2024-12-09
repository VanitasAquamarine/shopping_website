package com.example.util;

import java.sql.*;

public class DatabaseUtils {

	private static final String URL = "jdbc:mysql://localhost:3306/shopping_website?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8";
    private static final String USER = "root";  // 数据库用户名
    private static final String PASSWORD = "root";  // 数据库密码

    static {
        try {
            // 加载 MySQL 驱动程序（推荐在初始化时加载）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found. Please add the JDBC driver to your project.");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return Connection 对象
     * @throws SQLException 如果无法连接到数据库
     */
    public static Connection getConnection() throws SQLException {
    	System.out.println("Attempting to establish database connection...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭连接
     *
     * @param connection 要关闭的 Connection 对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 PreparedStatement
     *
     * @param statement 要关闭的 PreparedStatement 对象
     */
    public static void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 ResultSet
     *
     * @param resultSet 要关闭的 ResultSet 对象
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

