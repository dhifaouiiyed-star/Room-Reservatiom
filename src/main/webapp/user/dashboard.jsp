<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <% User user=(User) session.getAttribute("user"); if (user==null) {
            response.sendRedirect(request.getContextPath() + "/login" ); return; } %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>User Dashboard - Room Reservation System</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
            </head>

            <body>
                <div class="auth-container">
                    <div class="auth-card">
                        <div class="auth-header">
                            <div class="auth-logo">👤</div>
                            <h1 class="auth-title">User Dashboard</h1>
                            <p class="auth-subtitle">Welcome, <%= user.getUsername() %>!</p>
                        </div>

                        <div class="alert alert-success">
                            ✅ You are logged in as a regular user.
                        </div>

                        <div style="text-align: center; margin-top: 24px;">
                            <p style="color: #6b7280; margin-bottom: 16px;">
                                Dashboard features coming soon...
                            </p>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-primary"
                                style="display: inline-block; text-decoration: none;">
                                Logout
                            </a>
                        </div>
                    </div>
                </div>
            </body>

            </html>