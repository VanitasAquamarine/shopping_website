package com.example.model;

import java.sql.Timestamp;

public class Log {
    private int id;
    private int userId;
    private int productId;
    private String action;
    private Timestamp timestamp;
    private boolean isPaymentSuccessful;
    private User user;  // 关联的用户
    private Product product;  // 关联的商品

    // Constructor
    public Log(int id, int userId, int productId, String action, Timestamp timestamp, boolean isPaymentSuccessful, User user, Product product) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.action = action;
        this.timestamp = timestamp;
        this.isPaymentSuccessful = isPaymentSuccessful;
        this.user = user;
        this.product = product;
    }

    // Default Constructor
    public Log() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPaymentSuccessful() {
        return isPaymentSuccessful;
    }

    public void setPaymentSuccessful(boolean paymentSuccessful) {
        isPaymentSuccessful = paymentSuccessful;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                ", isPaymentSuccessful=" + isPaymentSuccessful +
                ", user=" + user +
                ", product=" + product +
                '}';
    }
}
