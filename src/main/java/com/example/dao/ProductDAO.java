package com.example.dao;

import com.example.model.Product;
import com.example.model.Category;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private CategoryDAO categoryDAO = new CategoryDAO();  // 引入 CategoryDAO

    // 获取所有商品
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products"; // 获取所有商品的查询语句

        try (Connection conn = DatabaseUtils.getConnection()) {
            System.out.println("Database connection successful: " + conn);
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setImageUrl(rs.getString("image_url"));
                    
                    // 获取分类信息
                    int categoryId = rs.getInt("category_id");
                    if (categoryId > 0) {
                        Category category = categoryDAO.getCategoryById(categoryId); // 获取分类信息
                        product.setCategory(category);
                    }

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded products: " + products);
        return products;
    }


    // 根据 ID 获取商品
    public Product getProductById(int id) {
        Product product = null;
        String query = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setImageUrl(rs.getString("image_url")); // 获取图片路径
                    
                    // 获取分类信息
                    int categoryId = rs.getInt("category_id");
                    Category category = categoryDAO.getCategoryById(categoryId);
                    product.setCategory(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    // 添加商品
    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (name, description, price, stock, image_url, category_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setInt(6, product.getCategory().getId()); // 确保 category 不为空

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // 更新商品
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, price = ?, description = ?, stock = ?, category_id = ?, image_url = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getCategory().getId());  // 使用关联的 Category 对象的 ID
            stmt.setString(6, product.getImageUrl()); // 更新图片路径
            stmt.setInt(7, product.getId());  // 根据商品 ID 更新记录

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

 // 删除商品
    public boolean deleteProduct(int productId) {
        String deleteOrderItemsSql = "DELETE FROM order_items WHERE product_id = ?";
        String deleteCartSql = "DELETE FROM cart WHERE product_id = ?";
        String deleteProductSql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection()) {
            conn.setAutoCommit(false); // 开启事务

            try (PreparedStatement deleteOrderItemsStmt = conn.prepareStatement(deleteOrderItemsSql);
                 PreparedStatement deleteCartStmt = conn.prepareStatement(deleteCartSql);
                 PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductSql)) {

                // 删除关联的订单项
                deleteOrderItemsStmt.setInt(1, productId);
                deleteOrderItemsStmt.executeUpdate();

                // 删除购物车中的商品
                deleteCartStmt.setInt(1, productId);
                deleteCartStmt.executeUpdate();

                // 删除商品
                deleteProductStmt.setInt(1, productId);
                int rowsAffected = deleteProductStmt.executeUpdate();

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


    // 删除所有库存为 0 的商品
    public boolean deleteOutOfStockProducts() {
        String query = "DELETE FROM products WHERE stock = 0";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean updateStock(int productId, int quantity) {
        String query = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity); // 确保库存不会减成负数

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

