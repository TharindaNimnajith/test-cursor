// API Configuration
// Local dev: proxied by Vite dev server (see vite.config.js) or direct
// Production: relative path — host Nginx proxies /api/ to the backend container
export const API_BASE_URL = '/api';

// Room Names
export const ROOM_NAMES = [
  'Board Room',
  'Game Room',
  'Spirit Room',
  'Fire Room',
  'Water Room',
  'Earth Room',
  'Air Room'
];

// Time Grid Configuration
export const TIME_GRID_START_HOUR = 9;
export const TIME_GRID_END_HOUR = 18;
export const SLOT_DURATION_MINUTES = 15;
