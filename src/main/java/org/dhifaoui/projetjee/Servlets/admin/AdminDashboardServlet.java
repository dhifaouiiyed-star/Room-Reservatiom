package org.dhifaoui.projetjee.Servlets.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.ReservationService;
import org.dhifaoui.projetjee.service.RoomService;
import org.dhifaoui.projetjee.service.UserManagementService;

import java.io.IOException;


@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final RoomService roomService = new RoomService();
    private final UserManagementService userManagementService = new UserManagementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Check if user is admin
        if (!user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        // Get statistics
        long totalUsers = userManagementService.getTotalUserCount();
        long totalRooms = roomService.getTotalRoomCount();
        long activeReservations = reservationService.getTotalActiveReservations();

        // Get recent reservations (upcoming)
        var upcomingReservations = reservationService.getUpcomingReservations();

        // Set attributes for JSP
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalRooms", totalRooms);
        request.setAttribute("activeReservations", activeReservations);
        request.setAttribute("upcomingReservations", upcomingReservations);

        // Forward to JSP
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
