package org.dhifaoui.projetjee.service;

import org.dhifaoui.projetjee.dao.ReservationDAO;
import org.dhifaoui.projetjee.dao.RoomDAO;
import org.dhifaoui.projetjee.dao.UserDAO;
import org.dhifaoui.projetjee.entities.Reservation;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Reservation business logic.
 * Handles business rule validation and reservation operations.
 */
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final UserDAO userDAO;
    private final RoomDAO roomDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.userDAO = new UserDAO();
        this.roomDAO = new RoomDAO();
    }

    /**
     * Business Rule: Validate that the room is not already booked for the given
     * time slot
     */
    public boolean validateNoDoubleBooking(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Reservation> conflicts = reservationDAO.findConflictingReservations(roomId, startDateTime, endDateTime);
        return conflicts.isEmpty();
    }

    /**
     * Business Rule: Validate that the user doesn't have another active reservation
     * in the same time slot
     */
    public boolean validateUserAvailability(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Reservation> userConflicts = reservationDAO.findUserReservationInTimeSlot(userId, startDateTime,
                endDateTime);
        return userConflicts.isEmpty();
    }

    /**
     * Business Rule: Validate that the reservation is not in the past
     */
    public boolean validateNotPast(LocalDateTime startDateTime) {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Business Rule: Validate that end time is after start time
     */
    public boolean validateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return endDateTime.isAfter(startDateTime);
    }

    /**
     * Business Rule: Check if a reservation can be modified (not in the past)
     */
    public boolean canModifyReservation(Reservation reservation) {
        return !reservation.isPast();
    }

    /**
     * Create a reservation with full business rule validation
     * 
     * @throws IllegalArgumentException if any business rule is violated
     */
    public Reservation createReservation(Long userId, Long roomId, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        // Validate time range
        if (!validateTimeRange(startDateTime, endDateTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Validate not in the past
        if (!validateNotPast(startDateTime)) {
            throw new IllegalArgumentException("Cannot create reservation in the past");
        }

        // Validate no double booking
        if (!validateNoDoubleBooking(roomId, startDateTime, endDateTime)) {
            throw new IllegalArgumentException("This room is already booked for the selected time slot");
        }

        // Validate user availability
        if (!validateUserAvailability(userId, startDateTime, endDateTime)) {
            throw new IllegalArgumentException("You already have a reservation during this time slot");
        }

        // Get user and room
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Optional<Room> roomOpt = roomDAO.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        Room room = roomOpt.get();
        if (!room.getIsAvailable()) {
            throw new IllegalArgumentException("This room is currently unavailable");
        }

        // Create and save reservation
        Reservation reservation = new Reservation(startDateTime, endDateTime, userOpt.get(), room);
        return reservationDAO.save(reservation);
    }

    /**
     * Cancel a user's own reservation
     * 
     * @throws IllegalArgumentException if validation fails
     */
    public void cancelReservation(Long reservationId, Long userId) {
        Optional<Reservation> reservationOpt = reservationDAO.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();

        // Verify user owns this reservation
        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own reservations");
        }

        // Check if reservation can be modified
        if (!canModifyReservation(reservation)) {
            throw new IllegalArgumentException("Cannot cancel past reservations");
        }

        // Check if already cancelled
        if (!reservation.isActive()) {
            throw new IllegalArgumentException("Reservation is already cancelled");
        }

        // Cancel the reservation
        reservation.cancel();
        reservationDAO.update(reservation);
    }

    /**
     * Admin cancel any reservation
     * 
     * @throws IllegalArgumentException if validation fails
     */
    public void adminCancelReservation(Long reservationId) {
        Optional<Reservation> reservationOpt = reservationDAO.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();

        // Check if already cancelled
        if (!reservation.isActive()) {
            throw new IllegalArgumentException("Reservation is already cancelled");
        }

        // Cancel the reservation
        reservation.cancel();
        reservationDAO.update(reservation);
    }

    /**
     * Get all reservations for a specific user
     */
    public List<Reservation> getUserReservations(Long userId) {
        return reservationDAO.findByUserId(userId);
    }

    /**
     * Get active reservations for a specific user
     */
    public List<Reservation> getUserActiveReservations(Long userId) {
        return reservationDAO.findActiveByUserId(userId);
    }

    /**
     * Get upcoming reservations for a specific user
     */
    public List<Reservation> getUserUpcomingReservations(Long userId) {
        return reservationDAO.findUpcomingByUserId(userId);
    }

    /**
     * Get past reservations for a specific user
     */
    public List<Reservation> getUserPastReservations(Long userId) {
        return reservationDAO.findPastByUserId(userId);
    }

    /**
     * Get all reservations (admin only)
     */
    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    /**
     * Get all upcoming reservations (admin)
     */
    public List<Reservation> getUpcomingReservations() {
        return reservationDAO.findUpcomingReservations();
    }

    /**
     * Get reservations for a specific room
     */
    public List<Reservation> getRoomReservations(Long roomId) {
        return reservationDAO.findByRoomId(roomId);
    }

    /**
     * Get a reservation by ID
     */
    public Optional<Reservation> getReservationById(Long id) {
        return reservationDAO.findById(id);
    }

    /**
     * Get reservation statistics
     */
    public long getTotalActiveReservations() {
        return reservationDAO.countActiveReservations();
    }

    /**
     * Get user's reservation count
     */
    public long getUserReservationCount(Long userId) {
        return reservationDAO.countByUserId(userId);
    }
}
