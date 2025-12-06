package org.dhifaoui.projetjee.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a room reservation.
 * Contains business rules validation for time slots and user constraints.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // Constructors
    public Reservation() {
        this.status = ReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Room room) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.user = user;
        this.room = room;
        this.status = ReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // Business logic methods

    /**
     * Validates if the end time is after the start time
     */
    public boolean isValidTimeSlot() {
        return endDateTime != null && startDateTime != null && endDateTime.isAfter(startDateTime);
    }

    /**
     * Checks if this reservation overlaps with another reservation
     */
    public boolean overlapsWith(Reservation other) {
        if (other == null || !this.room.getId().equals(other.getRoom().getId())) {
            return false;
        }

        return this.startDateTime.isBefore(other.getEndDateTime()) &&
                this.endDateTime.isAfter(other.getStartDateTime());
    }

    /**
     * Checks if the reservation is in the past
     */
    public boolean isPast() {
        return endDateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Cancels the reservation
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    /**
     * Checks if the reservation is active
     */
    public boolean isActive() {
        return this.status == ReservationStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", status=" + status +
                ", userId=" + (user != null ? user.getId() : null) +
                ", roomId=" + (room != null ? room.getId() : null) +
                ", createdAt=" + createdAt +
                '}';
    }
}
