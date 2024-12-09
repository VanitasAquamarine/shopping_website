package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.CartDAO;
import com.example.model.Cart;

import java.io.IOException;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO = new CartDAO();

    // 处理 GET 请求，显示购物车内容
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户 ID
    	Integer userId = (Integer) request.getSession().getAttribute("userId");

        // 获取该用户的购物车内容
        List<Cart> cartItems = cartDAO.getCartByUserId(userId);

        // 计算购物车总金额
        double totalAmount = 0;
        for (Cart cartItem : cartItems) {
            totalAmount += cartItem.getProductPrice() * cartItem.getQuantity();
        }

        // 将购物车内容和总金额传递给 JSP 页面
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalAmount", totalAmount);

        // 检查是否有错误信息传递
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
        }

        // 转发到 cart.jsp 页面
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    // 处理 POST 请求，添加商品到购物车或删除商品
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // 如果是添加商品到购物车
        if ("addToCart".equals(action)) {
        	Integer userId = (Integer) request.getSession().getAttribute("userId");
        	if (userId == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not logged in");
                return;
            }
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // 检查库存
            if (cartDAO.checkProductStock(productId, quantity)) {
                // 将商品添加到购物车
                if (cartDAO.addToCart(userId, productId, quantity)) {
                	response.sendRedirect(request.getContextPath() + "/CartServlet?userId=" + userId); // 成功添加，跳转到购物车页面
                } else {
                    // 添加失败，传递错误消息到 cart.jsp
                    request.setAttribute("errorMessage", "Failed to add product to cart. Please try again.");
                    doGet(request, response);
                }
            } else {
                // 库存不足，传递错误消息到 cart.jsp
                request.setAttribute("errorMessage", "Insufficient stock for the selected product.");
                doGet(request, response); // 转发到 cart.jsp 显示错误信息
            }
        }

        // 如果是删除购物车中的商品
        else if ("removeFromCart".equals(action)) {
            int cartId = Integer.parseInt(request.getParameter("cartId"));

            // 删除购物车中的商品
            boolean success = cartDAO.removeFromCart(cartId);
            if (success) {
                response.sendRedirect(request.getContextPath() +"/cart.jsp"); // 删除成功后刷新购物车页面
            } else {
                // 删除失败，传递错误消息到 cart.jsp
                request.setAttribute("errorMessage", "Failed to remove product from cart. Please try again.");
                doGet(request, response);
            }
        }
    }
}
