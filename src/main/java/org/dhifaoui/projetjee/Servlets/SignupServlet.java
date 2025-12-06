package org.dhifaoui.projetjee.Servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dhifaoui.projetjee.entities.UserRole;
import org.dhifaoui.projetjee.service.AuthenticationService;

import java.io.IOException;

/**
 * Servlet for handling user registration
 */
public class SignupServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display signup page
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String roleParam = request.getParameter("role");

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Username is required");
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        if (confirmPassword == null || !password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        // Determine role (default to USER if not specified)
        UserRole role = UserRole.USER;
        if (roleParam != null && !roleParam.isEmpty()) {
            try {
                role = UserRole.valueOf(roleParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                role = UserRole.USER;
            }
        }

        try {
            // Register user
            authService.register(username, email, password, role);

            // Set success message
            request.getSession().setAttribute("successMessage",
                    "Registration successful! Please login with your credentials.");

            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (IllegalArgumentException e) {
            // Handle validation errors
            request.setAttribute("error", e.getMessage());
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle unexpected errors
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            preserveFormData(request, username, email);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
        }
    }

    /**
     * Preserve form data to redisplay in case of errors
     */
    private void preserveFormData(HttpServletRequest request, String username, String email) {
        request.setAttribute("username", username);
        request.setAttribute("email", email);
    }
}
