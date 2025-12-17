<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Reservation" %>
            <%@ page import="java.util.List" %>
                <%@ page import="java.time.format.DateTimeFormatter" %>
                    <% User user=(User) session.getAttribute("user"); if (user==null || !user.isAdmin()) {
                        response.sendRedirect(request.getContextPath() + "/login" ); return; } // Handle null attributes
                        with defaults Long totalUsersObj=(Long) request.getAttribute("totalUsers"); Long
                        totalRoomsObj=(Long) request.getAttribute("totalRooms"); Long activeReservationsObj=(Long)
                        request.getAttribute("activeReservations"); List<Reservation> upcomingReservations = (List
                        <Reservation>) request.getAttribute("upcomingReservations");

                            long totalUsers = totalUsersObj != null ? totalUsersObj : 0L;
                            long totalRooms = totalRoomsObj != null ? totalRoomsObj : 0L;
                            long activeReservations = activeReservationsObj != null ? activeReservationsObj : 0L;
                            if (upcomingReservations == null) {
                            upcomingReservations = new java.util.ArrayList<>();
                                }

                                String success = (String) session.getAttribute("success");
                                String error = (String) session.getAttribute("error");
                                if (success != null) session.removeAttribute("success");
                                if (error != null) session.removeAttribute("error");

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
                                %>
                                <!DOCTYPE html>
                                <html lang="en">

                                <head>
                                    <meta charset="UTF-8">
                                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                    <title>Admin Dashboard - Room Reservation System</title>
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
                                                    <%= user.getUsername() %>
                                                </div>
                                            </div>
                                            <nav>
                                                <ul class="sidebar-nav">
                                                    <li><a href="${pageContext.request.contextPath}/admin/dashboard"
                                                            class="active">
                                                            <span class="sidebar-nav-icon">📊</span> Dashboard
                                                        </a></li>
                                                    <li><a href="${pageContext.request.contextPath}/admin/users">
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
                                                <h1>Dashboard Overview</h1>
                                                <p>Welcome back, <%= user.getUsername() %>! Here's what's happening
                                                        today.</p>
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

                                                            <!-- Statistics Cards -->
                                                            <div class="stats-grid">
                                                                <div class="stat-card">
                                                                    <div class="stat-icon blue">👥</div>
                                                                    <div class="stat-label">Total Users</div>
                                                                    <div class="stat-value">
                                                                        <%= totalUsers %>
                                                                    </div>
                                                                </div>
                                                                <div class="stat-card">
                                                                    <div class="stat-icon green">🚪</div>
                                                                    <div class="stat-label">Total Rooms</div>
                                                                    <div class="stat-value">
                                                                        <%= totalRooms %>
                                                                    </div>
                                                                </div>
                                                                <div class="stat-card">
                                                                    <div class="stat-icon purple">📅</div>
                                                                    <div class="stat-label">Active Reservations</div>
                                                                    <div class="stat-value">
                                                                        <%= activeReservations %>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <!-- Upcoming Reservations -->
                                                            <div class="content-card">
                                                                <h2>Upcoming Reservations</h2>
                                                                <% if (upcomingReservations.isEmpty()) { %>
                                                                    <div class="empty-state">
                                                                        <div class="empty-state-icon">📅</div>
                                                                        <p>No upcoming reservations</p>
                                                                    </div>
                                                                    <% } else { %>
                                                                        <table class="data-table">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>User</th>
                                                                                    <th>Room</th>
                                                                                    <th>Start Time</th>
                                                                                    <th>End Time</th>
                                                                                    <th>Status</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <% for (Reservation reservation :
                                                                                    upcomingReservations) { %>
                                                                                    <tr>
                                                                                        <td>
                                                                                            <%= reservation.getUser().getUsername()
                                                                                                %>
                                                                                        </td>
                                                                                        <td>
                                                                                            <%= reservation.getRoom().getName()
                                                                                                %>
                                                                                        </td>
                                                                                        <td>
                                                                                            <%= reservation.getStartDateTime().format(formatter)
                                                                                                %>
                                                                                        </td>
                                                                                        <td>
                                                                                            <%= reservation.getEndDateTime().format(formatter)
                                                                                                %>
                                                                                        </td>
                                                                                        <td>
                                                                                            <span
                                                                                                class="badge badge-<%= reservation.isActive() ? "
                                                                                                success" : "danger" %>">
                                                                                                <%= reservation.getStatus()
                                                                                                    %>
                                                                                            </span>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <% } %>
                                                                            </tbody>
                                                                        </table>
                                                                        <% } %>
                                                            </div>

                                                            <!-- Quick Actions -->
                                                            <div class="content-card">
                                                                <h2>Quick Actions</h2>
                                                                <div class="btn-group">
                                                                    <a href="${pageContext.request.contextPath}/admin/rooms"
                                                                        class="btn btn-primary">
                                                                        ➕ Add New Room
                                                                    </a>
                                                                    <a href="${pageContext.request.contextPath}/admin/users"
                                                                        class="btn btn-secondary">
                                                                        👥 Manage Users
                                                                    </a>
                                                                    <a href="${pageContext.request.contextPath}/admin/reservations"
                                                                        class="btn btn-secondary">
                                                                        📋 View All Reservations
                                                                    </a>
                                                                </div>
                                                            </div>
                                        </main>
                                    </div>
                                </body>

                                </html>