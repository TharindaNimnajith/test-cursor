# Meeting Room Booking System

A simple single-page web application for booking meeting rooms in an office environment.

## Tech Stack

- **Frontend**: React (JavaScript) + Tailwind CSS
- **Backend**: Spring Boot (Java 21)
- **Database**: SQLite
- **API**: REST

## Features

- View availability for all 7 meeting rooms
- Create bookings by selecting room, date, start time, and end time
- Cancel existing bookings
- Real-time availability updates
- Visual timeline view (08:00-18:00) with color-coded slots
- Current time highlighting on today's schedule
- Overlap validation (prevents double-booking)

## Project Structure

```
├── backend/          # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/demo/
│   │       │   ├── entity/      # Booking entity
│   │       │   ├── repository/  # BookingRepository
│   │       │   ├── service/     # BookingService with validation
│   │       │   ├── controller/  # REST controllers
│   │       │   └── dto/         # Request DTOs
│   │       └── resources/
│   │           └── application.yaml
│   └── build.gradle
└── frontend/         # React application
    ├── src/
    │   ├── components/  # React components
    │   ├── services/    # API service
    │   └── App.jsx      # Main app component
    └── package.json
```

## Prerequisites

- Java 21 JDK
- Node.js (v16 or higher)
- npm or yarn

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```
   (On Windows: `gradlew.bat build`)

3. Run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```
   (On Windows: `gradlew.bat bootRun`)

   The backend will start on `http://localhost:8080`

   The SQLite database (`meeting_rooms.db`) will be automatically created in the backend directory on first run.

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## Usage

1. Open your browser and navigate to `http://localhost:5173`
2. Select a date using the date picker
3. View the availability grid showing all 7 rooms with their booked (red) and available (green) slots
4. To create a booking:
   - Select a room from the dropdown
   - Choose start and end times
   - Click "Create Booking"
5. To cancel a booking:
   - Click on a red (booked) slot in the availability grid
   - Confirm the cancellation

## API Endpoints

### GET /api/rooms
Returns the list of all 7 meeting rooms.

**Response:**
```json
[
  {"id": 1, "name": "Room 1"},
  {"id": 2, "name": "Room 2"},
  ...
]
```

### GET /api/bookings?date=YYYY-MM-DD
Returns all bookings for the specified date.

**Response:**
```json
[
  {
    "id": 1,
    "roomId": 1,
    "date": "2024-01-15",
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
  "roomId": 1,
  "date": "2024-01-15",
  "startTime": "09:00",
  "endTime": "10:00"
}
```

**Response:** Created booking object (201) or error message (400)

### DELETE /api/bookings/{id}
Cancels a booking by ID.

**Response:** 204 No Content or error message (404)

## Validation Rules

- End time must be strictly after start time
- Overlapping bookings for the same room and date are not allowed
- Room ID must be between 1 and 7
- All validations are enforced on the backend

## Database

The application uses SQLite with automatic schema creation. The database file (`meeting_rooms.db`) is created automatically in the backend directory when the application starts.

## Development Notes

- The backend uses Spring Boot 3.2.0 with Java 21
- The frontend uses Vite as the build tool
- Tailwind CSS is used for styling
- CORS is configured to allow requests from `http://localhost:5173`
- The availability grid shows 30-minute time slots from 08:00 to 18:00
