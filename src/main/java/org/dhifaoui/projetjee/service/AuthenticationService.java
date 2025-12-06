package org.dhifaoui.projetjee.service;

import org.dhifaoui.projetjee.dao.UserDAO;
import org.dhifaoui.projetjee.entities.User;
import org.dhifaoui.projetjee.entities.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Service class for handling authentication and user management.
 */
public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     * 
     * @param username User's username
     * @param email    User's email
     * @param password Plain text password (will be hashed)
     * @param role     User's role
     * @return The created user
     * @throws IllegalArgumentException if validation fails
     */
    public User register(String username, String email, String password, UserRole role) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check for duplicates
        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDAO.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Hash password and create user
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword, email, role);

        return userDAO.save(user);
    }

    /**
     * Authenticate a user with username and password
     * 
     * @param username Username
     * @param password Plain text password
     * @return Optional containing the user if authentication succeeds
     */
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (verifyPassword(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * Hash a password using BCrypt
     * 
     * @param password Plain text password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verify a password against a hashed password
     * 
     * @param rawPassword    Plain text password
     * @param hashedPassword Hashed password from database
     * @return true if password matches
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Basic email format validation
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
