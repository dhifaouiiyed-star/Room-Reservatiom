<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="org.dhifaoui.projetjee.entities.User" %>
        <%@ page import="org.dhifaoui.projetjee.entities.Room" %>
            <%@ page import="java.util.List" %>
                <% User currentUser=(User) session.getAttribute("user"); if (currentUser==null ||
                    !currentUser.isAdmin()) { response.sendRedirect(request.getContextPath() + "/login" ); return; }
                    List<Room> rooms = (List<Room>) request.getAttribute("rooms");

                        String success = (String) session.getAttribute("success");
                        String error = (String) session.getAttribute("error");
                        if (success != null) session.removeAttribute("success");
                        if (error != null) session.removeAttribute("error");
                        %>
                        <!DOCTYPE html>
                        <html lang="en">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Manage Rooms - Admin Panel</title>
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
                                            <li><a href="${pageContext.request.contextPath}/admin/rooms" class="active">
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
                                        <h1>Room Management</h1>
                                        <p>Create and manage rooms</p>
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

                                                    <!-- Create Room Form -->
                                                    <div class="content-card">
                                                        <h2>Create New Room</h2>
                                                        <form method="POST"
                                                            action="${pageContext.request.contextPath}/admin/rooms">
                                                            <input type="hidden" name="action" value="create">
                                                            <div class="form-group">
                                                                <label for="name" class="form-label">Room Name *</label>
                                                                <input type="text" id="name" name="name"
                                                                    class="form-input" required maxlength="100"
                                                                    placeholder="e.g., Conference Room A">
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="capacity" class="form-label">Capacity
                                                                    *</label>
                                                                <input type="number" id="capacity" name="capacity"
                                                                    class="form-input" required min="1"
                                                                    placeholder="e.g., 10">
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="description"
                                                                    class="form-label">Description</label>
                                                                <textarea id="description" name="description"
                                                                    class="form-textarea" maxlength="500"
                                                                    placeholder="Brief description of the room"></textarea>
                                                            </div>
                                                            <button type="submit" class="btn btn-primary">➕ Create
                                                                Room</button>
                                                        </form>
                                                    </div>

                                                    <!-- Rooms List -->
                                                    <div class="content-card">
                                                        <h2>All Rooms (<%= rooms.size() %>)</h2>
                                                        <% if (rooms.isEmpty()) { %>
                                                            <div class="empty-state">
                                                                <div class="empty-state-icon">🚪</div>
                                                                <p>No rooms available. Create one above!</p>
                                                            </div>
                                                            <% } else { %>
                                                                <table class="data-table">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>Name</th>
                                                                            <th>Capacity</th>
                                                                            <th>Description</th>
                                                                            <th>Status</th>
                                                                            <th>Actions</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <% for (Room room : rooms) { %>
                                                                            <tr>
                                                                                <td><strong>
                                                                                        <%= room.getName() %>
                                                                                    </strong></td>
                                                                                <td>
                                                                                    <%= room.getCapacity() %> people
                                                                                </td>
                                                                                <td>
                                                                                    <%= room.getDescription() !=null ?
                                                                                        room.getDescription()
                                                                                        : "No description" %>
                                                                                </td>
                                                                                <td>
                                                                                    <% if (room.getIsAvailable()) { %>
                                                                                        <span
                                                                                            class="badge badge-success">Available</span>
                                                                                        <% } else { %>
                                                                                            <span
                                                                                                class="badge badge-danger">Unavailable</span>
                                                                                            <% } %>
                                                                                </td>
                                                                                <td>
                                                                                    <div class="btn-group">
                                                                                        <form method="POST"
                                                                                            action="${pageContext.request.contextPath}/admin/rooms"
                                                                                            style="display: inline;">
                                                                                            <input type="hidden"
                                                                                                name="action"
                                                                                                value="toggleAvailability">
                                                                                            <input type="hidden"
                                                                                                name="roomId"
                                                                                                value="<%= room.getId() %>">
                                                                                            <button type="submit"
                                                                                                class="btn btn-sm btn-secondary">
                                                                                                <%= room.getIsAvailable()
                                                                                                    ? "Disable"
                                                                                                    : "Enable" %>
                                                                                            </button>
                                                                                        </form>

                                                                                        <form method="POST"
                                                                                            action="${pageContext.request.contextPath}/admin/rooms"
                                                                                            onsubmit="return confirm('Are you sure you want to delete this room? This will also delete all associated reservations.');"
                                                                                            style="display: inline;">
                                                                                            <input type="hidden"
                                                                                                name="action"
                                                                                                value="delete">
                                                                                            <input type="hidden"
                                                                                                name="roomId"
                                                                                                value="<%= room.getId() %>">
                                                                                            <button type="submit"
                                                                                                class="btn btn-sm btn-danger">Delete</button>
                                                                                        </form>
                                                                                    </div>
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