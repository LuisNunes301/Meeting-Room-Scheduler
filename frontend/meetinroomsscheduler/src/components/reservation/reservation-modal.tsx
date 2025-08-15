'use client';

import { useState, useEffect } from 'react';
import { Reservation, Room, CreateReservationRequest, UpdateReservationRequest } from '@/types';
import { reservationService } from '@/services/reservations';
import { roomService } from '@/services/rooms';

interface ReservationModalProps {
  mode: 'create' | 'edit';
  reservation?: Reservation;
  onClose: () => void;
  onSubmit: (data: Reservation) => void;
}

export const ReservationModal = ({ mode, reservation, onClose, onSubmit }: ReservationModalProps) => {
  const [rooms, setRooms] = useState<Room[]>([]);
  const [selectedRoomId, setSelectedRoomId] = useState<number>(reservation?.room.id || 0);
  const [startTime, setStartTime] = useState<string>(reservation?.startTime || '');
  const [endTime, setEndTime] = useState<string>(reservation?.endTime || '');
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Buscar salas disponíveis
  useEffect(() => {
    roomService.getRooms()
      .then(allRooms => setRooms(allRooms.filter(r => r.available)))
      .catch(() => setError('Failed to load rooms'));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();

  if (!selectedRoomId) return setError('Please select a room');
  if (!startTime || !endTime) return setError('Start and end time are required');

  const startDate = new Date(startTime);
  const endDate = new Date(endTime);

  if (endDate <= startDate) return setError('End time must be after start time');

  setIsSubmitting(true);
  setError(null);

  try {
    let res: Reservation;

    // Converte datetime-local para formato ISO sem 'Z'
    const toBackendOffset = (localDateTime: string) => {
  const date = new Date(localDateTime);
  return date.toISOString(); // já retorna em formato ISO com 'Z' (UTC)
};

const startISO = toBackendOffset(startTime);
const endISO = toBackendOffset(endTime);

    if (mode === 'create') {
      const payload: CreateReservationRequest = { roomId: selectedRoomId, startTime: startISO, endTime: endISO };
      res = await reservationService.createReservation(payload);
    } else if (mode === 'edit' && reservation) {
      const payload: UpdateReservationRequest = { roomId: selectedRoomId, startTime: startISO, endTime: endISO };
      res = await reservationService.updateReservationUser(reservation.id, payload);
    } else {
      throw new Error('Invalid mode');
    }

    onSubmit(res);
    onClose();
  } catch (err: any) {
    setError(err?.message || 'An error occurred');
  } finally {
    setIsSubmitting(false);
  }
};
  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-xl font-semibold mb-4">
          {mode === 'create' ? 'Make a Reservation' : 'Edit Reservation'}
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block font-medium mb-1">Select Room</label>
            <select
              className="w-full border p-2 rounded"
              value={selectedRoomId}
              onChange={(e) => setSelectedRoomId(Number(e.target.value))}
              required
            >
              <option value={0} disabled>Select a room</option>
              {rooms.map(room => (
                <option key={room.id} value={room.id}>
                  {room.name} - {room.location} (Capacity: {room.capacity})
                </option>
              ))}
            </select>
          </div>

          <div className="mb-4">
            <label className="block font-medium mb-1">Start Time</label>
            <input
              type="datetime-local"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          <div className="mb-4">
            <label className="block font-medium mb-1">End Time</label>
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          {error && <div className="text-red-500 mb-2">{error}</div>}

          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded"
              disabled={isSubmitting}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Processing...' : 'Submit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
