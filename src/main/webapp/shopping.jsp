<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping</title>
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/login.jpg');
            background-size: cover;
            background-position: center;
            font-family: Arial, sans-serif;
        }
        .header {
            display: flex;
            justify-content: flex-end;
            padding: 20px;
        }
        .header a {
            color: white;
            text-decoration: none;
            font-size: 18px;
            margin-left: 20px;
        }
        .product-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            margin-top: 50px;
        }
        .product {
            width: 250px;
            padding: 15px;
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 10px;
            text-align: center;
        }
        .product img {
            max-width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
        }
        .product h3 {
            font-size: 18px;
            margin-top: 10px;
        }
        .product p {
            font-size: 16px;
        }
        .product a {
            color: #333;
            text-decoration: none;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <div class="header">
        <a href="${pageContext.request.contextPath}/CartServlet">我的购物车</a>
        <a href="${pageContext.request.contextPath}/logout" style="color: white; font-size: 18px; margin-left: 20px;">退出登录</a>
        <a href="javascript:void(0)" onclick="showDeleteAccountModal()" style="color: white; font-size: 18px; margin-left: 20px;">注销账号</a>
    </div>

    <div class="product-container">
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach var="product" items="${products}">
                    <div class="product">
                        <img src="${pageContext.request.contextPath}/images/<c:out value='${product.imageUrl}' />" 
     alt="<c:out value='${product.name}' />">
                        <h3><c:out value='${product.name}' /></h3>
                        <p>分类: <c:out value='${product.category.name}' /></p>
                        <p>单价: $<c:out value='${product.price}' /></p>
                        <a href="${pageContext.request.contextPath}/shopping?id=${product.id}">查看商品细节</a>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No products found in the database.</p>
            </c:otherwise>
        </c:choose>
    </div>
    <!-- 删除账号确认弹窗 -->
<div id="deleteAccountModal" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: rgba(0, 0, 0, 0.7); padding: 20px; border-radius: 10px; color: white; text-align: center;">
    <p>确定要注销账号吗？你的相关信息会被删除。</p>
    <!-- 使用表单来提交删除请求 -->
    <form action="${pageContext.request.contextPath}/DeleteAccountServlet" method="POST">
        <button type="submit" style="background-color: red; color: white; padding: 10px 20px; border: none; cursor: pointer;">确定</button>
    </form>
    <button onclick="closeDeleteAccountModal()" style="background-color: gray; color: white; padding: 10px 20px; border: none; cursor: pointer;">取消</button>
</div>


<script>
    function showDeleteAccountModal() {
        document.getElementById('deleteAccountModal').style.display = 'block';
    }

    function closeDeleteAccountModal() {
        document.getElementById('deleteAccountModal').style.display = 'none';
    }

    function deleteAccount() {
        window.location.href = '${pageContext.request.contextPath}/DeleteAccountServlet';
    }
</script>
</body>

</html>
