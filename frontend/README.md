# Meeting Room Booking — Frontend

React 19 SPA built with Vite + SWC and Tailwind CSS 4.

## Development

```bash
npm install
npm run dev      # dev server on http://localhost:5173
npm run build    # production build → dist/
npm run lint     # ESLint
```

The Vite dev server proxies `/api/*` to `http://localhost:8080`, so the backend must be running locally. This mirrors production where host Nginx handles the same routing.

## API

All calls go through `API_BASE_URL = '/api'` (relative path). In development, Vite proxies this to `localhost:8080`. In production, the host Nginx on the droplet proxies it to the backend container.

## Docker

```bash
docker build -t yourdockerhubuser/meeting-room-frontend:latest .
docker run -d --name frontend -p 3000:80 yourdockerhubuser/meeting-room-frontend:latest
```

The image uses a two-stage build (Node → Nginx). The `nginx.conf` serves the static SPA build with proper `try_files` routing for client-side navigation. No environment variables are required at runtime.
