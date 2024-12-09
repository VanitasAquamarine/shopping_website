package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/userManage")
public class UserManageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    // 处理 POST 请求
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing");
            return;
        }

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;
            case "login":
                handleLogin(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    // 处理用户注册
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // 参数校验
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
            request.setAttribute("error", "请填写完整的注册信息");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        System.out.println("Register action triggered with: " + username + ", " + email); // 调试日志

        try {
            // 检查用户名和邮箱是否已存在
            if (userDAO.checkUsernameExists(username)) {
                request.setAttribute("error", "用户名已存在");
                request.getRequestDispatcher("/signup.jsp").forward(request, response);
                return;
            }

            if (userDAO.checkEmailExists(email)) {
                request.setAttribute("error", "邮箱已注册");
                request.getRequestDispatcher("/signup.jsp").forward(request, response);
                return;
            }

            // 创建新用户并注册
            User user = new User(username, password, email);
            if (userDAO.addUser(user)) {
                System.out.println("User registered successfully");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } else {
                request.setAttribute("error", "注册失败，请稍后重试");
                request.getRequestDispatcher("/signup.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志
            request.setAttribute("error", "服务器异常，请稍后重试");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
        }
    }

    // 处理用户登录
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 参数校验
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "请填写用户名和密码");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = userDAO.getUserByUsernameAndPassword(username, password);
            if (user != null) {
                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId()); // 将 userId 存入 session
                session.setAttribute("username", user.getUsername()); // 可选，继续保留 username
                System.out.println("User logged in successfully: " + username);
             // 跳转到不同页面
                if ("admin".equals(user.getRole())) {
                	response.sendRedirect(request.getContextPath() + "/manage?action=view");
                } else {
                    response.sendRedirect(request.getContextPath() + "/shopping");
                }
            } else {
                // 登录失败
                request.setAttribute("error", "用户名或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志
            request.setAttribute("error", "服务器异常，请稍后重试");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}

