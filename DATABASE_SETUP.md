# Room Reservation System - Database Setup Documentation

## Overview
This document describes the database setup and entity structure for the Room Reservation System.

## Database Configuration

### PostgreSQL Database
- **Database Name:** `roomreservation`
- **Connection URL:** `jdbc:postgresql://localhost:5432/roomreservation`
- **Default Credentials:** postgres/postgres (update in `persistence.xml` as needed)

### Persistence Configuration
The JPA persistence configuration is located in `src/main/resources/META-INF/persistence.xml`.

**Key settings:**
- **Provider:** Hibernate 5.6.15.Final
- **Dialect:** PostgreSQL
- **Schema Generation:** `hibernate.hbm2ddl.auto=update` (automatically creates/updates tables)
- **SQL Logging:** Enabled for development

## Entity Structure

### 1. User Entity
**Table:** `users`

Represents users in the system (both administrators and regular users).

**Fields:**
- `id` (Long) - Primary key, auto-generated
- `username` (String) - Unique username, max 50 chars
- `password` (String) - User password (should be hashed in production)
- `email` (String) - Unique email, max 100 chars
- `role` (UserRole) - User role: ADMIN or USER
- `createdAt` (LocalDateTime) - Account creation timestamp

**Relationships:**
- One-to-Many with Reservation

**Business Methods:**
- `isAdmin()` - Check if user is an administrator
- `addReservation(Reservation)` - Add a reservation to the user
- `removeReservation(Reservation)` - Remove a reservation

### 2. Room Entity
**Table:** `rooms`

Represents rooms available for reservation.

**Fields:**
- `id` (Long) - Primary key, auto-generated
- `name` (String) - Unique room name/number, max 100 chars
- `capacity` (Integer) - Room capacity (number of people)
- `description` (String) - Room description, max 500 chars
- `isAvailable` (Boolean) - Availability flag

**Relationships:**
- One-to-Many with Reservation

**Business Methods:**
- `addReservation(Reservation)` - Add a reservation for this room
- `removeReservation(Reservation)` - Remove a reservation

### 3. Reservation Entity
**Table:** `reservations`

Represents room bookings with time slots.

**Fields:**
- `id` (Long) - Primary key, auto-generated
- `startDateTime` (LocalDateTime) - Reservation start time
- `endDateTime` (LocalDateTime) - Reservation end time
- `status` (ReservationStatus) - Status: ACTIVE or CANCELLED
- `createdAt` (LocalDateTime) - Booking creation timestamp

**Relationships:**
- Many-to-One with User (who made the reservation)
- Many-to-One with Room (which room is reserved)

**Business Methods:**
- `isValidTimeSlot()` - Validates end time is after start time
- `overlapsWith(Reservation)` - Checks for time slot conflicts
- `isPast()` - Checks if reservation is in the past
- `cancel()` - Cancels the reservation
- `isActive()` - Checks if reservation is active

### 4. Enums

#### UserRole
- `ADMIN` - Administrator with full system access
- `USER` - Regular user with limited access

#### ReservationStatus
- `ACTIVE` - Active reservation
- `CANCELLED` - Cancelled reservation

## Utility Classes

### JPAUtil
Located in `org.dhifaoui.projetjee.util.JPAUtil`

Provides singleton access to EntityManagerFactory and EntityManager instances.

**Methods:**
- `getEntityManagerFactory()` - Get or create EntityManagerFactory
- `getEntityManager()` - Create a new EntityManager
- `closeEntityManagerFactory()` - Close the factory on shutdown

### DatabaseConnectionTest
Located in `org.dhifaoui.projetjee.util.DatabaseConnectionTest`

Test class to verify database connection and entity setup.

**To run:** Execute the main method to:
- Test database connection
- Create sample users, rooms, and reservations
- Verify database schema creation
- Display database statistics

## Business Rules Implemented

1. **Unique Constraints:**
   - Username and email must be unique
   - Room name must be unique

2. **Validation Rules:**
   - All required fields are marked with `@Column(nullable = false)`
   - End time must be after start time (validated in `Reservation.isValidTimeSlot()`)

3. **Cascading Operations:**
   - Deleting a user cascades to their reservations
   - Deleting a room cascades to its reservations

4. **Relationship Management:**
   - Bidirectional relationships maintained through utility methods
   - Lazy loading for performance optimization

## Testing the Setup

### Prerequisites
1. PostgreSQL installed and running
2. Database `roomreservation` created
3. Correct credentials in `persistence.xml`

### Create Database (if not exists)
```sql
CREATE DATABASE roomreservation;
```

### Run the Test
```bash
mvn compile exec:java -Dexec.mainClass="org.dhifaoui.projetjee.util.DatabaseConnectionTest"
```

Or run `DatabaseConnectionTest.main()` from your IDE.

### Expected Output
- Database connection successful
- Tables created (users, rooms, reservations)
- Sample data inserted
- Statistics displayed

## Next Steps

After verifying the database setup, you should:

1. **Implement DAO/Repository classes** for data access
2. **Create Service layer** for business logic
3. **Implement Servlets/Controllers** for web layer
4. **Add validation logic** for business rules:
   - No double booking of rooms
   - User can only book one room per time slot
   - Past reservations cannot be modified
5. **Implement authentication** and authorization
6. **Create JSP views** for the web interface

## Troubleshooting

### Connection Refused
- Verify PostgreSQL is running
- Check port 5432 is accessible
- Verify database name and credentials

### Schema Not Created
- Check `hibernate.hbm2ddl.auto` is set to `update`
- Verify entity classes are listed in `persistence.xml`
- Check Hibernate logs for errors

### ClassNotFoundException
- Ensure PostgreSQL JDBC driver is in dependencies
- Run `mvn clean install` to update dependencies
