package org.dhifaoui.projetjee.Servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.Reservation;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.ReservationService;

import java.io.IOException;
import java.util.List;

/**
 * User Dashboard Servlet
 * Displays user's reservations and quick stats
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

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

        // Redirect admin to admin dashboard
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Get user's upcoming and past reservations
        List<Reservation> upcomingReservations = reservationService.getUserUpcomingReservations(user.getId());
        List<Reservation> pastReservations = reservationService.getUserPastReservations(user.getId());
        long totalReservations = reservationService.getUserReservationCount(user.getId());

        // Set attributes for JSP
        request.setAttribute("upcomingReservations", upcomingReservations);
        request.setAttribute("pastReservations", pastReservations);
        request.setAttribute("totalReservations", totalReservations);

        // Forward to JSP
        request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
    }
}
