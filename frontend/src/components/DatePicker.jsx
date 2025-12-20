function DatePicker({ date, onDateChange }) {
  return (
    <div className="mb-6">
      <label htmlFor="date" className="block text-sm font-medium text-gray-700 mb-2">
        Select Date
      </label>
      <input
        type="date"
        id="date"
        value={date.toISOString().split('T')[0]}
        onChange={(e) => onDateChange(new Date(e.target.value))}
        className="px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      />
    </div>
  );
}

export default DatePicker;
