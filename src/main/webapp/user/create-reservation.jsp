<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Room" %>
            <%@ page import="java.time.LocalDateTime" %>
                <%@ page import="java.time.format.DateTimeFormatter" %>
                    <% User user=(User) session.getAttribute("user"); if (user==null) {
                        response.sendRedirect(request.getContextPath() + "/login" ); return; } Room room=(Room)
                        request.getAttribute("room"); if (room==null) { response.sendRedirect(request.getContextPath()
                        + "/user/rooms" ); return; } String error=(String) session.getAttribute("error"); if (error
                        !=null) session.removeAttribute("error"); // Generate default dates (now + 1 hour and now + 2 hours)
                        LocalDateTime now=LocalDateTime.now(); LocalDateTime
                        defaultStart=now.plusHours(1).withMinute(0).withSecond(0).withNano(0); LocalDateTime
                        defaultEnd=defaultStart.plusHours(1); DateTimeFormatter
                        htmlFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); %>
                        <!DOCTYPE html>
                        <html lang="en">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Book <%= room.getName() %> - Room Reservation System</title>
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
                                    <h1>Book <%= room.getName() %> 📅</h1>
                                    <p>Create a new reservation</p>
                                </header>

                                <!-- Navigation -->
                                <nav class="user-nav">
                                    <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-link">📊
                                        Dashboard</a>
                                    <a href="${pageContext.request.contextPath}/user/rooms" class="nav-link">🚪 Browse
                                        Rooms</a>
                                    <a href="${pageContext.request.contextPath}/logout" class="nav-link">🚪 Logout</a>
                                </nav>

                                <% if (error !=null) { %>
                                    <div class="alert alert-error">
                                        <%= error %>
                                    </div>
                                    <% } %>

                                        <!-- Room Info -->
                                        <div class="info-box">
                                            <div class="info-box-title">📍 Room Details</div>
                                            <div class="info-box-text">
                                                <strong>
                                                    <%= room.getName() %>
                                                </strong><br>
                                                Capacity: <%= room.getCapacity() %> people<br>
                                                    <% if (room.getDescription() !=null &&
                                                        !room.getDescription().trim().isEmpty()) { %>
                                                        <%= room.getDescription() %>
                                                            <% } %>
                                            </div>
                                        </div>

                                        <!-- Reservation Form -->
                                        <div class="form-container">
                                            <form method="POST"
                                                action="${pageContext.request.contextPath}/user/create-reservation"
                                                onsubmit="return validateForm()">
                                                <input type="hidden" name="roomId" value="<%= room.getId() %>">

                                                <div class="form-group">
                                                    <label for="startDateTime" class="form-label">Start Date & Time
                                                        *</label>
                                                    <input type="datetime-local" id="startDateTime" name="startDateTime"
                                                        class="form-input"
                                                        value="<%= defaultStart.format(htmlFormatter) %>" required>
                                                    <small
                                                        style="color: var(--text-secondary); font-size: 0.813rem; margin-top: 0.25rem; display: block;">
                                                        Select when your reservation should begin
                                                    </small>
                                                </div>

                                                <div class="form-group">
                                                    <label for="endDateTime" class="form-label">End Date & Time
                                                        *</label>
                                                    <input type="datetime-local" id="endDateTime" name="endDateTime"
                                                        class="form-input"
                                                        value="<%= defaultEnd.format(htmlFormatter) %>" required>
                                                    <small
                                                        style="color: var(--text-secondary); font-size: 0.813rem; margin-top: 0.25rem; display: block;">
                                                        Select when your reservation should end
                                                    </small>
                                                </div>

                                                <div class="info-box"
                                                    style="background: linear-gradient(135deg, #fef3c7, #fde68a);">
                                                    <div class="info-box-title" style="color: #92400e;">⚠️ Important
                                                        Rules</div>
                                                    <div class="info-box-text" style="color: #78350f;">
                                                        • End time must be after start time<br>
                                                        • You can only book one room at a time<br>
                                                        • The room must be available for the selected time slot<br>
                                                        • Reservations must be in the future
                                                    </div>
                                                </div>

                                                <div class="btn-group">
                                                    <button type="submit" class="btn btn-primary btn-full">
                                                        ✓ Create Reservation
                                                    </button>
                                                    <a href="${pageContext.request.contextPath}/user/rooms"
                                                        class="btn btn-secondary btn-full">
                                                        ← Back to Rooms
                                                    </a>
                                                </div>
                                            </form>
                                        </div>
                            </div>

                            <script>
                                function validateForm() {
                                    const startDateTime = document.getElementById('startDateTime').value;
                                    const endDateTime = document.getElementById('endDateTime').value;

                                    if (!startDateTime || !endDateTime) {
                                        alert('Please select both start and end times');
                                        return false;
                                    }

                                    const start = new Date(startDateTime);
                                    const end = new Date(endDateTime);
                                    const now = new Date();

                                    if (start < now) {
                                        alert('Start time must be in the future');
                                        return false;
                                    }

                                    if (end <= start) {
                                        alert('End time must be after start time');
                                        return false;
                                    }

                                    return true;
                                }

                                // Update minimum for end datetime when start changes
                                document.getElementById('startDateTime').addEventListener('change', function () {
                                    const startInput = this;
                                    const endInput = document.getElementById('endDateTime');

                                    if (startInput.value) {
                                        const start = new Date(startInput.value);
                                        start.setHours(start.getHours() + 1); // Add 1 hour minimum

                                        const year = start.getFullYear();
                                        const month = String(start.getMonth() + 1).padStart(2, '0');
                                        const day = String(start.getDate()).padStart(2, '0');
                                        const hours = String(start.getHours()).padStart(2, '0');
                                        const minutes = String(start.getMinutes()).padStart(2, '0');

                                        const minEnd = `${year}-${month}-${day}T${hours}:${minutes}`;
                                        endInput.min = minEnd;

                                        // If current end is before the new minimum, update it
                                        if (endInput.value < minEnd) {
                                            endInput.value = minEnd;
                                        }
                                    }
                                });
                            </script>
                        </body>

                        </html>