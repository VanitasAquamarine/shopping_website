package com.example.servlet;

import com.example.dao.*;
import com.example.model.*;
import com.example.util.EmailUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("view".equals(action)) {
            viewOrderDetails(req, resp); // 查看订单详情逻辑
        } else {
            prepareOrderCreationPage(req, resp); // 准备订单创建页面
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            createOrder(req, resp); // 创建订单逻辑
        } else {
            resp.getWriter().write("Invalid action for POST request.");
        }
    }

    // 准备订单创建页面
    private void prepareOrderCreationPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<Cart> cartItems = cartDAO.getCartByUserId(userId);
        if (cartItems.isEmpty()) {
            req.setAttribute("errorMessage", "Your cart is empty! Please add items before placing an order.");
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
            return;
        }

        double totalPrice = 0;
        for (Cart item : cartItems) {
            totalPrice += item.getProductPrice() * item.getQuantity();
        }

        req.setAttribute("user", user);
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalPrice", totalPrice);

        req.getRequestDispatcher("/order.jsp").forward(req, resp);
    }

    // 创建订单
    private void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = userDAO.getUserById(userId);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String shippingAddress = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");
        System.out.println("Shipping Address: " + req.getParameter("shippingAddress"));
        System.out.println("Payment Method: " + req.getParameter("paymentMethod"));


        if (shippingAddress == null || shippingAddress.trim().isEmpty() || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Shipping address and payment method are required.");
            prepareOrderCreationPage(req, resp);
            return;
        }

        List<Cart> cartItems = cartDAO.getCartByUserId(userId);
        if (cartItems.isEmpty()) {
            req.setAttribute("errorMessage", "Your cart is empty! Please add items before placing an order.");
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
            return;
        }

        double totalPrice = 0;
        for (Cart item : cartItems) {
            totalPrice += item.getProductPrice() * item.getQuantity();
            if (!cartDAO.checkProductStock(item.getProductId(), item.getQuantity())) {
                req.setAttribute("errorMessage", "Insufficient stock for product: " + item.getProductName());
                req.getRequestDispatcher("/cart.jsp").forward(req, resp);
                return;
            }
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus("paid");
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(shippingAddress);

        if (orderDAO.addOrder(order)) {
            int orderId = order.getId();

            for (Cart item : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getProductPrice() * item.getQuantity());
                orderItemDAO.addOrderItem(orderItem);
                productDAO.updateStock(item.getProductId(),item.getQuantity());
            }

            cartDAO.clearCart(userId);
            sendOrderConfirmationEmail(user, order);

            resp.sendRedirect(req.getContextPath() + "/OrderServlet?action=view&orderId=" + orderId);
        } else {
            req.setAttribute("errorMessage", "Failed to create order. Please try again.");
            req.getRequestDispatcher("/order.jsp").forward(req, resp);
        }
    }

    // 查看订单详情
    private void viewOrderDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = userDAO.getUserById(userId);
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderDAO.getOrderById(orderId);

        if (order == null || order.getUserId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this order.");
            return;
        }

        List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);

        req.setAttribute("user", user);
        req.setAttribute("order", order);
        req.setAttribute("orderItems", orderItems);

        req.getRequestDispatcher("/myorder.jsp").forward(req, resp);
    }

    private void sendOrderConfirmationEmail(User user, Order order) {
        try {
            String recipient = user.getEmail();
            String subject = "Order Confirmation - Order ID: " + order.getId();
            String content = "Dear " + user.getUsername() + ",\n\n" +
                    "Thank you for your order! Here are your order details:\n" +
                    "Order ID: " + order.getId() + "\n" +
                    "Total Price: $" + order.getTotalPrice() + "\n" +
                    "Payment Method: " + order.getPaymentMethod() + "\n" +
                    "Shipping Address: " + order.getShippingAddress() + "\n\n" +
                    "We will notify you once your order is shipped.\n\n" +
                    "Best regards,\n" +
                    "Online Shopping Website";

            EmailUtils.sendEmail(recipient, subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

