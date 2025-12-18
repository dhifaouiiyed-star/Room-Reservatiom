<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.UserRole" %>
            <%@ page import="java.util.List" %>
                <%@ page import="java.time.format.DateTimeFormatter" %>
                    <% User currentUser=(User) session.getAttribute("user"); if (currentUser==null ||
                        !currentUser.isAdmin()) { response.sendRedirect(request.getContextPath() + "/login" ); return; }
                        List<User> users = (List<User>) request.getAttribute("users");
                            Long currentUserId = (Long) request.getAttribute("currentUserId");

                            String success = (String) session.getAttribute("success");
                            String error = (String) session.getAttribute("error");
                            if (success != null) session.removeAttribute("success");
                            if (error != null) session.removeAttribute("error");

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                            %>
                            <!DOCTYPE html>
                            <html lang="en">

                            <head>
                                <meta charset="UTF-8">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <title>Manage Users - Admin Panel</title>
                                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
                                <link rel="preconnect" href="https://fonts.googleapis.com">
                                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                                <link
                                    href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
                                    rel="stylesheet">
                            </head>

                            <body>
                                <div class="admin-container">
                                    <!-- Sidebar -->
                                    <aside class="admin-sidebar">
                                        <div class="sidebar-header">
                                            <div class="sidebar-logo">🏢</div>
                                            <div class="sidebar-title">Admin Panel</div>
                                            <div class="sidebar-user">
                                                <%= currentUser.getUsername() %>
                                            </div>
                                        </div>
                                        <nav>
                                            <ul class="sidebar-nav">
                                                <li><a href="${pageContext.request.contextPath}/admin/dashboard">
                                                        <span class="sidebar-nav-icon">📊</span> Dashboard
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/admin/users"
                                                        class="active">
                                                        <span class="sidebar-nav-icon">👥</span> Manage Users
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/admin/rooms">
                                                        <span class="sidebar-nav-icon">🚪</span> Manage Rooms
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/admin/reservations">
                                                        <span class="sidebar-nav-icon">📅</span> All Reservations
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/logout">
                                                        <span class="sidebar-nav-icon">🚪</span> Logout
                                                    </a></li>
                                            </ul>
                                        </nav>
                                    </aside>

                                    <!-- Main Content -->
                                    <main class="admin-main">
                                        <div class="admin-header">
                                            <h1>User Management</h1>
                                            <p>Manage user accounts and permissions</p>
                                        </div>

                                        <% if (success !=null) { %>
                                            <div class="alert alert-success">
                                                <%= success %>
                                            </div>
                                            <% } %>
                                                <% if (error !=null) { %>
                                                    <div class="alert alert-error">
                                                        <%= error %>
                                                    </div>
                                                    <% } %>

                                                        <div class="content-card">
                                                            <h2>All Users (<%= users.size() %>)</h2>
                                                            <table class="data-table">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Username</th>
                                                                        <th>Email</th>
                                                                        <th>Role</th>
                                                                        <th>Created</th>
                                                                        <th>Actions</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <% for (User user : users) { boolean
                                                                        isCurrentUser=user.getId().equals(currentUserId);
                                                                        %>
                                                                        <tr>
                                                                            <td><strong>
                                                                                    <%= user.getUsername() %>
                                                                                </strong></td>
                                                                            <td>
                                                                                <%= user.getEmail() %>
                                                                            </td>
                                                                            <td>
                                                                                <% if (user.isAdmin()) { %>
                                                                                    <span
                                                                                        class="badge badge-admin">ADMIN</span>
                                                                                    <% } else { %>
                                                                                        <span
                                                                                            class="badge badge-info">USER</span>
                                                                                        <% } %>
                                                                            </td>
                                                                            <td>
                                                                                <%= user.getCreatedAt().format(formatter)
                                                                                    %>
                                                                            </td>
                                                                            <td>
                                                                                <div class="btn-group">
                                                                                    <% if (!isCurrentUser) { %>
                                                                                        <form method="POST"
                                                                                            action="${pageContext.request.contextPath}/admin/users"
                                                                                            style="display: inline;">
                                                                                            <input type="hidden"
                                                                                                name="action"
                                                                                                value="toggleRole">
                                                                                            <input type="hidden"
                                                                                                name="userId"
                                                                                                value="<%= user.getId() %>">
                                                                                            <button type="submit"
                                                                                                class="btn btn-sm btn-secondary">
                                                                                                <%= user.isAdmin()
                                                                                                    ? "Demote to User"
                                                                                                    : "Promote to Admin"
                                                                                                    %>
                                                                                            </button>
                                                                                        </form>

                                                                                        <form method="POST"
                                                                                            action="${pageContext.request.contextPath}/admin/users"
                                                                                            onsubmit="return confirm('Are you sure you want to delete this user? This will also delete all their reservations.');"
                                                                                            style="display: inline;">
                                                                                            <input type="hidden"
                                                                                                name="action"
                                                                                                value="delete">
                                                                                            <input type="hidden"
                                                                                                name="userId"
                                                                                                value="<%= user.getId() %>">
                                                                                            <button type="submit"
                                                                                                class="btn btn-sm btn-danger">Delete</button>
                                                                                        </form>
                                                                                        <% } else { %>
                                                                                            <span
                                                                                                class="badge badge-info">You</span>
                                                                                            <% } %>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                        <% } %>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                    </main>
                                </div>
                            </body>

                            </html>