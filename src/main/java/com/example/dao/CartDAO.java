package com.example.dao;

import com.example.model.Cart;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // 根据用户 ID 获取该用户的购物车商品，包括商品的详细信息
    public List<Cart> getCartByUserId(int userId) {
        List<Cart> cartItems = new ArrayList<>();
        String query = "SELECT c.id, c.user_id, c.product_id, c.quantity, p.name AS product_name, p.price AS product_price " +
                       "FROM cart c " +
                       "JOIN products p ON c.product_id = p.id " +
                       "WHERE c.user_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cart cart = new Cart();
                    cart.setId(rs.getInt("id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setProductId(rs.getInt("product_id"));
                    cart.setQuantity(rs.getInt("quantity"));
                    cart.setProductName(rs.getString("product_name"));
                    cart.setProductPrice(rs.getDouble("product_price"));
                    cartItems.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // 根据用户 ID 和商品 ID 获取购物车中的单个商品，包括商品的详细信息
    public Cart getCartItem(int userId, int productId) {
        Cart cart = null;
        String query = "SELECT c.id, c.user_id, c.product_id, c.quantity, p.name AS product_name, p.price AS product_price " +
                       "FROM cart c " +
                       "JOIN products p ON c.product_id = p.id " +
                       "WHERE c.user_id = ? AND c.product_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cart = new Cart();
                    cart.setId(rs.getInt("id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setProductId(rs.getInt("product_id"));
                    cart.setQuantity(rs.getInt("quantity"));
                    cart.setProductName(rs.getString("product_name"));
                    cart.setProductPrice(rs.getDouble("product_price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cart;
    }

    // 添加商品到购物车
    public boolean addToCart(int userId, int productId, int quantity) {
        // 首先检查购物车中是否已存在该商品
        Cart existingCartItem = getCartItem(userId, productId);
        if (existingCartItem != null) {
            // 如果已经存在商品，更新商品数量
            return updateCartQuantity(existingCartItem.getId(), existingCartItem.getQuantity() + quantity);
        }

        // 如果购物车中没有该商品，则插入新记录
        String query = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 更新购物车中的商品数量
    public boolean updateCartQuantity(int cartId, int newQuantity) {
        String query = "UPDATE cart SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, cartId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除购物车中的商品
    public boolean removeFromCart(int cartId) {
        String query = "DELETE FROM cart WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cartId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除指定用户的所有购物车商品
    public boolean clearCart(int userId) {
        String query = "DELETE FROM cart WHERE user_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean checkProductStock(int productId, int quantity) {
        // 定义查询语句，获取指定商品的库存数量
        String query = "SELECT stock FROM products WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 设置查询条件
            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 获取商品的库存数量
                    int stock = rs.getInt("stock");

                    // 检查库存是否足够
                    return stock >= quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 如果没有找到商品或发生错误，返回 false
        return false;
    }

}

