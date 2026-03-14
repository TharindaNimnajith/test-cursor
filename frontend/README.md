# Meeting Room Booking — Frontend

React 19 SPA built with Vite + SWC and Tailwind CSS 4.

## Development

```bash
npm install
npm run dev      # dev server on http://localhost:5173
npm run build    # production build → dist/
npm run lint     # ESLint
```

The Vite dev server proxies `/api/*` to `http://localhost:8080`, so the backend must be running locally.

## API

All calls go through `API_BASE_URL = '/api'` (relative path, defined in `src/services/constants.js`). In development, Vite proxies this to `localhost:8080`. In production, the container's Nginx proxies it to the backend container on the Docker network.

## Docker

Build and push:

```bash
cd frontend
docker build -t tharinda1998/my-frontend:latest .
docker push tharinda1998/my-frontend:latest
```

Pull and run on the droplet:

```bash
docker pull tharinda1998/my-frontend:latest
docker run -d \
  --name frontend \
  --network app-network \
  -p 80:80 \
  tharinda1998/my-frontend:latest
```

The `--network app-network` lets Nginx resolve the `backend` hostname (matching the `--name backend` on the backend container). The `nginx.conf` baked into the image proxies `/api/` to `http://backend:8080` and serves the SPA with `try_files` for client-side routing.

No environment variables required at runtime.
