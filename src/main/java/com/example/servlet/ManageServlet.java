package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.dao.UserDAO;
import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;

@WebServlet("/manage")
@MultipartConfig
public class ManageServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String IMAGE_FOLDER = "images"; // 图片存储路径
    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderitemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()||action.equals("view")) {
            viewData(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for GET request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 解决中文乱码问题
        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing or invalid.");
            return;
        }

        switch (action) {
            case "addProduct":
                handleAddProduct(request, response);
                break;
            case "updateProduct":
                handleUpdateProduct(request, response);
                break;
            case "deleteProduct":
                handleDeleteProduct(request, response);
                break;
            case "deleteUser":
                handleDeleteUser(request, response);
                break;
            case "deleteOrder":
                handleDeleteOrder(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    private void viewData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("products", productDAO.getAllProducts());
        request.setAttribute("users", userDAO.getAllUsers());
        request.setAttribute("orders", orderDAO.getAllOrders());
        request.setAttribute("orderItems", orderitemDAO.getAllOrderItems());
        request.getRequestDispatcher("/manage.jsp").forward(request, response);
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // 处理图片上传
        Part filePart = request.getPart("image");
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = extractFileName(filePart);
            if (fileName.endsWith(".jpg")) {
                String savePath = getServletContext().getRealPath("/") + IMAGE_FOLDER + File.separator + fileName;
                filePart.write(savePath);
                imageUrl = fileName; // 仅存储文件名
            } else {
                request.setAttribute("errorMessage", "Only JPG images are allowed.");
                viewData(request, response);
                return;
            }
        }

        Product product = new Product(name, description, price, stock, categoryId);
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }

        if (productDAO.addProduct(product)) {
            response.sendRedirect(request.getContextPath() + "/manage?action=view");
        } else {
            request.setAttribute("errorMessage", "Failed to add product.");
            viewData(request, response);
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // 处理图片上传
        Part filePart = request.getPart("image");
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = extractFileName(filePart);
            if (fileName.endsWith(".jpg")) {
                String savePath = getServletContext().getRealPath("/") + IMAGE_FOLDER + File.separator + fileName;
                filePart.write(savePath);
                imageUrl = fileName; // 仅存储文件名
            } else {
                request.setAttribute("errorMessage", "Only JPG images are allowed.");
                viewData(request, response);
                return;
            }
        }

        Product product = new Product(name, description, price, stock, categoryId);
        product.setId(productId);
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }

        if (productDAO.updateProduct(product)) {
            response.sendRedirect(request.getContextPath() + "/manage?action=view");
        } else {
            request.setAttribute("errorMessage", "Failed to update product.");
            viewData(request, response);
        }
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        if (productDAO.deleteProduct(productId)) {
            response.sendRedirect(request.getContextPath() + "/manage?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete product.");
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        if (userDAO.deleteUser(userId)) {
            response.sendRedirect(request.getContextPath() + "/manage?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user.");
        }
    }

    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        if (orderDAO.deleteOrder(orderId)) {
            response.sendRedirect(request.getContextPath() + "/manage?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete order.");
        }
    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}
