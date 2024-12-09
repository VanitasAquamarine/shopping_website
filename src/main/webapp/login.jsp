<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/login.jpg');
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .login-container {
            width: 300px;
            padding: 30px;
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 10px;
            margin: 100px auto;
        }
        .login-container h2 {
            text-align: center;
        }
        .login-container input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .login-container button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .login-container button:hover {
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
    <div class="login-container">
        <h2>登录</h2>
        <%-- 检查是否有错误消息需要显示 --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/userManage" method="POST">
            <!-- Hidden action -->
            <input type="hidden" name="action" value="login">
            
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" placeholder="请输入用户名" required>
            
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" placeholder="请输入密码" required>
            
            <button type="submit">登录</button>
        </form>
        <p style="text-align: center;">还没有账号？<a href="${pageContext.request.contextPath}/signup.jsp">注册</a></p>
    </div>
</body>
</html>
