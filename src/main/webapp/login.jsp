<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.dhifaoui.projetjee.entities.User" %>
<%
    // Check if user is already logged in
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser != null) {
        if (loggedInUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        }
        return;
    }

    String error = (String) request.getAttribute("error");
    String username = (String) request.getAttribute("username");
    String successMessage = (String) session.getAttribute("successMessage");
    
    // Clear success message after displaying
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Room Reservation System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <div class="auth-logo">🏢</div>
                <h1 class="auth-title">Welcome Back</h1>
                <p class="auth-subtitle">Sign in to manage your room reservations</p>
            </div>

            <% if (successMessage != null) { %>
                <div class="alert alert-success" role="alert">
                    <%= successMessage %>
                </div>
            <% } %>

            <% if (error != null) { %>
                <div class="alert alert-error" role="alert">
                    <%= error %>
                </div>
            <% } %>

            <form class="auth-form" method="POST" action="${pageContext.request.contextPath}/login" onsubmit="return validateLoginForm()">
                <div class="form-group">
                    <label for="username" class="form-label">Username</label>
                    <input 
                        type="text" 
                        id="username" 
                        name="username" 
                        class="form-input" 
                        placeholder="Enter your username"
                        value="<%= username != null ? username : "" %>"
                        required
                        autocomplete="username"
                    >
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        class="form-input" 
                        placeholder="Enter your password"
                        required
                        autocomplete="current-password"
                    >
                </div>

                <div class="form-group">
                    <div class="form-checkbox-group">
                        <input 
                            type="checkbox" 
                            id="rememberMe" 
                            name="rememberMe" 
                            class="form-checkbox"
                            value="true"
                        >
                        <label for="rememberMe" class="form-checkbox-label">
                            Remember me
                        </label>
                    </div>
                </div>

                <button type="submit" class="btn-primary">
                    Sign In
                </button>
            </form>

            <div class="auth-footer">
                Don't have an account? 
                <a href="${pageContext.request.contextPath}/signup" class="auth-link">Sign up</a>
            </div>
        </div>
    </div>

    <script>
        function validateLoginForm() {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;

            if (username === '') {
                alert('Please enter your username');
                return false;
            }

            if (password === '') {
                alert('Please enter your password');
                return false;
            }

            return true;
        }

        // Auto-hide success/error messages after 5 seconds
        window.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    alert.style.transition = 'opacity 0.5s';
                    alert.style.opacity = '0';
                    setTimeout(function() {
                        alert.style.display = 'none';
                    }, 500);
                }, 5000);
            });
        });
    </script>
</body>
</html>
