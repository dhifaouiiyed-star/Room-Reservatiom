# Room Reservation System

A comprehensive web application for managing and reserving rooms within an organization, built with Java EE, JPA/Hibernate, and PostgreSQL.

## Features

### Admin Features
- **Dashboard**: Overview statistics of users, rooms, and active reservations
- **User Management**: Create, update, delete users, and manage roles (Admin/User)
- **Room Management**: Create, update, delete rooms, and toggle availability
- **Reservation Management**: View all reservations with filtering options and cancel any reservation

### User Features
- **Personal Dashboard**: View upcoming and past reservations with statistics
- **Browse Rooms**: Search and filter available rooms by capacity
- **Create Reservations**: Book rooms with date/time selection
- **Cancel Reservations**: Cancel own reservations (except past ones)

### Business Rules (Enforced)
✅ No double booking - a room cannot be reserved twice for the same time slot  
✅ One reservation at a time - users cannot book multiple rooms for overlapping time slots  
✅ Past reservations cannot be modified  
✅ Admin can cancel any reservation  
✅ Time validation - end time must be after start time  
✅ Future bookings only - reservations must be in the future

## Technology Stack

- **Backend**: Java 24, Jakarta EE (Servlets, JPA)
- **ORM**: Hibernate 6.4.0
- **Database**: PostgreSQL 42.5.4
- **Security**: BCrypt password hashing
- **Frontend**: JSP, Vanilla CSS
- **Build Tool**: Maven

## Project Structure

```
ProjetJEE/
├── src/main/java/org/dhifaoui/projetjee/
│   ├── dao/                      # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── RoomDAO.java
│   │   └── ReservationDAO.java
│   ├── entities/                 # JPA Entities
│   │   ├── User.java
│   │   ├── Room.java
│   │   ├── Reservation.java
│   │   ├── UserRole.java
│   │   └── ReservationStatus.java
│   ├── service/                  # Business Logic Layer
│   │   ├── AuthenticationService.java
│   │   ├── ReservationService.java
│   │   ├── RoomService.java
│   │   └── UserManagementService.java
│   ├── Servlets/
│   │   ├── admin/                # Admin Servlets
│   │   │   ├── AdminDashboardServlet.java
│   │   │   ├── AdminUserManagementServlet.java
│   │   │   ├── AdminRoomManagementServlet.java
│   │   │   └── AdminReservationServlet.java
│   │   ├── user/                 # User Servlets
│   │   │   ├── UserDashboardServlet.java
│   │   │   ├── BrowseRoomsServlet.java
│   │   │   ├── CreateReservationServlet.java
│   │   │   └── CancelReservationServlet.java
│   │   ├── LoginServlet.java
│   │   └── SignupServlet.java
│   └── util/                     # Utilities
│       └── JPAUtil.java
├── src/main/webapp/
│   ├── admin/                    # Admin JSP Views
│   │   ├── dashboard.jsp
│   │   ├── users.jsp
│   │   ├── rooms.jsp
│   │   └── reservations.jsp
│   ├── user/                     # User JSP Views
│   │   ├── dashboard.jsp
│   │   ├── rooms.jsp
│   │   └── create-reservation.jsp
│   ├── css/                      # Stylesheets
│   │   ├── admin.css
│   │   ├── user.css
│   │   └── auth.css
│   ├── login.jsp
│   └── signup.jsp
└── src/main/resources/
    └── META-INF/
        └── persistence.xml       # JPA Configuration
```

## Setup Instructions

### Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Set `JAVA_HOME` environment variable

2. **PostgreSQL Database**
   - Install PostgreSQL 12 or higher
   - Create database: `roomreservation`

3. **Apache Tomcat or similar Java EE server**
   - Tomcat 10.1+ recommended

4. **Maven** (or use included mvnw wrapper)

### Database Setup

1. Create the database:
```sql
CREATE DATABASE roomreservation;
```

2. Update database credentials in `src/main/resources/META-INF/persistence.xml`:
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/roomreservation"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="your_password"/>
```

3. Tables will be created automatically by Hibernate on first run.

### Build and Deploy

1. **Clean and build the project:**
```bash
mvn clean package
```

2. **Deploy the WAR file:**
   - Copy `target/ProjetJEE-1.0-SNAPSHOT.war` to your Tomcat `webapps` folder
   - Or use your IDE's deployment features

3. **Start the server** and navigate to:
   ```
   http://localhost:8080/ProjetJEE-1.0-SNAPSHOT/
   ```

### Initial Setup

1. **Create an admin account:**
   - Go to `/signup` and create a user
   - Manually update the database to set the user's role to ADMIN:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

2. **Create rooms** via the admin panel

3. **Start using the system!**

## Usage Guide

### For Administrators

1. **Login** at `/login` with admin credentials
2. **Dashboard** shows overview statistics
3. **Manage Users**: Promote users to admin, demote admins, or delete users
4. **Manage Rooms**: Create new rooms, edit details, toggle availability, or delete
5. **View Reservations**: See all system reservations, filter by status, cancel any reservation

### For Users

1. **Login** or **Sign up** at `/login` or `/signup`
2. **Dashboard** displays your upcoming and past reservations
3. **Browse Rooms**: View available rooms with filtering options
4. **Book a Room**: Select a room, choose date/time, and create reservation
5. **Cancel Reservation**: Cancel your future reservations from the dashboard

## Design Highlights

### Modern UI/UX
- **Gradient Design**: Premium gradient backgrounds and buttons
- **Card-Based Layouts**: Clean, organized information display
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Smooth Animations**: Hover effects and transitions
- **Color-Coded Status**: Visual indicators for availability and reservation status

### Architecture
- **Three-Layer Architecture**: DAO → Service → Servlet
- **Business Logic Separation**: All validation rules in service layer
- **Transaction Management**: Proper database transaction handling
- **Error Handling**: Comprehensive exception handling with user-friendly messages

## Business Logic Implementation

All business rules are enforced in `ReservationService.java`:

```java
// No double booking
validateNoDoubleBooking(roomId, startDateTime, endDateTime)

// User availability check
validateUserAvailability(userId, startDateTime, endDateTime)

// No past reservations
validateNotPast(startDateTime)

// Valid time range
validateTimeRange(startDateTime, endDateTime)
```

## Security Features

- ✅ Password hashing with BCrypt
- ✅ Session-based authentication
- ✅ Role-based access control (Admin/User)
- ✅ SQL injection prevention (via JPA/Hibernate)
- ✅ Input validation and sanitization

## Future Enhancements (Optional)

- 📧 Email notifications for reservations
- 📅 Calendar view integration
- ⭐ Room rating system
- 🔔 Real-time availability updates
- 📊 Advanced analytics dashboard
- 🌐 Multi-language support

## Troubleshooting

### Common Issues

**Database Connection Error:**
- Verify PostgreSQL is running
- Check database credentials in `persistence.xml`
- Ensure database `roomreservation` exists

**Build Errors:**
- Ensure JAVA_HOME is set correctly
- Run `mvn clean install` to refresh dependencies

**Login Issues:**
- Verify user exists in database
- Check password hashing is working (compare hashed password in DB)

## License

This project is created for educational purposes as part of a JEE course.

## Contributors

Developed by: Dhifaoui Iyed

## Support

For issues or questions, please refer to the course materials or contact the instructor.
