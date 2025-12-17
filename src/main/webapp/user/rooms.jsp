<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Room" %>
            <%@ page import="java.util.List" %>
                <% User user=(User) session.getAttribute("user"); if (user==null) {
                    response.sendRedirect(request.getContextPath() + "/login" ); return; } List<Room> rooms = (List
                    <Room>) request.getAttribute("rooms");
                        String minCapacity = (String) request.getAttribute("minCapacity");
                        String availableOnly = (String) request.getAttribute("availableOnly");
                        %>
                        <!DOCTYPE html>
                        <html lang="en">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Browse Rooms - Room Reservation System</title>
                            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
                            <link rel="preconnect" href="https://fonts.googleapis.com">
                            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                            <link
                                href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
                                rel="stylesheet">
                        </head>

                        <body>
                            <div class="user-container">
                                <!-- Header -->
                                <header class="user-header">
                                    <h1>Browse Rooms 🚪</h1>
                                    <p>Find the perfect room for your needs</p>
                                </header>

                                <!-- Navigation -->
                                <nav class="user-nav">
                                    <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-link">📊
                                        Dashboard</a>
                                    <a href="${pageContext.request.contextPath}/user/rooms" class="nav-link active">🚪
                                        Browse Rooms</a>
                                    <a href="${pageContext.request.contextPath}/logout" class="nav-link">🚪 Logout</a>
                                </nav>

                                <!-- Filter Bar -->
                                <div class="filter-bar">
                                    <form method="GET" action="${pageContext.request.contextPath}/user/rooms">
                                        <div class="filter-row">
                                            <div class="form-group" style="margin-bottom: 0;">
                                                <label for="minCapacity" class="form-label">Minimum Capacity</label>
                                                <input type="number" id="minCapacity" name="minCapacity"
                                                    class="form-input" min="1"
                                                    value="<%= minCapacity != null ? minCapacity : "" %>"
                                                    placeholder="e.g., 10">
                                            </div>
                                            <div class="form-group" style="margin-bottom: 0;">
                                                <label
                                                    style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer; margin-top: 1.75rem;">
                                                    <input type="checkbox" name="availableOnly" value="true" <%="true"
                                                        .equals(availableOnly) ? "checked" : "" %>>
                                                    <span style="font-weight: 600;">Available only</span>
                                                </label>
                                            </div>
                                            <div class="form-group" style="margin-bottom: 0;">
                                                <button type="submit" class="btn btn-primary"
                                                    style="margin-top: 1.75rem;">🔍 Filter</button>
                                                <a href="${pageContext.request.contextPath}/user/rooms"
                                                    class="btn btn-secondary" style="margin-top: 1.75rem;">Clear</a>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <!-- Rooms Grid -->
                                <div class="section">
                                    <div class="section-header">
                                        <h2 class="section-title">Available Rooms (<%= rooms.size() %>)</h2>
                                    </div>

                                    <% if (rooms.isEmpty()) { %>
                                        <div class="empty-state">
                                            <div class="empty-state-icon">🚪</div>
                                            <div class="empty-state-title">No rooms found</div>
                                            <p class="empty-state-text">Try adjusting your filters</p>
                                        </div>
                                        <% } else { %>
                                            <div class="content-grid">
                                                <% for (Room room : rooms) { %>
                                                    <div class="card room-card">
                                                        <div class="room-card-icon">🚪</div>
                                                        <h3 class="room-name">
                                                            <%= room.getName() %>
                                                        </h3>

                                                        <div class="room-info">
                                                            <div class="room-info-item">
                                                                <span>👥</span>
                                                                <span>
                                                                    <%= room.getCapacity() %> people
                                                                </span>
                                                            </div>
                                                            <div class="room-info-item">
                                                                <% if (room.getIsAvailable()) { %>
                                                                    <span
                                                                        class="status-badge status-available">Available</span>
                                                                    <% } else { %>
                                                                        <span
                                                                            class="status-badge status-unavailable">Unavailable</span>
                                                                        <% } %>
                                                            </div>
                                                        </div>

                                                        <% if (room.getDescription() !=null &&
                                                            !room.getDescription().trim().isEmpty()) { %>
                                                            <p class="room-description">
                                                                <%= room.getDescription() %>
                                                            </p>
                                                            <% } %>

                                                                <% if (room.getIsAvailable()) { %>
                                                                    <a href="${pageContext.request.contextPath}/user/create-reservation?roomId=<%= room.getId() %>"
                                                                        class="btn btn-primary btn-full">
                                                                        📅 Book This Room
                                                                    </a>
                                                                    <% } else { %>
                                                                        <button class="btn btn-secondary btn-full"
                                                                            disabled>Currently Unavailable</button>
                                                                        <% } %>
                                                    </div>
                                                    <% } %>
                                            </div>
                                            <% } %>
                                </div>
                            </div>
                        </body>

                        </html>