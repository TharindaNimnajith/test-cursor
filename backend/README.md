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

The SQLite database (`meeting_rooms.db`) is created automatically in the working directory on first run. Schema is managed by Hibernate (`ddl-auto: update`).

## Configuration profiles

| Profile | Activate with | Notes |
|---|---|---|
| `local` | default | SQL logging on, CORS open |
| `docker` | `SPRING_PROFILES_ACTIVE=docker` | for Docker Compose local |
| `prod-digitalocean` | `SPRING_PROFILES_ACTIVE=prod-digitalocean` | DigitalOcean droplet |
| `prod-flyio` | `SPRING_PROFILES_ACTIVE=prod-flyio` | Fly.io |

Profile-specific YAML files in `src/main/resources/` override `application.yaml`.

## Docker

```bash
docker build -t yourdockerhubuser/meeting-room-backend:latest .
docker push yourdockerhubuser/meeting-room-backend:latest

# On the droplet:
docker run -d --name backend \
  -p 8080:8080 \
  -v /opt/meeting-rooms/data:/app/data \
  -e SPRING_PROFILES_ACTIVE=prod-digitalocean \
  yourdockerhubuser/meeting-room-backend:latest
```

The image uses a two-stage build (JDK 25 builder → JRE 25 runtime). SQLite is persisted via the volume mounted to `/app/data`. JVM is capped at 256 MB (`-Xmx256m`).

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
├── service/      BookingService — validation & business rules
├── repository/   BookingRepository — JPA data access
├── entity/       Booking — JPA entity
├── dto/          BookingRequest, ErrorResponse
├── exception/    BookingException, ResourceNotFoundException, GlobalExceptionHandler
└── config/       CorsConfig — allows all origins for GET/POST/DELETE
```
