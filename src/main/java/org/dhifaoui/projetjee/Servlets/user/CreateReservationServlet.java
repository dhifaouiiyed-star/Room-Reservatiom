package org.dhifaoui.projetjee.Servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.ReservationService;
import org.dhifaoui.projetjee.service.RoomService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Create Reservation Servlet
 * Handles reservation creation with business rule validation
 */
@WebServlet("/user/create-reservation")
public class CreateReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String roomIdStr = request.getParameter("roomId");

        if (roomIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/user/rooms");
            return;
        }

        try {
            Long roomId = Long.parseLong(roomIdStr);
            Optional<Room> roomOpt = roomService.getRoomById(roomId);

            if (roomOpt.isEmpty()) {
                session.setAttribute("error", "Room not found");
                response.sendRedirect(request.getContextPath() + "/user/rooms");
                return;
            }

            request.setAttribute("room", roomOpt.get());
            request.getRequestDispatcher("/user/create-reservation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/rooms");
        }
    }

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

        String roomIdStr = request.getParameter("roomId");
        String startDateTimeStr = request.getParameter("startDateTime");
        String endDateTimeStr = request.getParameter("endDateTime");

        try {
            // Parse inputs
            Long roomId = Long.parseLong(roomIdStr);

            // Parse datetime (HTML datetime-local format: yyyy-MM-ddTHH:mm)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);

            // Create reservation (service handles all validation)
            reservationService.createReservation(user.getId(), roomId, startDateTime, endDateTime);

            session.setAttribute("success", "Reservation created successfully!");
            response.sendRedirect(request.getContextPath() + "/user/dashboard");

        } catch (DateTimeParseException e) {
            session.setAttribute("error", "Invalid date/time format");
            response.sendRedirect(request.getContextPath() + "/user/create-reservation?roomId=" + roomIdStr);
        } catch (IllegalArgumentException e) {
            // Business rule violation
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/create-reservation?roomId=" + roomIdStr);
        } catch (Exception e) {
            session.setAttribute("error", "Error creating reservation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/create-reservation?roomId=" + roomIdStr);
        }
    }
}
