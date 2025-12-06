<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% String error=(String) request.getAttribute("error"); String username=(String) request.getAttribute("username");
        String email=(String) request.getAttribute("email"); %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Sign Up - Room Reservation System</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
                rel="stylesheet">
        </head>

        <body>
            <div class="auth-container">
                <div class="auth-card">
                    <div class="auth-header">
                        <div class="auth-logo">🏢</div>
                        <h1 class="auth-title">Create Account</h1>
                        <p class="auth-subtitle">Join us to start reserving rooms</p>
                    </div>

                    <% if (error !=null) { %>
                        <div class="alert alert-error" role="alert">
                            <%= error %>
                        </div>
                        <% } %>

                            <form class="auth-form" method="POST" action="${pageContext.request.contextPath}/signup"
                                onsubmit="return validateSignupForm()">
                                <div class="form-group">
                                    <label for="username" class="form-label">Username</label>
                                    <input type="text" id="username" name="username" class="form-input"
                                        placeholder="Choose a username" value="<%= username != null ? username : "" %>"
                                        required minlength="3" autocomplete="username">
                                </div>

                                <div class="form-group">
                                    <label for="email" class="form-label">Email Address</label>
                                    <input type="email" id="email" name="email" class="form-input"
                                        placeholder="your.email@example.com" value="<%= email != null ? email : "" %>"
                                        required autocomplete="email">
                                </div>

                                <div class="form-group">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" id="password" name="password" class="form-input"
                                        placeholder="Create a strong password" required minlength="6"
                                        autocomplete="new-password" oninput="checkPasswordStrength()">
                                    <div class="password-strength" id="passwordStrength" style="display: none;">
                                        <div class="password-strength-bar" id="strengthBar"></div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                                    <input type="password" id="confirmPassword" name="confirmPassword"
                                        class="form-input" placeholder="Re-enter your password" required minlength="6"
                                        autocomplete="new-password">
                                </div>

                                <div class="form-group">
                                    <label for="role" class="form-label">Account Type</label>
                                    <select id="role" name="role" class="form-select" required>
                                        <option value="USER">Regular User</option>
                                        <option value="ADMIN">Administrator</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn-primary">
                                    Create Account
                                </button>
                            </form>

                            <div class="auth-footer">
                                Already have an account?
                                <a href="${pageContext.request.contextPath}/login" class="auth-link">Sign in</a>
                            </div>
                </div>
            </div>

            <script>
                function validateSignupForm() {
                    const username = document.getElementById('username').value.trim();
                    const email = document.getElementById('email').value.trim();
                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;

                    if (username.length < 3) {
                        alert('Username must be at least 3 characters long');
                        return false;
                    }

                    if (!isValidEmail(email)) {
                        alert('Please enter a valid email address');
                        return false;
                    }

                    if (password.length < 6) {
                        alert('Password must be at least 6 characters long');
                        return false;
                    }

                    if (password !== confirmPassword) {
                        alert('Passwords do not match');
                        return false;
                    }

                    return true;
                }

                function isValidEmail(email) {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    return emailRegex.test(email);
                }

                function checkPasswordStrength() {
                    const password = document.getElementById('password').value;
                    const strengthIndicator = document.getElementById('passwordStrength');
                    const strengthBar = document.getElementById('strengthBar');

                    if (password.length === 0) {
                        strengthIndicator.style.display = 'none';
                        return;
                    }

                    strengthIndicator.style.display = 'block';

                    let strength = 0;

                    // Length check
                    if (password.length >= 6) strength++;
                    if (password.length >= 10) strength++;

                    // Character variety checks
                    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
                    if (/[0-9]/.test(password)) strength++;
                    if (/[^a-zA-Z0-9]/.test(password)) strength++;

                    // Update strength bar
                    strengthBar.className = 'password-strength-bar';
                    if (strength <= 2) {
                        strengthBar.classList.add('strength-weak');
                    } else if (strength <= 3) {
                        strengthBar.classList.add('strength-medium');
                    } else {
                        strengthBar.classList.add('strength-strong');
                    }
                }

                // Auto-hide error messages after 5 seconds
                window.addEventListener('DOMContentLoaded', function () {
                    const alerts = document.querySelectorAll('.alert');
                    alerts.forEach(function (alert) {
                        setTimeout(function () {
                            alert.style.transition = 'opacity 0.5s';
                            alert.style.opacity = '0';
                            setTimeout(function () {
                                alert.style.display = 'none';
                            }, 500);
                        }, 5000);
                    });
                });
            </script>
        </body>

        </html>