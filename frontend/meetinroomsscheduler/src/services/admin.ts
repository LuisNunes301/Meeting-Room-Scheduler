import api from '@/lib/api';
import { User, Room, Reservation } from '@/types';

export const adminService = {
  // User Management
  async getAllUsers(): Promise<User[]> {
    const response = await api.get<User[]>('/api/users');
    return response.data;
  },

  async createUser(userData: {
    username: string;
    password: string;
    name: string;
    email: string;
    role: 'ADMIN' | 'USER';
  }): Promise<User> {
    const response = await api.post<User>('/api/users', userData);
    return response.data;
  },

  async updateUser(id: number, userData: Partial<User>): Promise<User> {
    const response = await api.put<User>(`/api/users/${id}`, userData);
    return response.data;
  },

  async deleteUser(id: number): Promise<void> {
    await api.delete(`/api/users/${id}`);
  },

  // Room Management
  async getAllRooms(): Promise<Room[]> {
    const response = await api.get<Room[]>('/api/rooms');
    return response.data;
  },

  async createRoom(roomData: {
    name: string;
    location: string;
    capacity: number;
    available: boolean;
    description?: string;
    equipment?: string;
    price?: number;
  }): Promise<Room> {
    const response = await api.post<Room>('/api/rooms', roomData);
    return response.data;
  },

  async updateRoom(id: number, roomData: Partial<Room>): Promise<Room> {
    const response = await api.put<Room>(`/api/rooms/${id}`, roomData);
    return response.data;
  },

  async deleteRoom(id: number): Promise<void> {
    await api.delete(`/api/rooms/${id}`);
  },

  // Reservation Management
  async getAllReservations(): Promise<Reservation[]> {
    const response = await api.get<Reservation[]>('/api/reservations');
    return response.data;
  },

  async updateReservationStatus(id: number, status: 'PENDING' | 'CONFIRMED' | 'CANCELLED'): Promise<Reservation> {
    const response = await api.put<Reservation>(`/api/reservations/${id}/status`, { status });
    return response.data;
  },

  async deleteReservation(id: number): Promise<void> {
    await api.delete(`/api/reservations/${id}`);
  },

  // System Statistics
  async getSystemStats(): Promise<{
    totalUsers: number;
    totalRooms: number;
    totalReservations: number;
    pendingReservations: number;
  }> {
    const response = await api.get('/api/admin/stats');
    return response.data;
  },

  // Settings
  async getSettings(): Promise<Record<string, unknown>> {
    const response = await api.get('/api/admin/settings');
    return response.data;
  },

  async updateSettings(settings: Record<string, unknown>): Promise<void> {
    await api.put('/api/admin/settings', settings);
  },
};
