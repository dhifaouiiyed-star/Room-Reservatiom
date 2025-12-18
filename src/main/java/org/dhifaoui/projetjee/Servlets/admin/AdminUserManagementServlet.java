package org.dhifaoui.projetjee.Servlets.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.UserManagementService;

import java.io.IOException;
import java.util.List;


@WebServlet("/admin/users")
public class AdminUserManagementServlet extends HttpServlet {

    private final UserManagementService userManagementService = new UserManagementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Check if user is admin
        if (!currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        // Get all users
        List<User> users = userManagementService.getAllUsers();

        // Get statistics for each user
        request.setAttribute("users", users);
        request.setAttribute("currentUserId", currentUser.getId());

        // Forward to JSP
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in and is admin
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        if (!currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");

        if (action == null || userIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        try {
            Long userId = Long.parseLong(userIdStr);

            // Prevent admin from deleting or modifying themselves
            if (userId.equals(currentUser.getId()) && ("delete".equals(action) || "toggleRole".equals(action))) {
                session.setAttribute("error", "You cannot modify your own account");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            switch (action) {
                case "delete":
                    userManagementService.deleteUser(userId);
                    session.setAttribute("success", "User deleted successfully");
                    break;

                case "toggleRole":
                    userManagementService.toggleUserRole(userId);
                    session.setAttribute("success", "User role updated successfully");
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
