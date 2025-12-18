package org.dhifaoui.projetjee.Servlets.admin;

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


@WebServlet("/admin/reservations")
public class AdminReservationServlet extends HttpServlet {

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

        User currentUser = (User) session.getAttribute("user");

        // Check if user is admin
        if (!currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        // Get filter parameters
        String filterType = request.getParameter("filter");
        String filterId = request.getParameter("filterId");

        List<Reservation> reservations;

        if ("room".equals(filterType) && filterId != null) {
            // Filter by room
            reservations = reservationService.getRoomReservations(Long.parseLong(filterId));
        } else if ("user".equals(filterType) && filterId != null) {
            // Filter by user
            reservations = reservationService.getUserReservations(Long.parseLong(filterId));
        } else if ("upcoming".equals(filterType)) {
            // Show only upcoming
            reservations = reservationService.getUpcomingReservations();
        } else {
            // Show all reservations
            reservations = reservationService.getAllReservations();
        }

        request.setAttribute("reservations", reservations);
        request.setAttribute("currentFilter", filterType);

        // Forward to JSP
        request.getRequestDispatcher("/admin/reservations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in and is admin
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        if (!currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        String action = request.getParameter("action");
        String reservationIdStr = request.getParameter("reservationId");

        if (action == null || reservationIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
            return;
        }

        try {
            Long reservationId = Long.parseLong(reservationIdStr);

            if ("cancel".equals(action)) {
                reservationService.adminCancelReservation(reservationId);
                session.setAttribute("success", "Reservation cancelled successfully");
            } else {
                session.setAttribute("error", "Invalid action");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/reservations");
    }
}
