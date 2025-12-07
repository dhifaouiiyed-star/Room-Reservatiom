package org.dhifaoui.projetjee.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.service.AuthenticationService;
import org.dhifaoui.projetjee.service.HomeService;

import java.io.IOException;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private HomeService homeService;
    public void init() throws ServletException {
        homeService = new HomeService();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //check if the user is logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            //get the user
            User user = (User) session.getAttribute("user");
            //get the rooms
            List<Room> rooms = homeService.findAllRooms();
            request.setAttribute("rooms", rooms);
            request.getRequestDispatcher("/home/index.jsp").forward(request, response);
            return;
        }
        // display login page if not logged in
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
