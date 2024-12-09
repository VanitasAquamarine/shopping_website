package com.example.model;

import java.sql.Timestamp;

public class Shipment {
    private int id;
    private Order order;  // 订单对象，与订单表的外键关联
    private Timestamp shippedDate;
    private String shippingStatus; // shipping status ('pending', 'shipped', 'delivered')
    private String trackingNumber;

    // Constructor
    public Shipment(int id, Order order, Timestamp shippedDate, String shippingStatus, String trackingNumber) {
        this.id = id;
        this.order = order;
        this.shippedDate = shippedDate;
        this.shippingStatus = shippingStatus;
        this.trackingNumber = trackingNumber;
    }

    // Default Constructor
    public Shipment() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Timestamp getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Timestamp shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", order=" + order +
                ", shippedDate=" + shippedDate +
                ", shippingStatus='" + shippingStatus + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                '}';
    }
}

