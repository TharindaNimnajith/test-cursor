# Meeting Room Booking — Backend

Spring Boot 4 REST API backed by SQLite, serving all endpoints under `/api`.

## Development

```bash
./gradlew bootRun          # dev server on http://localhost:8080
./gradlew clean build      # full rebuild (skips tests)
./gradlew test             # run tests
./gradlew bootJar          # build production JAR → build/libs/
```

On Windows use `gradlew.bat` instead of `./gradlew`.

Run with the `local` profile to enable SQL logging:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

The SQLite database (`meeting_rooms.db`) is created automatically in the working directory on first run. Schema is managed by Hibernate (`ddl-auto: update`).

## Configuration profiles

| Profile | Activate with | Notes |
|---|---|---|
| `local` | `SPRING_PROFILES_ACTIVE=local` | SQL logging on, DB at `meeting_rooms.db` (relative path) |
| *(none)* | default (no env var) | Used in Docker — DB at `/app/data/meeting_rooms.db` |

Profile config files in `src/main/resources/`:
- `application.yaml` — defaults (used in Docker)
- `application-local.yaml` — overrides for local dev

## Docker

Build and push:

```bash
cd backend
docker build -t tharinda1998/my-backend:latest .
docker push tharinda1998/my-backend:latest
```

Pull and run on the droplet (no docker-compose needed):

```bash
# Create a shared network (once)
docker network create app-network

docker pull tharinda1998/my-backend:latest
docker run -d \
  --name backend \
  --network app-network \
  -v /app/data:/app/data \
  tharinda1998/my-backend:latest
```

The SQLite database is persisted to `/app/data` on the host via the volume. No `SPRING_PROFILES_ACTIVE` needed — the default `application.yaml` is used in Docker and already points to `/app/data/meeting_rooms.db`.

> **Note:** The two JVM flags `--enable-native-access=ALL-UNNAMED` and `--sun-misc-unsafe-memory-access=allow` are required for SQLite on Java 25 and are already baked into the Dockerfile `ENTRYPOINT`.

## API endpoints

All routes are prefixed with `/api` (set via `server.servlet.context-path`).

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/bookings?date=YYYY-MM-DD` | Get all bookings for a date |
| `POST` | `/api/bookings` | Create a booking |
| `DELETE` | `/api/bookings/{id}` | Cancel a booking |

### POST /api/bookings request body

```json
{
  "roomName": "Board Room",
  "description": "Team standup",
  "date": "2024-12-20",
  "startTime": "09:00",
  "endTime": "10:00"
}
```

### Error response shape

```json
{
  "status": 400,
  "message": "This room is already booked for the selected time slot."
}
```

## Package structure

```
com.example.demo/
├── controller/   BookingController — maps HTTP to service calls
├── service/      BookingService — overlap validation & business rules
├── repository/   BookingRepository — JPA data access
├── entity/       Booking — JPA entity
├── dto/          BookingRequest, ErrorResponse
├── exception/    BookingException, ResourceNotFoundException, GlobalExceptionHandler
└── config/       CorsConfig — allows all origins for GET/POST/DELETE
```
