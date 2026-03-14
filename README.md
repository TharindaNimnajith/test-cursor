# Meeting Room Booking System

A modern single-page web application for booking meeting rooms in an office environment, featuring comprehensive validation, real-time availability, and an intuitive user interface.

## Tech Stack

- **Frontend**: React 19 + Vite + Tailwind CSS
- **Backend**: Spring Boot 4 (Java 25)
- **Database**: SQLite with JPA/Hibernate
- **API**: REST with comprehensive error handling

## Features

- **Date Selection**: Choose any date to view room availability
- **Multiple Meeting Rooms**: Configurable room names and availability
- **Booking with Descriptions**: Create bookings with meaningful descriptions (minimum 3 characters)
- **Comprehensive Validation**:
  - Frontend: Description requirements, time validation, past booking prevention
  - Backend: Overlap detection to prevent double-booking
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
│   │   ├── service/            # Business logic with overlap validation
│   │   ├── dto/                # Data transfer objects
│   │   ├── exception/          # Custom exceptions & global error handler
│   │   └── config/             # CORS configuration
│   ├── src/main/resources/
│   │   ├── application.yaml        # Default config (used in Docker)
│   │   └── application-local.yaml  # Local dev overrides
│   └── build.gradle            # Build configuration
└── frontend/                   # React application
    ├── src/
    │   ├── components/         # React components
    │   ├── services/           # API integration & constants
    │   ├── App.jsx             # Main application component
    │   └── main.jsx            # React entry point
    ├── nginx.conf              # Nginx config (SPA routing + /api proxy)
    └── package.json            # Dependencies & scripts
```

## Prerequisites

- **Java 25 JDK** (required for Spring Boot 4)
- **Node.js** (v18 or higher)
- **npm** package manager

## Local Development

### Backend

```bash
cd backend
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

The backend starts on `http://localhost:8080`. The SQLite database (`meeting_rooms.db`) is created automatically in the `backend/` directory.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`. Vite proxies `/api/*` to `localhost:8080`.

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
  "message": "This room is already booked for the selected time slot."
}
```

### DELETE /api/bookings/{id}
Cancels a booking by ID.

**Response:** 204 No Content (success) or 404 (not found)

## Validation Rules

### Frontend Validation
- Description is required and must be at least 3 characters
- End time must be after start time
- Cannot book for past times/dates
- Cannot cancel past bookings

### Backend Validation
- Overlap prevention — no double-booking the same room at the same time

## Database Schema

The application uses SQLite with automatic schema management via JPA/Hibernate.

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

## Production Deployment (DigitalOcean Droplet)

Build images locally and push to Docker Hub, then pull and run on the droplet. No docker-compose needed.

### Traffic flow

```
Browser → Frontend Nginx container (port 80)
              ├── /api/*  → proxied to backend container (Docker network, port 8080)
              └── /*      → serves static React SPA
```

The frontend Nginx container handles both static file serving and API proxying — no host-level Nginx or docker-compose required.

### Build & push (local machine)

```bash
# Backend
cd backend
docker build -t tharinda1998/my-backend:latest .
docker push tharinda1998/my-backend:latest

# Frontend
cd frontend
docker build -t tharinda1998/my-frontend:latest .
docker push tharinda1998/my-frontend:latest
```

### Run on the droplet

```bash
# Create shared network (once)
docker network create app-network

# Pull images
docker pull tharinda1998/my-backend:latest
docker pull tharinda1998/my-frontend:latest

# Start backend (not exposed on host ports — only reachable via Docker network)
docker run -d \
  --name backend \
  -p 127.0.0.1:8080:8080 \
  -v /app/data:/app/data \
  tharinda1998/my-backend:latest

# Start frontend (port 80 exposed to the internet)
docker run -d \
  --name frontend \
  -p 127.0.0.1:3000:80 \
  tharinda1998/my-frontend:latest
```

The SQLite database is persisted to `/app/data` on the droplet host via the volume mount.

### Redeploying after changes

```bash
docker stop backend frontend
docker rm backend frontend
docker pull tharinda1998/my-backend:latest
docker pull tharinda1998/my-frontend:latest
# then re-run the docker run commands above
```
