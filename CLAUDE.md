# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Meeting Room Booking System — a full-stack app with a React frontend and Spring Boot backend using SQLite.

## Commands

### Backend (Spring Boot + Gradle)
```bash
cd backend
./gradlew bootRun                  # Dev server on :8080
./gradlew clean build -x test      # Full rebuild
./gradlew bootJar                  # Create production JAR
./gradlew test                     # Run tests
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

### Docker (local)
```bash
./deploy-docker.sh                 # Build & run with Docker Compose
# Or manually:
docker-compose -f docker-compose.yml -f docker-compose.docker.yml up -d
```

## Architecture

### Tech Stack
- **Frontend:** React 19, Vite + SWC, Tailwind CSS 4
- **Backend:** Spring Boot 4, Java 25, Gradle
- **Database:** SQLite (JPA/Hibernate with community dialect, auto-update schema)
- **Proxy:** Nginx (production) serving SPA + proxying `/api/*` to backend

### Backend Structure
All REST endpoints are prefixed with `/api`:
- `GET /api/bookings?date=YYYY-MM-DD`
- `POST /api/bookings`
- `DELETE /api/bookings/{id}`

Package layout: `controller/` → `service/` → `repository/` → `entity/`. DTOs in `dto/`, global exception handling in `exception/GlobalExceptionHandler`.

### Frontend Structure
- `src/services/api.js` — all API calls via `fetch()`
- `src/constants.js` — `API_BASE_URL` (from `import.meta.env.API_BASE_URL`), room names, time grid (9am–6pm, 15-min slots)
- Components: `BookingForm`, `AvailabilityGrid`, `DatePicker`

### Spring Profiles
| Profile | Use |
|---|---|
| `local` (default) | Local dev, CORS allows `localhost:5173` |
| `docker` | Docker Compose, CORS allows `localhost` |
| `prod-digitalocean` | DigitalOcean VPS |
| `prod-flyio` | Fly.io (backend at `meeting-room-backend.fly.dev`) |

Set via `SPRING_PROFILES_ACTIVE` environment variable.

### Database
SQLite file: `meeting_rooms.db` in the backend working directory. In Docker, persisted to `/app/data/` via the `db-data` volume.

### Key JVM flags (required for SQLite on Java 25)
Applied in `build.gradle` bootRun config and Dockerfile:
```
--enable-native-access=ALL-UNNAMED
--sun-misc-unsafe-memory-access=allow
```
