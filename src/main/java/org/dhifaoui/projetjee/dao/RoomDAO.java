package org.dhifaoui.projetjee.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.dhifaoui.projetjee.Exceptions.NotFoundException;
import org.dhifaoui.projetjee.entities.Room;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.util.JPAUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    // Create a new room
    public Room save(Room room) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(room);
            em.getTransaction().commit();
            return room;
        }catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error saving user");
        }finally {
            em.close();
        }
    }
    // Find by id
    public Optional<Room> findById(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        Room room = em.find(Room.class, id);
        if(room == null) throw new NotFoundException("Room not found");
        return Optional.of(room);
    }
    // Update room
    public Room update(Room room) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Room updatedRoom = em.merge(room);
            em.getTransaction().commit();
            return updatedRoom;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating Room: " + e.getMessage(), e);
        } finally {
            em.close();
        }

    }
    // Delete room
    public void deleteById(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Room room = em.find(Room.class, id);
            if (room != null) {
                em.remove(room);
            }else {
                throw new NotFoundException("Room not found");
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    // Get All the Rooms
    public List<Room> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r", Room.class);
            for(Room room : query.getResultList()){
                System.out.println(room);
            }
            return query.getResultList();
        }
    }
}
