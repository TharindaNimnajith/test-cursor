import { useState, useEffect } from 'react';
import DatePicker from './components/DatePicker';
import AvailabilityGrid from './components/AvailabilityGrid';
import BookingForm from './components/BookingForm';
import { api } from './services/api';

function App() {
  const [date, setDate] = useState(new Date());
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const loadBookings = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await api.getBookings(date);
      setBookings(data);
    } catch (err) {
      setError('Failed to load bookings: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBookings();
  }, [date]);

  const handleCreateBooking = async (bookingData) => {
    try {
      await api.createBooking(bookingData);
      await loadBookings();
    } catch (err) {
      throw err;
    }
  };

  const handleCancelBooking = async (id) => {
    try {
      await api.deleteBooking(id);
      await loadBookings();
    } catch (err) {
      alert('Failed to cancel booking: ' + err.message);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
          Meeting Room Booking System
        </h1>
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <DatePicker date={date} onDateChange={setDate} />
        </div>
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6">
            {error}
          </div>
        )}
        {loading ? (
          <div className="text-center py-8">Loading...</div>
        ) : (
          <div className="bg-white p-6 rounded-lg shadow-md mb-6">
            <h2 className="text-xl font-bold mb-4">Room Availability</h2>
            <AvailabilityGrid
              date={date}
              bookings={bookings}
              onCancelBooking={handleCancelBooking}
            />
          </div>
        )}
        <BookingForm date={date} onCreateBooking={handleCreateBooking} />
      </div>
    </div>
  );
}

export default App;
