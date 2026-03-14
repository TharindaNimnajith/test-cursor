# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Meeting Room Booking System — a full-stack app with a React frontend and Spring Boot backend using SQLite.

## Commands

### Backend (Spring Boot + Gradle)
```bash
cd backend
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun   # Dev server on :8080
./gradlew clean build -x test                    # Full rebuild
./gradlew bootJar                                # Create production JAR
./gradlew test                                   # Run tests
```

### Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev                        # Dev server on :5173
npm run build                      # Production build
npm run lint                       # ESLint
npm run preview                    # Preview production build
```

### Docker (build & push locally, run on droplet)
```bash
# Build and push
docker build -t tharinda1998/my-backend:latest ./backend
docker push tharinda1998/my-backend:latest
docker build -t tharinda1998/my-frontend:latest ./frontend
docker push tharinda1998/my-frontend:latest

# On droplet (no docker-compose):
docker network create app-network
docker run -d --name backend --network app-network -v /app/data:/app/data tharinda1998/my-backend:latest
docker run -d --name frontend --network app-network -p 80:80 tharinda1998/my-frontend:latest
```

## Architecture

### Tech Stack
- **Frontend:** React 19, Vite + SWC, Tailwind CSS 4
- **Backend:** Spring Boot 4, Java 25, Gradle
- **Database:** SQLite (JPA/Hibernate with community dialect, auto-update schema)
- **Proxy:** Frontend Nginx container proxying `/api/*` to backend container via Docker network

### Backend Structure
All REST endpoints are prefixed with `/api`:
- `GET /api/bookings?date=YYYY-MM-DD`
- `POST /api/bookings`
- `DELETE /api/bookings/{id}`

Package layout: `controller/` → `service/` → `repository/` → `entity/`. DTOs in `dto/`, global exception handling in `exception/GlobalExceptionHandler`.

### Frontend Structure
- `src/services/api.js` — all API calls via `fetch()`
- `src/services/constants.js` — `API_BASE_URL` (`/api` relative path), room names, time grid (9am–6pm, 15-min slots)
- Components: `BookingForm`, `AvailabilityGrid`, `DatePicker`

### Spring Profiles
| Profile | Use |
|---|---|
| `local` | Local dev — SQL logging on, DB at `meeting_rooms.db` (relative path) |
| *(none/default)* | Docker — DB at `/app/data/meeting_rooms.db` |

Set via `SPRING_PROFILES_ACTIVE` environment variable. No profile env var needed for Docker (default config covers it).

### Database
- **Local dev** (`local` profile): `meeting_rooms.db` in the `backend/` working directory
- **Docker**: `/app/data/meeting_rooms.db` — persisted via `-v /app/data:/app/data` volume mount

### Key JVM flags (required for SQLite on Java 25)
Applied in `build.gradle` bootRun config and Dockerfile:
```
--enable-native-access=ALL-UNNAMED
--sun-misc-unsafe-memory-access=allow
```
