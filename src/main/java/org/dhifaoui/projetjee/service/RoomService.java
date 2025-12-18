package org.dhifaoui.projetjee.service;

import org.dhifaoui.projetjee.dao.ReservationDAO;
import org.dhifaoui.projetjee.dao.RoomDAO;
import org.dhifaoui.projetjee.entities.Reservation;
import org.dhifaoui.projetjee.entities.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class RoomService {

    private final RoomDAO roomDAO;
    private final ReservationDAO reservationDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
        this.reservationDAO = new ReservationDAO();
    }

     
     // Get all rooms
      
    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

     
     // Get a room by ID
      
    public Optional<Room> getRoomById(Long id) {
        return roomDAO.findById(id);
    }

     
     // Get available rooms for a specific time slot
      
    public List<Room> getAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Room> allRooms = roomDAO.findAll();

        // Filter rooms that don't have conflicts and are marked as available
        return allRooms.stream()
                .filter(room -> room.getIsAvailable())
                .filter(room -> {
                    List<Reservation> conflicts = reservationDAO.findConflictingReservations(
                            room.getId(), startDateTime, endDateTime);
                    return conflicts.isEmpty();
                })
                .collect(Collectors.toList());
    }

     
     // Create a new room with validation
      
    public Room createRoom(String name, Integer capacity, String description) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be greater than 0");
        }

        // Create and save room
        Room room = new Room(name.trim(), capacity, description);
        return roomDAO.save(room);
    }

     
     // Update an existing room
      
    public Room updateRoom(Long id, String name, Integer capacity, String description, Boolean isAvailable) {
        Optional<Room> roomOpt = roomDAO.findById(id);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        Room room = roomOpt.get();

        // Validate inputs
        if (name != null && !name.trim().isEmpty()) {
            room.setName(name.trim());
        }
        if (capacity != null && capacity > 0) {
            room.setCapacity(capacity);
        }
        if (description != null) {
            room.setDescription(description);
        }
        if (isAvailable != null) {
            room.setIsAvailable(isAvailable);
        }

        return roomDAO.update(room);
    }

     
     // Delete a room (cascades to reservations)
      
    public void deleteRoom(Long id) {
        Optional<Room> roomOpt = roomDAO.findById(id);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        roomDAO.deleteById(id);
    }

     
     // Toggle room availability
      
    public Room toggleRoomAvailability(Long id) {
        Optional<Room> roomOpt = roomDAO.findById(id);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        Room room = roomOpt.get();
        room.setIsAvailable(!room.getIsAvailable());
        return roomDAO.update(room);
    }

     
     // Check if room is available for a specific time slot
      
    public boolean isRoomAvailable(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Optional<Room> roomOpt = roomDAO.findById(roomId);

        if (roomOpt.isEmpty() || !roomOpt.get().getIsAvailable()) {
            return false;
        }

        List<Reservation> conflicts = reservationDAO.findConflictingReservations(
                roomId, startDateTime, endDateTime);
        return conflicts.isEmpty();
    }

     
     // Get room with its reservations
      
    public Optional<Room> getRoomWithReservations(Long id) {
        return roomDAO.findById(id);
    }

     
     // Get total room count
      
    public long getTotalRoomCount() {
        return roomDAO.findAll().size();
    }
}
