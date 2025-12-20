import { useEffect, useState } from 'react';
import { ROOM_NAMES, TIME_GRID_START_HOUR, TIME_GRID_END_HOUR, SLOT_DURATION_MINUTES } from '../constants';

function AvailabilityGrid({ date, bookings, onCancelBooking }) {
  const [currentTime, setCurrentTime] = useState(null);
  const timeSlots = [];

  for (let hour = TIME_GRID_START_HOUR; hour < TIME_GRID_END_HOUR; hour++) {
    for (let minute = 0; minute < 60; minute += SLOT_DURATION_MINUTES) {
      const time = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
      timeSlots.push(time);
    }
  }

  useEffect(() => {
    const updateCurrentTime = () => {
      const now = new Date();
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const selectedDate = new Date(date);
      selectedDate.setHours(0, 0, 0, 0);

      if (today.getTime() === selectedDate.getTime()) {
        const hours = now.getHours();
        const minutes = now.getMinutes();
        setCurrentTime(`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`);
      } else {
        setCurrentTime(null);
      }
    };

    updateCurrentTime();
    const interval = setInterval(updateCurrentTime, 60000); // Update every minute
    return () => clearInterval(interval);
  }, [date]);

  const parseTime = (timeStr) => {
    // Handle both 'HH:mm' and 'HH:mm:ss' formats
    const parts = timeStr.substring(0, 5).split(':');
    return parseInt(parts[0]) * 60 + parseInt(parts[1]); // Convert to minutes
  };

  const isTimeSlotBooked = (roomName, timeSlot) => {
    return bookings.some(booking => {
      if (booking.roomName !== roomName) return false;
      const bookingStart = parseTime(booking.startTime);
      const bookingEnd = parseTime(booking.endTime);
      const slotTime = parseTime(timeSlot);
      const nextSlotTime = slotTime + SLOT_DURATION_MINUTES; // Each slot duration
      // Slot is booked if it overlaps with booking: slotStart < bookingEnd AND slotEnd > bookingStart
      return slotTime < bookingEnd && nextSlotTime > bookingStart;
    });
  };

  const getBookingForSlot = (roomName, timeSlot) => {
    return bookings.find(booking => {
      if (booking.roomName !== roomName) return false;
      const bookingStart = parseTime(booking.startTime);
      const bookingEnd = parseTime(booking.endTime);
      const slotTime = parseTime(timeSlot);
      const nextSlotTime = slotTime + SLOT_DURATION_MINUTES;
      // Return booking if slot overlaps with booking
      return slotTime < bookingEnd && nextSlotTime > bookingStart;
    });
  };

  const isCurrentTime = (timeSlot) => {
    if (!currentTime) return false;
    const [currentHour, currentMin] = currentTime.split(':').map(Number);
    const currentMinutes = currentHour * 60 + currentMin;
    const [slotHour, slotMin] = timeSlot.split(':').map(Number);
    const slotMinutes = slotHour * 60 + slotMin;
    // Highlight if current time falls within this time slot
    return currentMinutes >= slotMinutes && currentMinutes < slotMinutes + SLOT_DURATION_MINUTES;
  };

  const isPastBooking = (booking) => {
    // Check if booking is in the past
    const today = new Date();
    const bookingDate = new Date(booking.date);
    bookingDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    if (bookingDate < today) return true;
    if (bookingDate > today) return false;
    // Booking is for today - check if start time has passed
    const now = new Date();
    const [startHour, startMin] = booking.startTime.split(':').map(Number);
    const start = new Date(booking.date);
    start.setHours(startHour, startMin, 0, 0);
    return start < now;
  };

  return (
    <div className="overflow-x-auto mb-6">
      <table className="min-w-full border-collapse">
        <thead>
          <tr>
            <th className="border border-gray-300 px-4 py-2 bg-gray-100 font-semibold">Room</th>
            {timeSlots.map(slot => (
              <th key={slot} className="border border-gray-300 px-2 py-1 bg-gray-100 text-xs">
                {slot}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {ROOM_NAMES.map((roomName) => (
            <tr key={roomName}>
              <td className="border border-gray-300 px-4 py-2 bg-gray-50 font-medium">
                {roomName}
              </td>
              {timeSlots.map(timeSlot => {
                const isBooked = isTimeSlotBooked(roomName, timeSlot);
                const booking = getBookingForSlot(roomName, timeSlot);
                const isCurrent = isCurrentTime(timeSlot);

                return (
                  <td
                    key={timeSlot}
                    className={`border border-gray-300 px-1 py-1
                       ${isBooked ? 'bg-red-200 hover:bg-red-300 cursor-pointer' : 'bg-green-100'}
                       ${isCurrent ? 'ring-2 ring-blue-500 ring-inset' : ''}`}
                    title={isBooked ? booking.description : 'Available'}
                    onClick={() => {
                      if (isBooked && booking) {
                        if (isPastBooking(booking)) {
                          alert('Cannot cancel past bookings.');
                          return;
                        }
                        if (window.confirm(`Cancel booking "${booking.description}" from ${booking.startTime} to ${booking.endTime}?`)) {
                          onCancelBooking(booking.id);
                        }
                      }
                    }}
                  />
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AvailabilityGrid;
