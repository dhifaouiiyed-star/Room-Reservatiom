<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Reservation" %>
            <%@ page import="java.util.List" %>
                <%@ page import="java.time.format.DateTimeFormatter" %>
                    <% User user=(User) session.getAttribute("user"); if (user==null) {
                        response.sendRedirect(request.getContextPath() + "/login" ); return; } List<Reservation>
                        upcomingReservations = (List<Reservation>) request.getAttribute("upcomingReservations");
                            List<Reservation> pastReservations = (List<Reservation>)
                                    request.getAttribute("pastReservations");
                                    Long totalReservationsObj = (Long) request.getAttribute("totalReservations");

                                    long totalReservations = totalReservationsObj != null ? totalReservationsObj : 0L;
                                    if (upcomingReservations == null) {
                                    upcomingReservations = new java.util.ArrayList<>();
                                        }
                                        if (pastReservations == null) {
                                        pastReservations = new java.util.ArrayList<>();
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
                                                <title>My Dashboard - Room Reservation System</title>
                                                <link rel="stylesheet"
                                                    href="${pageContext.request.contextPath}/css/user.css">
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
                                                        <h1>Welcome, <%= user.getUsername() %>! 👋</h1>
                                                        <p>Manage your room reservations</p>
                                                    </header>

                                                    <!-- Navigation -->
                                                    <nav class="user-nav">
                                                        <a href="${pageContext.request.contextPath}/user/dashboard"
                                                            class="nav-link active">📊 Dashboard</a>
                                                        <a href="${pageContext.request.contextPath}/user/rooms"
                                                            class="nav-link">🚪 Browse Rooms</a>
                                                        <a href="${pageContext.request.contextPath}/logout"
                                                            class="nav-link">🚪
                                                            Logout</a>
                                                    </nav>

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

                                                                    <!-- Statistics -->
                                                                    <div class="section">
                                                                        <div class="card">
                                                                            <div class="card-header">
                                                                                <h2 class="card-title">📊 Your
                                                                                    Statistics</h2>
                                                                            </div>
                                                                            <p
                                                                                style="font-size: 2rem; font-weight: 700; color: var(--primary-color);">
                                                                                <%= totalReservations %> <span
                                                                                        style="font-size: 1rem; color: var(--text-secondary); font-weight: 400;">total
                                                                                        reservations</span>
                                                                            </p>
                                                                        </div>
                                                                    </div>

                                                                    <!-- Upcoming Reservations -->
                                                                    <div class="section">
                                                                        <div class="section-header">
                                                                            <h2 class="section-title">Upcoming
                                                                                Reservations</h2>
                                                                            <a href="${pageContext.request.contextPath}/user/rooms"
                                                                                class="btn btn-primary">➕ New
                                                                                Reservation</a>
                                                                        </div>

                                                                        <% if (upcomingReservations.isEmpty()) { %>
                                                                            <div class="empty-state">
                                                                                <div class="empty-state-icon">📅</div>
                                                                                <div class="empty-state-title">No
                                                                                    upcoming
                                                                                    reservations</div>
                                                                                <p class="empty-state-text">You don't
                                                                                    have any
                                                                                    reservations scheduled. Browse rooms
                                                                                    to make
                                                                                    a booking!</p>
                                                                                <a href="${pageContext.request.contextPath}/user/rooms"
                                                                                    class="btn btn-primary">Browse
                                                                                    Rooms</a>
                                                                            </div>
                                                                            <% } else { %>
                                                                                <div class="content-grid">
                                                                                    <% for (Reservation reservation :
                                                                                        upcomingReservations) { %>
                                                                                        <div
                                                                                            class="card reservation-card">
                                                                                            <div
                                                                                                class="reservation-header">
                                                                                                <div>
                                                                                                    <h3
                                                                                                        class="card-title">
                                                                                                        <%= reservation.getRoom().getName()
                                                                                                            %>
                                                                                                    </h3>
                                                                                                    <p
                                                                                                        class="reservation-date">
                                                                                                        Created <%=
                                                                                                            reservation.getCreatedAt().format(formatter)
                                                                                                            %>
                                                                                                    </p>
                                                                                                </div>
                                                                                                <span
                                                                                                    class="status-badge status-<%= reservation.isActive() ? "active" : "cancelled" %>">
                                                                                                    <%= reservation.getStatus()
                                                                                                        %>
                                                                                                </span>
                                                                                            </div>

                                                                                            <div
                                                                                                class="reservation-details">
                                                                                                <div
                                                                                                    class="reservation-detail">
                                                                                                    <div
                                                                                                        class="reservation-detail-label">
                                                                                                        Start</div>
                                                                                                    <div
                                                                                                        class="reservation-detail-value">
                                                                                                        <%= reservation.getStartDateTime().format(formatter)
                                                                                                            %>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div
                                                                                                    class="reservation-detail">
                                                                                                    <div
                                                                                                        class="reservation-detail-label">
                                                                                                        End</div>
                                                                                                    <div
                                                                                                        class="reservation-detail-value">
                                                                                                        <%= reservation.getEndDateTime().format(formatter)
                                                                                                            %>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div
                                                                                                    class="reservation-detail">
                                                                                                    <div
                                                                                                        class="reservation-detail-label">
                                                                                                        Capacity</div>
                                                                                                    <div
                                                                                                        class="reservation-detail-value">
                                                                                                        <%= reservation.getRoom().getCapacity()
                                                                                                            %> people
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div
                                                                                                    class="reservation-detail">
                                                                                                    <div
                                                                                                        class="reservation-detail-label">
                                                                                                        Description
                                                                                                    </div>
                                                                                                    <div
                                                                                                        class="reservation-detail-value">
                                                                                                        <%= reservation.getRoom().getDescription()
                                                                                                            !=null ?
                                                                                                            reservation.getRoom().getDescription()
                                                                                                            : "No description"
                                                                                                            %>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>

                                                                                            <% if
                                                                                                (reservation.isActive()
                                                                                                &&
                                                                                                !reservation.isPast()) {
                                                                                                %>
                                                                                                <form method="POST"
                                                                                                    action="${pageContext.request.contextPath}/user/cancel-reservation"
                                                                                                    onsubmit="return confirm('Are you sure you want to cancel this reservation?');">
                                                                                                    <input type="hidden"
                                                                                                        name="reservationId"
                                                                                                        value="<%= reservation.getId() %>">
                                                                                                    <button
                                                                                                        type="submit"
                                                                                                        class="btn btn-danger btn-full">Cancel
                                                                                                        Reservation</button>
                                                                                                </form>
                                                                                                <% } %>
                                                                                        </div>
                                                                                        <% } %>
                                                                                </div>
                                                                                <% } %>
                                                                    </div>

                                                                    <!-- Past Reservations -->
                                                                    <% if (!pastReservations.isEmpty()) { %>
                                                                        <div class="section">
                                                                            <div class="section-header">
                                                                                <h2 class="section-title">Past
                                                                                    Reservations</h2>
                                                                            </div>
                                                                            <div class="content-grid">
                                                                                <% for (Reservation reservation :
                                                                                    pastReservations) { %>
                                                                                    <div class="card reservation-card"
                                                                                        style="opacity: 0.7;">
                                                                                        <div class="reservation-header">
                                                                                            <div>
                                                                                                <h3 class="card-title">
                                                                                                    <%= reservation.getRoom().getName()
                                                                                                        %>
                                                                                                </h3>
                                                                                                <p
                                                                                                    class="reservation-date">
                                                                                                    <%= reservation.getStartDateTime().format(formatter)
                                                                                                        %>
                                                                                                </p>
                                                                                            </div>
                                                                                            <span
                                                                                                class="status-badge status-<%= reservation.isActive() ? "active" : "cancelled" %>">
                                                                                                <%= reservation.getStatus()
                                                                                                    %>
                                                                                            </span>
                                                                                        </div>
                                                                                    </div>
                                                                                    <% } %>
                                                                            </div>
                                                                        </div>
                                                                        <% } %>
                                                </div>
                                            </body>

                                            </html>