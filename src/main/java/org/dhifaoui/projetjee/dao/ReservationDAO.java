package org.dhifaoui.projetjee.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.dhifaoui.projetjee.entities.Reservation;
import org.dhifaoui.projetjee.entities.ReservationStatus;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.util.JPAUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationDAO {


    //Save a new reservation to the database

    public Reservation save(Reservation reservation) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(reservation);
            em.getTransaction().commit();
            return reservation;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving reservation: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }


    // Find a reservation by ID

    public Optional<Reservation> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Reservation reservation = em.find(Reservation.class, id);
            return Optional.ofNullable(reservation);
        } finally {
            em.close();
        }
    }


    //  Update an existing reservation

    public Reservation update(Reservation reservation) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Reservation updatedReservation = em.merge(reservation);
            em.getTransaction().commit();
            return updatedReservation;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating reservation: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }


    // Delete a reservation by ID

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Reservation reservation = em.find(Reservation.class, id);
            if (reservation != null) {
                em.remove(reservation);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting reservation: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }


     // Find all reservations

    public List<Reservation> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "ORDER BY r.startDateTime DESC",
                    Reservation.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find all reservations for a specific user

    public List<Reservation> findByUser(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user = :user ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find all reservations for a specific user by user ID

    public List<Reservation> findByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user.id = :userId ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find all reservations for a specific room

    public List<Reservation> findByRoom(Room room) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.room = :room ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("room", room);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find all reservations for a specific room by room ID

    public List<Reservation> findByRoomId(Long roomId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.room.id = :roomId ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("roomId", roomId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find active reservations for a specific user

    public List<Reservation> findActiveByUser(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user = :user AND r.status = :status ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("user", user);
            query.setParameter("status", ReservationStatus.ACTIVE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    //  Find active reservations for a specific user by user ID

    public List<Reservation> findActiveByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user.id = :userId AND r.status = :status ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("userId", userId);
            query.setParameter("status", ReservationStatus.ACTIVE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Check for double booking within a time
    public List<Reservation> findConflictingReservations(Long roomId, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
                            "AND r.status = :status " +
                            "AND r.startDateTime < :endDateTime " +
                            "AND r.endDateTime > :startDateTime",
                    Reservation.class);
            query.setParameter("roomId", roomId);
            query.setParameter("status", ReservationStatus.ACTIVE);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Find user reservation by time
    public List<Reservation> findUserReservationInTimeSlot(Long userId, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r WHERE r.user.id = :userId " +
                            "AND r.status = :status " +
                            "AND r.startDateTime < :endDateTime " +
                            "AND r.endDateTime > :startDateTime",
                    Reservation.class);
            query.setParameter("userId", userId);
            query.setParameter("status", ReservationStatus.ACTIVE);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find upcoming reservations (future reservations only)

    public List<Reservation> findUpcomingReservations() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.status = :status " +
                            "AND r.startDateTime > :now ORDER BY r.startDateTime ASC",
                    Reservation.class);
            query.setParameter("status", ReservationStatus.ACTIVE);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } finally {
            em.close();
        }
    }


     // Find upcoming reservations for a specific user

    public List<Reservation> findUpcomingByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user.id = :userId " +
                            "AND r.status = :status " +
                            "AND r.startDateTime > :now ORDER BY r.startDateTime ASC",
                    Reservation.class);
            query.setParameter("userId", userId);
            query.setParameter("status", ReservationStatus.ACTIVE);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

     // Find past reservations for a specific user

    public List<Reservation> findPastByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.user " +
                            "JOIN FETCH r.room " +
                            "WHERE r.user.id = :userId " +
                            "AND r.endDateTime < :now ORDER BY r.startDateTime DESC",
                    Reservation.class);
            query.setParameter("userId", userId);
            query.setParameter("now", LocalDateTime.now());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

     // Count total active reservations

    public long countActiveReservations() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM Reservation r WHERE r.status = :status", Long.class);
            query.setParameter("status", ReservationStatus.ACTIVE);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }


     // Count reservations for a specific user

    public long countByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM Reservation r WHERE r.user.id = :userId", Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
