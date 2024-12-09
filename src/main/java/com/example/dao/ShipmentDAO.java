package com.example.dao;

import com.example.model.Shipment;
import com.example.model.Order;
import com.example.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {

    private OrderDAO orderDAO = new OrderDAO();  // 引入 OrderDAO

    // 获取所有发货记录
    public List<Shipment> getAllShipments() {
        List<Shipment> shipments = new ArrayList<>();
        String query = "SELECT * FROM shipment";  // 获取所有发货记录的查询语句

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setId(rs.getInt("id"));
                
                // 获取订单信息
                int orderId = rs.getInt("order_id");
                Order order = orderDAO.getOrderById(orderId);  // 根据 orderId 获取订单信息
                shipment.setOrder(order);
                
                shipment.setShippedDate(rs.getTimestamp("shipped_date"));
                shipment.setShippingStatus(rs.getString("shipping_status"));
                shipment.setTrackingNumber(rs.getString("tracking_number"));

                shipments.add(shipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shipments;
    }

    // 根据 ID 获取发货记录
    public Shipment getShipmentById(int id) {
        Shipment shipment = null;
        String query = "SELECT * FROM shipment WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    shipment = new Shipment();
                    shipment.setId(rs.getInt("id"));
                    
                    // 获取订单信息
                    int orderId = rs.getInt("order_id");
                    Order order = orderDAO.getOrderById(orderId);  // 根据 orderId 获取订单信息
                    shipment.setOrder(order);
                    
                    shipment.setShippedDate(rs.getTimestamp("shipped_date"));
                    shipment.setShippingStatus(rs.getString("shipping_status"));
                    shipment.setTrackingNumber(rs.getString("tracking_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shipment;
    }

    // 添加发货记录
    public boolean addShipment(Shipment shipment) {
        String query = "INSERT INTO shipment (order_id, shipped_date, shipping_status, tracking_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, shipment.getOrder().getId());  // 使用关联的 Order 对象的 ID
            stmt.setTimestamp(2, shipment.getShippedDate());
            stmt.setString(3, shipment.getShippingStatus());
            stmt.setString(4, shipment.getTrackingNumber());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 更新发货记录
    public boolean updateShipment(Shipment shipment) {
        String query = "UPDATE shipment SET order_id = ?, shipped_date = ?, shipping_status = ?, tracking_number = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, shipment.getOrder().getId());  // 使用关联的 Order 对象的 ID
            stmt.setTimestamp(2, shipment.getShippedDate());
            stmt.setString(3, shipment.getShippingStatus());
            stmt.setString(4, shipment.getTrackingNumber());
            stmt.setInt(5, shipment.getId());  // 根据发货记录 ID 更新记录

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除发货记录
    public boolean deleteShipment(int shipmentId) {
        String query = "DELETE FROM shipment WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, shipmentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

