import api from '@/lib/api';
import { Room } from '@/types';

export const roomService = {
  async getRooms(): Promise<Room[]> {
    const response = await api.get<Room[]>('/api/rooms');
    return response.data;
  },

  async getRoom(id: number): Promise<Room> {
    const response = await api.get<Room>(`/api/rooms/${id}`);
    return response.data;
  },
};
