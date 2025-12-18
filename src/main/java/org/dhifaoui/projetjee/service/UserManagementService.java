package org.dhifaoui.projetjee.service;

import org.dhifaoui.projetjee.dao.ReservationDAO;
import org.dhifaoui.projetjee.dao.UserDAO;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.entities.UserRole;

import java.util.List;
import java.util.Optional;


public class UserManagementService {

    private final UserDAO userDAO;
    private final ReservationDAO reservationDAO;

    public UserManagementService() {
        this.userDAO = new UserDAO();
        this.reservationDAO = new ReservationDAO();
    }

      
     // Get all users
       
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

      
     // Get a user by ID
       
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

      
     // Promote a user to admin role
       
    public User promoteToAdmin(Long userId) {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        user.setRole(UserRole.ADMIN);
        return userDAO.update(user);
    }

      
     // Demote an admin to regular user role
       
    public User demoteToUser(Long userId) {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        user.setRole(UserRole.USER);
        return userDAO.update(user);
    }

      
     // Toggle user role between ADMIN and USER
       
    public User toggleUserRole(Long userId) {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        if (user.isAdmin()) {
            user.setRole(UserRole.USER);
        } else {
            user.setRole(UserRole.ADMIN);
        }
        return userDAO.update(user);
    }

      
     // Delete a user (cascades to their reservations)
       
    public void deleteUser(Long userId) {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        userDAO.delete(userId);
    }

      
     // Get user statistics (reservation count, etc.)
       
    public UserStatistics getUserStatistics(Long userId) {
        Optional<User> userOpt = userDAO.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        long totalReservations = reservationDAO.countByUserId(userId);
        long activeReservations = reservationDAO.findActiveByUserId(userId).size();

        return new UserStatistics(userId, totalReservations, activeReservations);
    }

      
     // Get total user count
       
    public long getTotalUserCount() {
        return userDAO.findAll().size();
    }

      
     // Inner class for user statistics
       
    public static class UserStatistics {
        private final Long userId;
        private final long totalReservations;
        private final long activeReservations;

        public UserStatistics(Long userId, long totalReservations, long activeReservations) {
            this.userId = userId;
            this.totalReservations = totalReservations;
            this.activeReservations = activeReservations;
        }

        public Long getUserId() {
            return userId;
        }

        public long getTotalReservations() {
            return totalReservations;
        }

        public long getActiveReservations() {
            return activeReservations;
        }
    }
}
