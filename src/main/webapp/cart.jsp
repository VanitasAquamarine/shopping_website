<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/login.jpg');
            background-size: cover;
            color: white;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            height: 100vh; /* Ensure the body takes full height */
            position: relative;
        }

        /* Overlay for the semi-transparent white box */
        .overlay {
            position: absolute;
            top: 50px; /* Below the orange header */
            left: 20px; /* Add some margin for left side */
            right: 20px; /* Add some margin for right side */
            bottom: 20px; /* Add some margin for bottom side */
            background-color: rgba(255, 255, 255, 0.8); /* Semi-transparent white */
            border-radius: 10px;
            z-index: 1; /* Ensure the overlay is below the content */
        }

        /* Your Shopping Cart Header with Orange Border */
        h1 {
            background-color: orange;
            color: white;
            padding: 10px;
            margin: 0;
            text-align: center;
            border-radius: 5px 5px 0 0;
        }

        .container {
            position: relative;
            z-index: 2; /* Ensure the container is above the overlay */
            padding: 20px;
        }

        .cart-item {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            padding: 10px;
            background-color: rgba(0, 0, 0, 0.6);
            border-radius: 8px;
        }

        .cart-item img {
            max-width: 100px;
            max-height: 100px;
            margin-right: 20px;
        }

        .cart-item-details {
            flex: 1;
        }

        .cart-item-details p {
            margin: 5px 0;
        }

        .cart-item button {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
            border-radius: 5px;
        }

        .cart-item button:hover {
            background-color: #e53935;
        }

        .total-price {
            text-align: right;
            font-size: 20px;
            margin-top: 20px;
            color: black;
        }

        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            display: block;
            width: 200px;
            margin-top: 20px;
            text-align: center;
        }

        .btn:hover {
            background-color: #45a049;
        }

        .continue-shopping-btn {
            background-color: #f89c42;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            display: inline-block;
            text-align: center;
            margin-right: 10px;
        }

        .continue-shopping-btn:hover {
            background-color: #e6892d;
        }
    </style>
</head>
<body>
    <!-- Semi-transparent white box as overlay -->
    <div class="overlay"></div>

    <div class="container">
        <!-- Your Shopping Cart Header with Orange Border -->
        <h1>Your Shopping Cart</h1>

        <!-- Loop through cart items -->
        <c:forEach var="cartItem" items="${cartItems}">
            <div class="cart-item">
                <div class="cart-item-details">
                    <p><strong>${cartItem.productName}</strong></p>
                    <p>Price: $${cartItem.productPrice}</p>
                    <p>Quantity: ${cartItem.quantity}</p>
                    <p>Total: $${cartItem.productPrice * cartItem.quantity}</p>
                </div>
                <form action="${pageContext.request.contextPath}/CartServlet" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="removeFromCart">
                    <input type="hidden" name="cartId" value="${cartItem.id}">
                    <button type="submit">Delete</button>
                </form>
            </div>
        </c:forEach>

        <!-- Total price -->
        <div class="total-price">
            <p><strong>Total Amount: $${totalAmount}</strong></p>
        </div>

        <!-- Buttons: Continue Shopping and Confirm Purchase -->
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <!-- Continue shopping button (left side) -->
            <a href="${pageContext.request.contextPath}/shopping">
                <button class="continue-shopping-btn">Continue Shopping</button>
            </a>

            <!-- Confirm purchase button (right side) -->
            <a href="javascript:void(0)" onclick="document.getElementById('confirmOrderForm').submit();">
                <button class="btn">Confirm Purchase</button>
            </a>
            <form id="confirmOrderForm" method="POST" action="${pageContext.request.contextPath}/OrderServlet" style="display:none;">
                <input type="hidden" name="action" value="create">
            </form>
        </div>
    </div>
</body>
</html>

