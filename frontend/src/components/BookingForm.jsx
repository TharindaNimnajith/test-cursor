import { useState } from 'react';
import { ROOM_NAMES, DEFAULT_START_TIME, DEFAULT_END_TIME } from '../constants';

function BookingForm({ date, onCreateBooking }) {
  const [roomName, setRoomName] = useState(ROOM_NAMES[0]);
  const [description, setDescription] = useState('');
  const [startTime, setStartTime] = useState(DEFAULT_START_TIME);
  const [endTime, setEndTime] = useState(DEFAULT_END_TIME);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const isPastBooking = () => {
    // Check if booking is for a past date/time
    const today = new Date();
    const bookingDate = new Date(date);
    bookingDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    if (bookingDate < today) return true;
    if (bookingDate > today) return false;
    // Booking is for today - check if start time has passed
    const now = new Date();
    const [startHour, startMin] = startTime.split(':').map(Number);
    const start = new Date(date);
    start.setHours(startHour, startMin, 0, 0);
    return start < now;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!description.trim()) {
      setError('Please enter a description.');
      return;
    }

    if (description.trim().length < 3) {
      setError('Description must be at least 3 characters long.');
      return;
    }

    if (endTime <= startTime) {
      setError('End time must be after start time.');
      return;
    }

    if (isPastBooking()) {
      setError('Cannot book for past times.');
      return;
    }

    try {
      await onCreateBooking({
        roomName,
        description: description.trim(),
        date,
        startTime,
        endTime,
      });
      setSuccess('Booking created successfully.');
      setDescription('');
      setStartTime(DEFAULT_START_TIME);
      setEndTime(DEFAULT_END_TIME);
    } catch (err) {
      setError(err.message || 'Failed to create booking.');
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h2 className="text-xl font-bold mb-4">Create Booking</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="roomName" className="block text-sm font-medium text-gray-700 mb-1">
            Room
          </label>
          <select
            id="roomName"
            value={roomName}
            onChange={(e) => setRoomName(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
            {ROOM_NAMES.map((name) => (
              <option key={name} value={name}>{name}</option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <input
            type="text"
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Enter booking description"
            required
          />
        </div>
        <div>
          <label htmlFor="startTime" className="block text-sm font-medium text-gray-700 mb-1">
            Start Time
          </label>
          <input
            type="time"
            id="startTime"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            required
          />
        </div>
        <div>
          <label htmlFor="endTime" className="block text-sm font-medium text-gray-700 mb-1">
            End Time
          </label>
          <input
            type="time"
            id="endTime"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            required
          />
        </div>
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
        )}
        {success && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
            {success}
          </div>
        )}
        <button
          type="submit"
          className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md transition-colors"
        >
          Create Booking
        </button>
      </form>
    </div>
  );
}

export default BookingForm;
