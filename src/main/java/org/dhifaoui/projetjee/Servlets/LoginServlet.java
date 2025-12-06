package org.dhifaoui.projetjee.Servlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.AuthenticationService;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for handling user login
 */
public class LoginServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            redirectBasedOnRole(response, user);
            return;
        }

        // Display login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        Optional<User> userOpt = authService.authenticate(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole());

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            // Redirect based on role
            redirectBasedOnRole(response, user);
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username); // Keep username in form
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * Redirect user based on their role
     */
    private void redirectBasedOnRole(HttpServletResponse response, User user) throws IOException {
        if (user.isAdmin()) {
            response.sendRedirect(getServletContext().getContextPath() + "/admin/dashboard.jsp");
        } else {
            response.sendRedirect(getServletContext().getContextPath() + "/user/dashboard.jsp");
        }
    }
}
