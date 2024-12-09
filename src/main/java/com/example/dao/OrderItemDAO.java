package com.example.dao;

import com.example.model.OrderItem;
import com.example.model.Product;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    private ProductDAO productDAO = new ProductDAO();  // 引入 ProductDAO

    // 获取订单中的所有商品项
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.*, p.name AS product_name, p.price AS product_price " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.id " +
                       "WHERE oi.order_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(rs.getInt("id"));
                    orderItem.setOrderId(rs.getInt("order_id"));
                    orderItem.setProductId(rs.getInt("product_id"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setPrice(rs.getDouble("price"));

                    // Populate product details
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setPrice(rs.getDouble("product_price"));
                    orderItem.setProduct(product);

                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }



    // 根据 ID 获取某个商品项
    public OrderItem getOrderItemById(int id) {
        OrderItem orderItem = null;
        String query = "SELECT * FROM order_items WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    orderItem = new OrderItem();
                    orderItem.setId(rs.getInt("id"));
                    orderItem.setOrderId(rs.getInt("order_id"));
                    orderItem.setProductId(rs.getInt("product_id"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setPrice(rs.getDouble("price"));

                    // 获取关联的产品信息
                    int productId = rs.getInt("product_id");
                    Product product = productDAO.getProductById(productId);  // 根据 productId 获取产品信息
                    orderItem.setProduct(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItem;
    }

    // 添加商品项
    public boolean addOrderItem(OrderItem orderItem) {
        String query = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getProductId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getPrice());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

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


    // 更新商品项数量
    public boolean updateOrderItemQuantity(int id, int quantity) {
        String query = "UPDATE order_items SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.price, p.name AS product_name " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.id";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(rs.getInt("id"));
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setProductId(rs.getInt("product_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getDouble("price"));

                // 仅设置商品名，其他商品详细信息不需要
                Product product = new Product();
                product.setName(rs.getString("product_name"));
                orderItem.setProduct(product);

                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

}
