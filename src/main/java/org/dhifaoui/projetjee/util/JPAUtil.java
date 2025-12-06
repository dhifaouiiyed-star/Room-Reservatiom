package org.dhifaoui.projetjee.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManager instances.
 * Provides a singleton EntityManagerFactory for the application.
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "myPU";
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Private constructor to prevent instantiation
     */
    private JPAUtil() {
    }

    /**
     * Gets the EntityManagerFactory instance.
     * Creates it if it doesn't exist.
     * 
     * @return EntityManagerFactory instance
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (Exception e) {
                System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        return entityManagerFactory;
    }

    /**
     * Creates a new EntityManager instance
     * 
     * @return new EntityManager
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory.
     * Should be called when the application shuts down.
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
