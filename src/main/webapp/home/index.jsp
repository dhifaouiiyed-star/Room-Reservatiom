<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.dhifaoui.projetjee.entities.User" %>
<%@ page import="org.dhifaoui.projetjee.entities.Room" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Room Reservation System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>

<body>
<nav class="navbar">
    <div class="nav-links">
        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard.jsp">Dashboard</a>
    </div>
</nav>

<div class="home-content">
    <h1 class="home-title">Reserve The Room You Want</h1>
    <h4 class="home-subtitle">Now with a click</h4>

    <div class="home-sections">

        <% if (rooms != null && !rooms.isEmpty()) { %>

        <% for (Room room : rooms) { %>

        <!-- REAL CARD STRUCTURE -->
        <div class="home-card">

            <div class="home-card-title">
                <%= room.getName() %>
            </div>

            <div class="home-card-desc">
                <%= room.getDescription() %>
            </div>

            <div class="home-card-content">
                <p>Capacity: <%= room.getCapacity() %></p>
                <p>Available: <%= room.getIsAvailable() %></p>
            </div>

        </div>

        <% } %>

        <% } else { %>

        <h1 class="home-subtitle">There Are No Rooms</h1>

        <% } %>

    </div>
</div>

</body>
</html>
