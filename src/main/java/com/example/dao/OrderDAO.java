package com.example.dao;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {

    // 获取所有订单
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";  // 获取所有订单的查询语句

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setUpdatedAt(rs.getTimestamp("updated_at"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setShippingAddress(rs.getString("shipping_address"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // 根据订单ID获取订单
    public Order getOrderById(int id) {
        Order order = null;
        String query = "SELECT * FROM orders WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    // 根据用户ID获取所有订单
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setShippingAddress(rs.getString("shipping_address"));

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // 添加订单
    public boolean addOrder(Order order) {
        String query = "INSERT INTO orders (user_id, total_price, status, payment_method, shipping_address) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setDouble(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getStatus());
            preparedStatement.setString(4, order.getPaymentMethod());
            preparedStatement.setString(5, order.getShippingAddress());

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                // 获取自动生成的订单 ID
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));  // 设置生成的订单 ID
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 更新订单状态
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public Map<Order, List<OrderItem>> getOrderDetailsById(int orderId) {
        Order order = getOrderById(orderId); // 调用现有方法获取订单信息
        Map<Order, List<OrderItem>> orderDetails = new HashMap<>();

        if (order != null) {
            OrderItemDAO orderItemDAO = new OrderItemDAO();
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId); // 获取订单项信息
            orderDetails.put(order, orderItems);
        }

        return orderDetails;
    }
 // 获取最近插入订单的 ID
    public int getLastInsertedOrderId() {
        String query = "SELECT LAST_INSERT_ID()"; // MySQL 内置函数
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1); // 获取第一列的值
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 如果失败，返回 -1
    }
    private static final String UPDATE_ORDER_SQL = "UPDATE orders SET payment_method = ?, shipping_address = ? WHERE id = ?";

    // 更新订单的支付方式和收货地址
    public boolean updateOrder(Order order) {
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_SQL)) {

            preparedStatement.setString(1, order.getPaymentMethod());
            preparedStatement.setString(2, order.getShippingAddress());
            preparedStatement.setInt(3, order.getId());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 // 删除订单及其关联的数据
    public boolean deleteOrder(int orderId) {
        String deleteOrderItemsSql = "DELETE FROM order_items WHERE order_id = ?";
        String deleteOrderSql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection()) {
            conn.setAutoCommit(false); // 开启事务

            try (PreparedStatement deleteOrderItemsStmt = conn.prepareStatement(deleteOrderItemsSql);
                 PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSql)) {

                // 删除关联的订单项
                deleteOrderItemsStmt.setInt(1, orderId);
                deleteOrderItemsStmt.executeUpdate();

                // 删除订单
                deleteOrderStmt.setInt(1, orderId);
                int rowsAffected = deleteOrderStmt.executeUpdate();

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
    
    public List<Map<String, Object>> getPurchaseLogs() {
        String query = """
            SELECT 
                o.id AS order_id,
                o.created_at AS order_time,
                u.username AS user_name,
                u.email AS user_email,
                p.name AS product_name,
                p.description AS product_description,
                oi.quantity AS quantity,
                oi.price AS total_price
            FROM orders o
            JOIN users u ON o.user_id = u.id
            JOIN order_items oi ON o.id = oi.order_id
            JOIN products p ON oi.product_id = p.id
            ORDER BY o.created_at DESC;
        """;

        List<Map<String, Object>> logs = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("orderId", rs.getInt("order_id"));
                log.put("orderTime", rs.getTimestamp("order_time"));
                log.put("userName", rs.getString("user_name"));
                log.put("userEmail", rs.getString("user_email"));
                log.put("productName", rs.getString("product_name"));
                log.put("productDescription", rs.getString("product_description"));
                log.put("quantity", rs.getInt("quantity"));
                log.put("totalPrice", rs.getDouble("total_price"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

}
