import { useState } from 'react';

function DatePicker({ date, onDateChange }) {
  const [inputValue, setInputValue] = useState(date.toISOString().split('T')[0]);
  // Keep input value in sync with date prop
  if (date && inputValue !== date.toISOString().split('T')[0]) {
    setInputValue(date.toISOString().split('T')[0]);
  }

  const handleChange = (e) => {
    const newDate = new Date(e.target.value);
    if (!isNaN(newDate.getTime())) {
      setInputValue(e.target.value);
      onDateChange(newDate);
    }
  };

  const handleClear = () => {
    const today = new Date();
    const todayStr = today.toISOString().split('T')[0];
    setInputValue(todayStr);
    onDateChange(today);
  };

  return (
    <div className="mb-6 flex space-x-2 items-end">
      <div className="flex-1">
        <label htmlFor="date" className="block text-sm font-medium text-gray-700 mb-2">
          Select Date
        </label>
        <input
          type="date"
          id="date"
          value={inputValue}
          onChange={handleChange}
          className="px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <button
        type="button"
        onClick={handleClear}
        className="ml-2 bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-md border border-gray-300"
      >
        Today
      </button>
    </div>
  );
}

export default DatePicker;
