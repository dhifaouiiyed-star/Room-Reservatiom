<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.dhifaoui.projetjee.entities.User" %>
<% User user=(User) session.getAttribute("user"); if (user==null) {
    response.sendRedirect(request.getContextPath() + "/login" ); return; } %>
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
    <h1 class="home-title">Reserve The room that you want </h1>
    <h4 class="home-subtitle">Now with a click</h4>
    <div class="home-sections">
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    <div class="home-card">
        <image src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdf01-FeKYQ4IwLul879E7QEJiIKjJojUZ0g&s" class="home-card-image"></image>

        <div class="home-card-content">
            <h3 class="home-card-title">Reserve a Room</h3>
            <p class="home-card-desc">
                Quickly reserve a study room with our smart scheduling system.
            </p>
        </div>
    </div>
    </div>

</div>
</body>

</html>