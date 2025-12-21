#!/bin/bash
set -e  # Exit immediately if a command fails

echo "🚀 Building Meeting Room Booking System..."

# -----------------------------
# Build backend
# -----------------------------
echo "📦 Building backend..."
cd backend || exit
./gradlew clean build -x test
cd ..

# -----------------------------
# Build frontend
# -----------------------------
echo "📦 Building frontend..."
cd frontend || exit
npm install
npm run build
cd ..

# -----------------------------
# Stop old containers
# -----------------------------
echo "🛑 Stopping old containers..."
docker-compose down

# -----------------------------
# Build Docker images
# -----------------------------
echo "🐳 Building Docker images..."
docker-compose build --no-cache

# -----------------------------
# Start services
# -----------------------------
echo "🚀 Starting services..."
docker-compose up -d

# -----------------------------
# Prune dangling images
# -----------------------------
echo "🧹 Cleaning up dangling images..."
docker image prune -f

# -----------------------------
# Info
# -----------------------------
echo "✅ Deployment complete."
echo "Frontend: http://localhost"
echo "Backend API: http://localhost:8080/api"
echo "View logs with: docker-compose logs -f"

# -----------------------------
# Logs
# -----------------------------
docker-compose logs -f
