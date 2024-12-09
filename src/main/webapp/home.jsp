<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
    <style>
        /* 设置背景图片 */
        body {
            margin: 0;
            padding: 0;
            height: 100%;
            background-image: url('${pageContext.request.contextPath}/images/home.jpg');
            background-size: cover;
            background-position: center;
            color: white;
            font-family: Arial, sans-serif;
        }
        
        /* 页面内容的容器 */
        .container {
            text-align: center;
            padding-top: 20%;
        }
        
        .container a {
            font-size: 20px;
            color: white;
            text-decoration: none;
            margin: 0 15px;
            padding: 10px 20px;
            border: 2px solid white;
            border-radius: 5px;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .container a:hover {
            background-color: rgba(255, 255, 255, 0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>欢迎访问线上购物网站！</h1>
        <p>
            <a href="${pageContext.request.contextPath}/login.jsp">登录</a>
            <a href="${pageContext.request.contextPath}/signup.jsp">注册</a>
        </p>
    </div>
</body>
</html>

