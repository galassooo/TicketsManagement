# Ticket Management System

This is a web-based ticket management system built with Spring Boot. The application allows users to create, track, and manage support tickets and organize them into milestones.

## Features

- **User Management**: Registration and authentication system with role-based access control (ADMIN and USER roles)
- **Ticket Management**: Create, view, edit, and delete tickets
- **Milestone Management**: Organize tickets into milestones with progress tracking
- **File Attachments**: Upload and download attachments for tickets
- **Status Tracking**: Track ticket status (OPEN, IN_PROGRESS, DONE, CLOSED)
- **Search Functionality**: Search for tickets across various parameters
- **Responsive UI**: Modern responsive interface with dark theme

## Technical Stack

- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf templates, HTML, CSS, JavaScript
- **Database**: JPA compatible database (configured via Spring Boot)
- **Authentication**: Custom implementation with Spring Security

## Project Structure

The project follows a standard MVC architecture:

- **Models**: Define the data structure (User, Ticket, Milestone, etc.)
- **Controllers**: Handle HTTP requests and manage application flow
- **Services**: Implement business logic and data access
- **Repositories**: Interface with the database
- **Views**: Thymeleaf templates for the user interface

## Security

The application implements security with Spring Security:

- Custom user details service
- BCrypt password encoding
- Role-based access control
- Protected endpoints with appropriate permission levels

## How to Use

### User Registration and Login

- New users can register with a username, name, surname, and password
- User validation ensures proper input format
- After registration, users can login to access the system

### Ticket Management

- Create tickets with title, description, type, and optional attachments
- View ticket details including status and assigned user
- Admin users can edit ticket details or change the assigned user
- Change ticket status through a streamlined interface

### Milestone Management

- Create milestones with title, description, and due date
- Add tickets to milestones
- Track milestone progress based on completed tickets
- Mark milestones as complete (admin only)

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven or Gradle

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run` or `./gradlew bootRun`
4. Access the application at `http://localhost:8080/tickets`

### Default Admin Account

The system creates a default admin account on startup:
- Username: `_admin`
- Password: `password`

## Screenshots

The application features a dark-themed interface with distinct sections for:
- Ticket listings categorized by status
- Milestone progress tracking
- Ticket details with attachment management
- Admin control panels

## License

This project is available for educational and personal use.
