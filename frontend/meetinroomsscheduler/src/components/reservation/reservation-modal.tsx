'use client';

import { useState, useEffect } from 'react';
import { Reservation, Room } from '@/types';
import { reservationService } from '@/services/reservations';
import { format, parseISO } from 'date-fns';
import { useAuthStore } from '@/store/auth';

const { getCurrentUser } = useAuthStore();
interface ReservationProps {
    mode: 'create';
    room?: Room;
    user: ReturnType<typeof getCurrentUser>;
    reservation?: Reservation;
    onClose: () => void;
    onSubmit: (data: Reservation) => void;
}

export function ReservationModal({ mode, room, user, reservation, onClose, onSubmit }: ReservationProps) {
    const [formData, setFormData] = useState<{
        startTime: string;
        endTime: string;
    }>({
        startTime: reservation?.startTime || '',
        endTime: reservation?.endTime || ''
    });

    const [error, setError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        if (reservation) {
            setFormData({
                startTime: reservation.startTime,
                endTime: reservation.endTime
            });
        }
    }, [reservation]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setIsSubmitting(true);

        try {
            if (!room) {
                throw new Error('Room is not selected');
            }

            const startTime = new Date(formData.startTime);
            const endTime = new Date(formData.endTime);

            if (startTime >= endTime) {
                throw new Error('End time must be after start time');
            }

            if (mode === 'create') {
                const newReservation = await reservationService.createReservation({
                    roomId: room.id,
                    startTime: formData.startTime,
                    endTime: formData.endTime
                });

                onSubmit(newReservation);
            }

            onClose();
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An unknown error occurred');
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!room) {
        return null;
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
            <div className="bg-white rounded-lg p-6 w-full max-w-md">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-xl font-bold">
                        {mode === 'create' ? 'Create Reservation' : 'Edit Reservation'}
                    </h2>
                    <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
                        &times;
                    </button>
                </div>

                <div className="mb-4">
                    <p className="font-semibold">Room: {room.name}</p>
                    <p>Location: {room.location}</p>
                    <p>Capacity: {room.capacity}</p>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Start Time
                        </label>
                        <input
                            type="datetime-local"
                            name="startTime"
                            value={formData.startTime}
                            onChange={handleChange}
                            className="w-full p-2 border rounded"
                            required
                        />
                    </div>

                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            End Time
                        </label>
                        <input
                            type="datetime-local"
                            name="endTime"
                            value={formData.endTime}
                            onChange={handleChange}
                            className="w-full p-2 border rounded"
                            required
                        />
                    </div>

                    {error && (
                        <div className="mb-4 text-red-500 text-sm">
                            {error}
                        </div>
                    )}

                    <div className="flex justify-end space-x-2">
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
}