package org.dhifaoui.projetjee.service;

import org.dhifaoui.projetjee.dao.RoomDAO;
import org.dhifaoui.projetjee.entities.Room;

import java.util.List;

public class HomeService {
    private final RoomDAO roomDAO;

    public HomeService() {
        this.roomDAO = new RoomDAO();
    }

    public List<Room> findAllRooms()
    {
        return roomDAO.findAll();
    }
}
