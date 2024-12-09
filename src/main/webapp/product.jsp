<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details</title>
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/login.jpg');
            background-size: cover;
            color: white;
            font-family: Arial, sans-serif;
        }
        .container {
            display: flex;
            justify-content: space-around;
            margin-top: 50px;
        }
        .left-side {
            width: 40%;
        }
        .right-side {
            width: 40%;
        }
        .product-image {
            max-width: 100%;
            border: 1px solid #ddd;
        }
        .product-details {
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: rgba(0, 0, 0, 0.5);
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.7);
            z-index: 1000;
        }
        .modal-content {
            position: relative;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            width: 300px;
        }
        .close {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 30px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Left side: Product image -->
        <div class="left-side">
            <img src="${pageContext.request.contextPath}/images/${product.imageUrl}" alt="Product Image" class="product-image">
        </div>

        <!-- Right side: Product details -->
        <div class="right-side">
            <div class="product-details">
                <h1>${product.name}</h1>
                <p>${product.description}</p>
                <p><strong>单价:</strong> $${product.price}</p>

                <!-- Form for adding to cart -->
                <form id="addToCartForm" method="POST" action="${pageContext.request.contextPath}/CartServlet">
                    <input type="hidden" name="action" value="addToCart">
                    <input type="hidden" name="productId" value="${product.id}">
                    <label for="quantity">Quantity: </label>
                    <input type="number" id="quantity" name="quantity" value="1" min="1" max="${product.stock}">
                    <p>Total Price: $<span id="total-price">${product.price}</span></p>
                    <button class="btn" type="submit">添加到购物车</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        var quantityInput = document.getElementById("quantity");
        var totalPrice = document.getElementById("total-price");

        // Update total price when quantity changes
        quantityInput.oninput = function() {
            var price = ${product.price};
            var quantity = parseInt(quantityInput.value);
            totalPrice.textContent = (price * quantity).toFixed(2);
        }
    </script>
</body>

</html>
