const API_BASE_URL = 'http://localhost:8080/api';

export const api = {
  async getRooms() {
    const response = await fetch(`${API_BASE_URL}/rooms`);
    if (!response.ok) throw new Error('Failed to fetch rooms');
    return response.json();
  },

  formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  },

  async getBookings(date) {
    const dateStr = this.formatDate(date);
    const response = await fetch(`${API_BASE_URL}/bookings?date=${dateStr}`);
    if (!response.ok) throw new Error('Failed to fetch bookings');
    return response.json();
  },

  async createBooking(booking) {
    const response = await fetch(`${API_BASE_URL}/bookings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        roomId: booking.roomId,
        date: this.formatDate(booking.date),
        startTime: booking.startTime,
        endTime: booking.endTime,
      }),
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.error || 'Failed to create booking');
    }
    return data;
  },

  async deleteBooking(id) {
    const response = await fetch(`${API_BASE_URL}/bookings/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const data = await response.json();
      throw new Error(data.error || 'Failed to delete booking');
    }
  },
};
