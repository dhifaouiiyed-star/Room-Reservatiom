package org.dhifaoui.projetjee.Servlets.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.RoomService;

import java.io.IOException;
import java.util.List;


@WebServlet("/admin/rooms")
public class AdminRoomManagementServlet extends HttpServlet {

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

        User currentUser = (User) session.getAttribute("user");

        // Check if user is admin
        if (!currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        // Get all rooms
        List<Room> rooms = roomService.getAllRooms();
        request.setAttribute("rooms", rooms);

        // Forward to JSP
        request.getRequestDispatcher("/admin/rooms.jsp").forward(request, response);
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

        try {
            switch (action) {
                case "create":
                    createRoom(request, session);
                    break;

                case "update":
                    updateRoom(request, session);
                    break;

                case "delete":
                    deleteRoom(request, session);
                    break;

                case "toggleAvailability":
                    toggleAvailability(request, session);
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/rooms");
    }

    private void createRoom(HttpServletRequest request, HttpSession session) {
        String name = request.getParameter("name");
        String capacityStr = request.getParameter("capacity");
        String description = request.getParameter("description");

        if (name == null || capacityStr == null) {
            throw new IllegalArgumentException("Name and capacity are required");
        }

        Integer capacity = Integer.parseInt(capacityStr);
        roomService.createRoom(name, capacity, description);
        session.setAttribute("success", "Room created successfully");
    }

    private void updateRoom(HttpServletRequest request, HttpSession session) {
        String roomIdStr = request.getParameter("roomId");
        String name = request.getParameter("name");
        String capacityStr = request.getParameter("capacity");
        String description = request.getParameter("description");
        String isAvailableStr = request.getParameter("isAvailable");

        if (roomIdStr == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        Long roomId = Long.parseLong(roomIdStr);
        Integer capacity = capacityStr != null ? Integer.parseInt(capacityStr) : null;
        Boolean isAvailable = isAvailableStr != null ? Boolean.parseBoolean(isAvailableStr) : null;

        roomService.updateRoom(roomId, name, capacity, description, isAvailable);
        session.setAttribute("success", "Room updated successfully");
    }

    private void deleteRoom(HttpServletRequest request, HttpSession session) {
        String roomIdStr = request.getParameter("roomId");

        if (roomIdStr == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        Long roomId = Long.parseLong(roomIdStr);
        roomService.deleteRoom(roomId);
        session.setAttribute("success", "Room deleted successfully");
    }

    private void toggleAvailability(HttpServletRequest request, HttpSession session) {
        String roomIdStr = request.getParameter("roomId");

        if (roomIdStr == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        Long roomId = Long.parseLong(roomIdStr);
        roomService.toggleRoomAvailability(roomId);
        session.setAttribute("success", "Room availability updated successfully");
    }
}
