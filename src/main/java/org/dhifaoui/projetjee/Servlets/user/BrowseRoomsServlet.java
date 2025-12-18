package org.dhifaoui.projetjee.Servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.service.RoomService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/user/rooms")
public class BrowseRoomsServlet extends HttpServlet {

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

        // Get filter parameters
        String capacityStr = request.getParameter("minCapacity");
        String availableOnlyStr = request.getParameter("availableOnly");

        List<Room> rooms = roomService.getAllRooms();

        // Apply filters
        if (capacityStr != null && !capacityStr.trim().isEmpty()) {
            try {
                int minCapacity = Integer.parseInt(capacityStr);
                rooms = rooms.stream()
                        .filter(room -> room.getCapacity() >= minCapacity)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Ignore invalid capacity filter
            }
        }

        if ("true".equals(availableOnlyStr)) {
            rooms = rooms.stream()
                    .filter(Room::getIsAvailable)
                    .collect(Collectors.toList());
        }

        // Set attributes for JSP
        request.setAttribute("rooms", rooms);
        request.setAttribute("minCapacity", capacityStr);
        request.setAttribute("availableOnly", availableOnlyStr);

        // Forward to JSP
        request.getRequestDispatcher("/user/rooms.jsp").forward(request, response);
    }
}
