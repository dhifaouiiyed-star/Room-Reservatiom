<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Reservation" %>
            <%@ page import="java.util.List" %>
                <%@ page import="java.time.format.DateTimeFormatter" %>
                    <% User currentUser=(User) session.getAttribute("user"); if (currentUser==null ||
                        !currentUser.isAdmin()) { response.sendRedirect(request.getContextPath() + "/login" ); return; }
                        List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                            String currentFilter = (String) request.getAttribute("currentFilter");

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
                                <title>All Reservations - Admin Panel</title>
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
                                                <li><a href="${pageContext.request.contextPath}/admin/users">
                                                        <span class="sidebar-nav-icon">👥</span> Manage Users
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/admin/rooms">
                                                        <span class="sidebar-nav-icon">🚪</span> Manage Rooms
                                                    </a></li>
                                                <li><a href="${pageContext.request.contextPath}/admin/reservations"
                                                        class="active">
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
                                            <h1>All Reservations</h1>
                                            <p>View and manage all room reservations</p>
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

                                                        <!-- Filters -->
                                                        <div class="content-card">
                                                            <h2>Filter Reservations</h2>
                                                            <div class="btn-group">
                                                                <a href="${pageContext.request.contextPath}/admin/reservations"
                                                                    class="btn btn-sm <%= currentFilter == null ? "
                                                                    btn-primary" : "btn-secondary" %>">
                                                                    All Reservations
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/admin/reservations?filter=upcoming"
                                                                    class="btn btn-sm <%= "
                                                                    upcoming".equals(currentFilter) ? "btn-primary"
                                                                    : "btn-secondary" %>">
                                                                    Upcoming Only
                                                                </a>
                                                            </div>
                                                        </div>

                                                        <!-- Reservations List -->
                                                        <div class="content-card">
                                                            <h2>Reservations (<%= reservations.size() %>)</h2>
                                                            <% if (reservations.isEmpty()) { %>
                                                                <div class="empty-state">
                                                                    <div class="empty-state-icon">📅</div>
                                                                    <p>No reservations found</p>
                                                                </div>
                                                                <% } else { %>
                                                                    <table class="data-table">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>ID</th>
                                                                                <th>User</th>
                                                                                <th>Room</th>
                                                                                <th>Start Time</th>
                                                                                <th>End Time</th>
                                                                                <th>Status</th>
                                                                                <th>Actions</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <% for (Reservation reservation :
                                                                                reservations) { %>
                                                                                <tr>
                                                                                    <td>#<%= reservation.getId() %>
                                                                                    </td>
                                                                                    <td><strong>
                                                                                            <%= reservation.getUser().getUsername()
                                                                                                %>
                                                                                        </strong></td>
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
                                                                                        <% if (reservation.isActive()) {
                                                                                            %>
                                                                                            <span
                                                                                                class="badge badge-success">ACTIVE</span>
                                                                                            <% } else { %>
                                                                                                <span
                                                                                                    class="badge badge-danger">CANCELLED</span>
                                                                                                <% } %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <% if (reservation.isActive()) {
                                                                                            %>
                                                                                            <form method="POST"
                                                                                                action="${pageContext.request.contextPath}/admin/reservations"
                                                                                                onsubmit="return confirm('Are you sure you want to cancel this reservation?');"
                                                                                                style="display: inline;">
                                                                                                <input type="hidden"
                                                                                                    name="action"
                                                                                                    value="cancel">
                                                                                                <input type="hidden"
                                                                                                    name="reservationId"
                                                                                                    value="<%= reservation.getId() %>">
                                                                                                <button type="submit"
                                                                                                    class="btn btn-sm btn-danger">Cancel</button>
                                                                                            </form>
                                                                                            <% } else { %>
                                                                                                <span
                                                                                                    class="badge badge-danger">Cancelled</span>
                                                                                                <% } %>
                                                                                    </td>
                                                                                </tr>
                                                                                <% } %>
                                                                        </tbody>
                                                                    </table>
                                                                    <% } %>
                                                        </div>
                                    </main>
                                </div>
                            </body>

                            </html>