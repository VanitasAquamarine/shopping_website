<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 90%;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1, h2 {
            text-align: center;
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        table th, table td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        table th {
            background-color: #f4f4f4;
        }
        .actions {
            margin: 10px 0;
            text-align: right;
        }
        .actions button {
            margin-left: 10px;
            padding: 8px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .actions button:hover {
            background-color: #45a049;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.7);
        }
        .modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 20px;
            border-radius: 10px;
            width: 60%;
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .close {
            cursor: pointer;
            font-size: 20px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        form label {
            margin-top: 10px;
        }
        form input, form select {
            padding: 8px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        form button {
            margin-top: 15px;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        form button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Admin Management</h1>

    <!-- Product Management -->
    <div id="product-management">
        <h2>Product Management</h2>
        <div class="actions">
            <button onclick="openModal('add-product-modal')">Add Product</button>
            <button onclick="openModal('delete-product-modal')">Delete Product</button>
            <button onclick="openModal('update-product-modal')">Update Product</button>
        </div>
        <table id="product-table">
            <thead>
            <tr>
                <th>Product ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Category</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                    <td>${product.price}</td>
                    <td>${product.stock}</td>
                    <td>${product.category.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- User Management -->
    <div id="user-management">
        <h2>User Management</h2>
        <div class="actions">
            <button onclick="openModal('delete-user-modal')">Delete User</button>
        </div>
        <table id="user-table">
            <thead>
            <tr>
                <th>User ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Order Management -->
    <div id="order-management">
        <h2>Order Management</h2>
        <div class="actions">
            <button onclick="openModal('delete-order-modal')">Delete Order</button>
        </div>
        <table id="order-table">
            <thead>
            <tr>
                <th>Order ID</th>
                <th>User ID</th>
                <th>Total Price</th>
                <th>Status</th>
                <th>Created At</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>${order.id}</td>
                    <td>${order.userId}</td>
                    <td>${order.totalPrice}</td>
                    <td>${order.status}</td>
                    <td> <fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" timeZone="Asia/Shanghai" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <!-- Order Item Management -->
    <div id="order-item-management">
        <h2>Order Item Management</h2>
        <table id="order-item-table">
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Product</th>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="orderItem" items="${orderItems}">
                <tr>
                    <td>${orderItem.orderId}</td>
                    <td>${orderItem.product.name}</td>
                    <td>${orderItem.quantity}</td>
                    <td>${orderItem.price}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<!-- Add Product Modal -->
<div id="add-product-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Add Product</h2>
            <span class="close" onclick="closeModal('add-product-modal')">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/manage" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="addProduct">
            <label for="product-name">Name:</label>
            <input type="text" id="product-name" name="name" required>
            <label for="product-description">Description:</label>
            <input type="text" id="product-description" name="description" required>
            <label for="product-price">Price:</label>
            <input type="number" id="product-price" name="price" step="0.01" required>
            <label for="product-stock">Stock:</label>
            <input type="number" id="product-stock" name="stock" required>
            <label for="product-category">Category ID:</label>
            <input type="number" id="product-category" name="categoryId" required>
            <label for="product-image">Image:</label>
            <input type="file" id="product-image" name="image" accept=".jpg">
            <button type="submit">Add Product</button>
        </form>
    </div>
</div>
<!-- Update Product Modal -->
<div id="update-product-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Update Product</h2>
            <span class="close" onclick="closeModal('update-product-modal')">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/manage" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateProduct">
            <label for="update-product-id">Product ID:</label>
            <input type="number" id="update-product-id" name="id" required>
            <label for="update-product-name">Name:</label>
            <input type="text" id="update-product-name" name="name" required>
            <label for="update-product-description">Description:</label>
            <input type="text" id="update-product-description" name="description" required>
            <label for="update-product-price">Price:</label>
            <input type="number" id="update-product-price" name="price" step="0.01" required>
            <label for="update-product-stock">Stock:</label>
            <input type="number" id="update-product-stock" name="stock" required>
            <label for="update-product-category">Category ID:</label>
            <input type="number" id="update-product-category" name="categoryId" required>
            <label for="update-product-image">Image:</label>
            <input type="file" id="update-product-image" name="image" accept=".jpg">
            <button type="submit">Update Product</button>
        </form>
    </div>
</div>
<!-- Delete Product Modal -->
<div id="delete-product-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Delete Product</h2>
            <span class="close" onclick="closeModal('delete-product-modal')">&times;</span>
        </div>
        <form action="/shopping_website/manage?action=deleteProduct" method="post">
            <label for="product-id">Enter Product ID:</label>
            <input type="number" id="product-id" name="id" required>
            <button type="submit">Delete</button>
        </form>
    </div>
</div>

<!-- Delete User Modal -->
<div id="delete-user-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Delete User</h2>
            <span class="close" onclick="closeModal('delete-user-modal')">&times;</span>
        </div>
        <form action="/shopping_website/manage?action=deleteUser" method="post">
            <label for="user-id">Enter User ID:</label>
            <input type="number" id="user-id" name="id" required>
            <button type="submit">Delete</button>
        </form>
    </div>
</div>

<!-- Delete Order Modal -->
<div id="delete-order-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Delete Order</h2>
            <span class="close" onclick="closeModal('delete-order-modal')">&times;</span>
        </div>
        <form action="/shopping_website/manage?action=deleteOrder" method="post">
            <label for="order-id">Enter Order ID:</label>
            <input type="number" id="order-id" name="id" required>
            <button type="submit">Delete</button>
        </form>
    </div>
</div>

<script>
    let currentAction = '';

    function openModal(modalId) {
        document.getElementById(modalId).style.display = 'block';
    }

    function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }

    
</script>

</body>

</html>
