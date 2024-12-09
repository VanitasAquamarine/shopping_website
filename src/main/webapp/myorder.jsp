<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1000px;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1, h2 {
            text-align: center;
            color: #333;
        }
        .user-info, .order-summary {
            margin-bottom: 30px;
        }
        .user-info p, .order-summary p {
            margin: 5px 0;
        }
        .order-items table {
            width: 100%;
            border-collapse: collapse;
        }
        .order-items th, .order-items td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        .order-items th {
            background-color: #f4f4f4;
        }
        .order-total {
            text-align: right;
            margin-top: 20px;
            font-size: 18px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>我的订单</h1>

    <!-- User Info -->
    <div class="user-info">
        <h2>个人信息</h2>
        <p><strong>用户名:</strong> ${user.username}</p>
        <p><strong>Email:</strong> ${user.email}</p>
    </div>

    <!-- Order Summary -->
    <div class="order-summary">
        <h2>订单详情</h2>
        <p><strong>订单号:</strong> ${order.id}</p>
        <p><strong>收货地址:</strong> ${order.shippingAddress}</p>
        <p><strong>支付方式:</strong> ${order.paymentMethod}</p>
        <p><strong>订单状态:</strong> ${order.status}</p>
    </div>

    <!-- Order Items -->
    <div class="order-items">
        <h2>Purchased Items</h2>
        <table>
            <thead>
            <tr>
                <th>商品</th>
                <th>数量</th>
                <th>价格</th>
                <th>总计</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${orderItems}">
                <tr>
                    <td>${item.product.name}</td>
                    <td>${item.quantity}</td>
                    <td>$${item.product.price}</td>
                    <td>$${item.quantity * item.product.price}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Order Total -->
    <div class="order-total">
        <p>订单总价为: $${order.totalPrice}</p>
    </div>
     <!-- Back to Shopping Website Button -->
    <div class="back-button">
        <a href="${pageContext.request.contextPath}/shopping">返回购物页面</a>
    </div>
</div>
</body>
</html>
