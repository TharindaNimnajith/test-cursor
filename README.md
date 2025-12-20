# Meeting Room Booking System

A modern single-page web application for booking meeting rooms in an office environment, featuring comprehensive validation, real-time availability, and an intuitive user interface.

## Tech Stack

- **Frontend**: React 19 + Vite + Tailwind CSS
- **Backend**: Spring Boot 4.0.1 (Java 25)
- **Database**: SQLite with JPA/Hibernate
- **API**: REST with comprehensive error handling

## Features

- **Date Selection**: Choose any date to view room availability
- **Multiple Meeting Rooms**: Configurable room names and availability
- **Booking with Descriptions**: Create bookings with meaningful descriptions (minimum 3 characters)
- **Comprehensive Validation**:
  - Frontend: Description requirements, time validation, past booking prevention
  - Backend: Overlap detection, business rule enforcement
- **Visual Timeline**: Configurable time slots with color-coded availability
- **Current Time Highlighting**: Visual indicator shows current time slot on today's schedule
- **Easy Cancellation**: Click booked slots to cancel with confirmation (past bookings protected)
- **Responsive Design**: Works on desktop and mobile devices
- **Real-time Updates**: Availability grid updates immediately after booking/cancellation

## Project Structure

```
├── backend/                    # Spring Boot application
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/         # REST API endpoints
│   │   ├── entity/             # JPA entities
│   │   ├── repository/         # Data access layer
│   │   ├── service/            # Business logic with validation
│   │   ├── dto/                # Data transfer objects
│   │   ├── exception/          # Custom exceptions & global error handler
│   │   └── config/             # CORS configuration
│   ├── src/main/resources/
│   │   └── application.yaml    # Spring configuration
│   ├── meeting_rooms.db        # SQLite database (auto-created)
│   └── build.gradle            # Build configuration
└── frontend/                   # React application
    ├── src/
    │   ├── components/         # React components
    │   ├── services/           # API integration
    │   ├── constants.js        # Shared constants (API_BASE_URL, ROOM_NAMES)
    │   ├── App.jsx             # Main application component
    │   ├── main.jsx            # React entry point
    │   └── index.css           # Global styles
    ├── public/
    │   └── index.html          # HTML template
    └── package.json            # Dependencies & scripts
```

## Prerequisites

- **Java 25 JDK** (required for Spring Boot 4.0.1)
- **Node.js** (v18 or higher recommended)
- **npm** or **yarn** package manager

## Setup Instructions

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```
   *(On Windows: `gradlew.bat build`)*

3. **Run the Spring Boot application:**
   ```bash
   ./gradlew bootRun
   ```
   *(On Windows: `gradlew.bat bootRun`)*

   The backend will start on `http://localhost:8080`

   **Note:** The SQLite database (`meeting_rooms.db`) will be automatically created in the backend directory on first run. The database schema will be updated automatically via JPA.

#### Backend Details

- **Framework**: Spring Boot 4.0.1 with Java 25
- **Database**: SQLite with Hibernate ORM
- **API**: RESTful endpoints with comprehensive error handling
- **CORS**: Configured globally for frontend access
- **Validation**: Business rules enforced on the backend
- **Error Handling**: Structured error responses with proper HTTP status codes
- **Logging**: Comprehensive logging with SLF4J and Lombok

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

#### Frontend Details

- **Framework**: React 19 with modern hooks
- **Build Tool**: Vite for fast development and optimized production builds
- **Styling**: Tailwind CSS 4.x for utility-first CSS
- **State Management**: React useState/useEffect hooks
- **Validation**: Client-side validation with user-friendly error messages
- **Constants**: Centralized configuration for API URLs and room names

## Usage

1. **Open your browser** and navigate to `http://localhost:5173`

2. **Select a date** using the date picker (or click "Today" for current date)

3. **View room availability** in the grid showing all configured rooms with:
   - Green slots: Available for booking
   - Red slots: Already booked (hover to see description)
   - Blue ring: Current time indicator (today only)

4. **Create a booking:**
   - Select a room from the dropdown
   - Enter a booking description (minimum 3 characters)
   - Choose start and end times
   - Click "Create Booking"

5. **Cancel a booking:**
   - Click on a red (booked) slot in the availability grid
   - Confirm the cancellation in the popup dialog
   - Past bookings cannot be cancelled

## API Endpoints

All endpoints are prefixed with `/api` and return JSON responses.

### GET /api/bookings?date=YYYY-MM-DD
Returns all bookings for the specified date.

**Response:**
```json
[
  {
    "id": 1,
    "roomName": "Board Room",
    "description": "Team standup meeting",
    "date": "2024-12-20",
    "startTime": "09:00:00",
    "endTime": "10:00:00"
  }
]
```

### POST /api/bookings
Creates a new booking.

**Request Body:**
```json
{
  "roomName": "Board Room",
  "description": "Team standup meeting",
  "date": "2024-12-20",
  "startTime": "09:00",
  "endTime": "10:00"
}
```

**Success Response:** Created booking object (201 Created)

**Error Response:**
```json
{
  "status": 400,
  "message": "Description is required."
}
```

### DELETE /api/bookings/{id}
Cancels a booking by ID.

**Response:** 204 No Content (success) or 404 (not found)

## Validation Rules

### Frontend Validation
- Description is required
- Description must be at least 3 characters long
- End time must be after start time
- Cannot book for past times/dates
- Cannot cancel past bookings

### Backend Validation
- Description validation (required, minimum length)
- Time validation (end > start)
- Room name validation (must be a configured room)
- Overlap prevention (no double-booking same room/time)
- Date/time format validation

## Database Schema

The application uses SQLite with automatic schema management via JPA/Hibernate. The main `bookings` table structure:

```sql
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);
```

**Note:** The database schema will be automatically created/updated when the application starts. If upgrading from an older version, the schema migration will handle data conversion.

## Development Notes

- **Backend**: Spring Boot 4.0.1 with Java 25, using modern JVM features
- **Frontend**: React 19 with modern hooks and concurrent features
- **Database**: SQLite for simplicity, easily replaceable with PostgreSQL/MySQL for production
- **Styling**: Tailwind CSS v4 with JIT compilation
- **Build**: Vite for fast development builds and optimized production bundles
- **Code Quality**: ESLint configuration for consistent code style
- **CORS**: Configured to allow frontend development server access
- **Configuration**: Room names, API URLs, and time slots are configurable constants
- **Error Handling**: Comprehensive error messages for better UX
- **Logging**: Structured logging with SLF4J and Lombok annotations

## Production Deployment

1. **Build the frontend:**
   ```bash
   cd frontend && npm run build
   ```

2. **Copy built files** to `backend/src/main/resources/static/` for Spring Boot to serve them

3. **Build the backend:**
   ```bash
   cd backend && ./gradlew bootJar
   ```

4. **Run the JAR:**
   ```bash
   java -jar build/libs/meeting-room-booking-system-1.0.0.jar
   ```

The application will be available at `http://localhost:8080` with both frontend and backend served from the same port.
