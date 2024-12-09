package com.example.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String createdAt;
    private Category category; // 表示商品所属类别
    private int categoryId; // 新增字段，便于关联

    // 添加商品时的构造方法
    public Product(String name, String description, double price, int stock, int categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = new Category(categoryId, null); // 初始化 Category 对象
    }


    // 更新商品时的构造方法
    public Product(int id, String name, String description, double price, int stock, String imageUrl, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    // 完整构造方法（包含创建时间和类别对象）
    public Product(int id, String name, String description, double price, int stock, String imageUrl, String createdAt, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.category = category;
    }

    public Product() {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
