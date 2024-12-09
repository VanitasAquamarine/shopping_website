<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/login.jpg');
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .signup-container {
            width: 300px;
            padding: 30px;
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 10px;
            margin: 100px auto;
        }
        .signup-container h2 {
            text-align: center;
        }
        .signup-container input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .signup-container button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .signup-container button:hover {
            background-color: #45a049;
        }
        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="signup-container">
        <h2>注册</h2>
        
        <%-- Check if there is an error message to display --%>
        <c:if test="${not empty error}">
    <div class="error-message">${error}</div>
        </c:if>


       <form action="${pageContext.request.contextPath}/userManage" method="post" accept-charset="UTF-8">
            <!-- Hidden action -->
            <input type="hidden" name="action" value="register">
            
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" placeholder="用户名" required>

            <label for="password">密码:</label>
            <input type="password" id="password" name="password" placeholder="密码" required>

            <label for="email">邮箱:</label>
            <input type="email" id="email" name="email" placeholder="邮箱" required>

            <button type="submit">注册</button>
        </form>
        <p style="text-align: center;">已有账号？<a href="${pageContext.request.contextPath}/login.jsp">登录</a></p>
    </div>
</body>
</html>
