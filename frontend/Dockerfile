# -----------------------------
# Builder stage (Node.js)
# -----------------------------
FROM node:lts-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package.json & package-lock.json first for caching dependencies
COPY package*.json ./

# Install dependencies (faster build if package files haven't changed)
RUN npm ci

# Copy the rest of the source code
COPY . .

# Build the frontend (produces /app/dist folder)
RUN npm run build

# -----------------------------
# Production stage (Nginx)
# -----------------------------
FROM nginx:alpine

# Copy the built frontend from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom Nginx configuration template
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template

# Expose port 80 for HTTP
EXPOSE 80

# Start Nginx with envsubst to replace $BACKEND_URL
CMD envsubst '$BACKEND_URL' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'
