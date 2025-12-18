package org.dhifaoui.projetjee.Servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.ReservationService;

import java.io.IOException;

/**
 * Cancel Reservation Servlet
 * Allows users to cancel their own reservations
 */
@WebServlet("/user/cancel-reservation")
public class CancelReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String reservationIdStr = request.getParameter("reservationId");

        if (reservationIdStr == null) {
            session.setAttribute("error", "Reservation ID is required");
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        try {
            Long reservationId = Long.parseLong(reservationIdStr);

            // Cancel reservation (service validates ownership and rules)
            reservationService.cancelReservation(reservationId, user.getId());

            session.setAttribute("success", "Reservation cancelled successfully");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("error", "Error cancelling reservation: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/user/dashboard");
    }
}
