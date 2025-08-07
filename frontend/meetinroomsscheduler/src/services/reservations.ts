import api from '@/lib/api';
import { Reservation, CreateReservationRequest } from '@/types';

export const reservationService = {
  async getReservations(params?: {
    roomId?: number;
    status?: 'PENDING' | 'CONFIRMED' | 'CANCELLED';
  }): Promise<Reservation[]> {
    const response = await api.get<Reservation[]>('/api/reservations', { params });
    return response.data;
  },

  async createReservation(data: CreateReservationRequest): Promise<Reservation> {
    const response = await api.post<Reservation>('/api/reservations', data);
    return response.data;
  },

  async updateReservationStatus(id: number, status: 'PENDING' | 'CONFIRMED' | 'CANCELLED'): Promise<Reservation> {
    const response = await api.put<Reservation>(`/api/reservations/${id}/status`, { status });
    return response.data;
  },

  async cancelReservation(id: number): Promise<Reservation> {
    const response = await api.post<Reservation>(`/api/reservations/${id}/cancel`);
    return response.data;
  },

  async confirmReservation(id: number): Promise<Reservation> {
    const response = await api.post<Reservation>(`/api/reservations/${id}/confirm`);
    return response.data;
  },

  async releaseReservation(id: number): Promise<Reservation> {
    const response = await api.post<Reservation>(`/api/reservations/${id}/release`);
    return response.data;
  },

  async deleteReservation(id: number): Promise<Reservation> {
    const response = await api.delete<Reservation>(`/api/reservations/${id}`);
    return response.data;
  },
};
