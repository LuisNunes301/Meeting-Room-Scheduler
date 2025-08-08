import api from '@/lib/api';
import { AuthResponse, UpdateCurrentUserRequest, UpdateUserRequest, User, UserResponse } from '@/types';

export const userService = {
  getAllUsers: async (): Promise<User[]> => {
    const response = await api.get<User[]>('/api/users');
    return response.data;
  },

  getUserById: async (id: string): Promise<User> => {
    const response = await api.get<User>(`/api/users/${id}`);
    return response.data;
  },

  createUser: async (user: User): Promise<User> => {
    const response = await api.post<User>('/api/users', user);
    return response.data;
  },

 
  updateUser: async (id: number, user: UpdateUserRequest): Promise<User> => {
    const response = await api.put<User>(`/api/users/${id}`, user);
    return response.data;
  },

  updateCurrentUser: async (user: UpdateCurrentUserRequest): Promise<User> => {
    const response = await api.put<User>('/api/users/me', user);
    return response.data;
  },
  deleteUser: async (id: string): Promise<void> => {
    await api.delete(`/api/users/${id}`);
  },
  forgotPassword: async (email: string): Promise<AuthResponse> => {
    const response = await api.post('/api/users/forgot-password', { email });
    return response.data;
  },

  resetPassword: async (token: string, newPassword: string): Promise<AuthResponse> => {
    const response = await api.post('/api/users/reset-password', { token, newPassword });
    return response.data;
  },
};

export default userService;
