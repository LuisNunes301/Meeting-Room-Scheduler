'use client';

import { useState, useEffect } from 'react';
import { Reservation } from '@/types';
import { reservationService } from '@/services/reservations';
import { Navbar } from '@/components/layout/navbar';
import { ReservationModal } from '@/components/reservation/reservation-modal';

export default function ReservationPage() {
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchReservations = async () => {
    try {
      setLoading(true);
      const data = await reservationService.getReservations();
      setReservations(data);
    } catch (err) {
      setError('Failed to load reservations');
      console.error('Error fetching reservations:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReservations();
  }, []);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  const handleReservationSubmit = (reservation: Reservation) => {
    setReservations(prev => [reservation, ...prev]);
  };

  const handleStatusChange = async (reservation: Reservation, newStatus: 'CONFIRMED' | 'CANCELLED') => {
    try {
      const updated = await reservationService.updateReservationUser(reservation.id, {
        startTime: reservation.startTime,
        endTime: reservation.endTime,
        roomId: reservation.room.id,
        status: newStatus
      });
      setReservations(prev =>
        prev.map(r => r.id === reservation.id ? updated : r)
      );
    } catch (err) {
      console.error('Failed to update reservation status', err);
      setError('Failed to update reservation status');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'bg-green-100 text-green-800';
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'CANCELLED': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8 flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">My Reservations</h1>
            <p className="mt-2 text-gray-600">View and manage your meeting room reservations</p>
          </div>
          <button
            onClick={handleOpenModal}
            className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
          >
            Make a Reservation
          </button>
        </div>

        {reservations.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-gray-500 text-lg">No reservations found</div>
            <p className="text-gray-400 mt-2">You haven't made any reservations yet.</p>
          </div>
        ) : (
          <div className="space-y-6">
            {reservations.map(reservation => (
              <div key={reservation.id} className="bg-white rounded-lg shadow-md p-6">
                <div className="flex items-center justify-between mb-4">
                  <div>
                    <h3 className="text-xl font-semibold text-gray-900">{reservation.room.name}</h3>
                    <p className="text-sm text-gray-600">{reservation.room.location}</p>
                  </div>
                  <span
                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                      reservation.status
                    )}`}
                  >
                    {reservation.status}
                  </span>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                  <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-1">Start Time</h4>
                    <p className="text-sm text-gray-900">{new Date(reservation.startTime).toISOString().slice(0, 16).replace('T', ' ')}</p>
                  </div>
                  <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-1">End Time</h4>
                    <p className="text-sm text-gray-900">{new Date(reservation.endTime).toISOString().slice(0, 16).replace('T', ' ')}</p>
                  </div>
                </div>

                {/* Botões de ação */}
                <div className="flex gap-2 mt-4">
                  {reservation.status === 'PENDING' && (
                    <>
                      <button
                        onClick={() => handleStatusChange(reservation, 'CONFIRMED')}
                        className="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600"
                      >
                        Confirm
                      </button>
                      <button
                        onClick={() => handleStatusChange(reservation, 'CANCELLED')}
                        className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
                      >
                        Cancel
                      </button>
                    </>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {isModalOpen && (
        <ReservationModal
          mode="create"
          onClose={handleCloseModal}
          onSubmit={handleReservationSubmit}
        />
      )}
    </div>
  );
}
