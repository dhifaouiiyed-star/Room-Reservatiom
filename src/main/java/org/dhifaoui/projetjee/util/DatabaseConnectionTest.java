package org.dhifaoui.projetjee.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.dhifaoui.projetjee.entities.*;

import java.time.LocalDateTime;

/**
 * Test class to verify database connection and entity setup.
 * This class creates sample data to test the JPA configuration.
 */
public class DatabaseConnectionTest {

    public static void main(String[] args) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            // Get EntityManager
            em = JPAUtil.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            System.out.println("✓ Successfully connected to the database!");
            System.out.println("✓ EntityManager created successfully!");

            // Create a test admin user
            User admin = new User("admin", "admin123", "admin@example.com", UserRole.ADMIN);
            em.persist(admin);
            System.out.println("✓ Admin user created: " + admin.getUsername());

            // Create a test regular user
            User user1 = new User("john.doe", "password123", "john@example.com", UserRole.USER);
            em.persist(user1);
            System.out.println("✓ Regular user created: " + user1.getUsername());

            // Create test rooms
            Room room1 = new Room("Salle A101", 20, "Salle de conférence avec projecteur");
            em.persist(room1);
            System.out.println("✓ Room created: " + room1.getName());

            Room room2 = new Room("Salle B202", 30, "Grande salle de réunion");
            em.persist(room2);
            System.out.println("✓ Room created: " + room2.getName());

            // Create test reservations
            LocalDateTime start1 = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
            LocalDateTime end1 = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);
            Reservation reservation1 = new Reservation(start1, end1, user1, room1);
            em.persist(reservation1);
            System.out.println("✓ Reservation created for " + user1.getUsername() + " in " + room1.getName());

            LocalDateTime start2 = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);
            LocalDateTime end2 = LocalDateTime.now().plusDays(2).withHour(16).withMinute(0);
            Reservation reservation2 = new Reservation(start2, end2, user1, room2);
            em.persist(reservation2);
            System.out.println("✓ Reservation created for " + user1.getUsername() + " in " + room2.getName());

            // Commit transaction
            transaction.commit();
            System.out.println("\n✓ All test data saved successfully!");
            System.out.println("✓ Database schema created successfully!");

            // Verify data by querying
            em.clear();
            long userCount = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            long roomCount = em.createQuery("SELECT COUNT(r) FROM Room r", Long.class).getSingleResult();
            long reservationCount = em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class).getSingleResult();

            System.out.println("\n=== Database Statistics ===");
            System.out.println("Total Users: " + userCount);
            System.out.println("Total Rooms: " + roomCount);
            System.out.println("Total Reservations: " + reservationCount);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                System.err.println("✗ Transaction rolled back due to error");
            }
            System.err.println("✗ Error during database operation:");
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            JPAUtil.closeEntityManagerFactory();
            System.out.println("\n✓ EntityManager closed");
        }
    }
}
