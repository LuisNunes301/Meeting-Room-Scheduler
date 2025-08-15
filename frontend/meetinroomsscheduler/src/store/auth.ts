import { create } from 'zustand';
import { User } from '@/types';
import { authService } from '@/services/auth';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  isAuthResolved: boolean;
}

interface AuthActions {
  login: (username: string, password: string) => Promise<void>;
  signup: (userData: {
    username: string;
    password: string;
    name: string;
    email: string;
  }) => Promise<void>;
  logout: () => Promise<void>;
  getCurrentUser: () => Promise<User | null>;
  clearError: () => void;
  setUser: (user: User | null) => void;
}

export const useAuthStore = create<AuthState & AuthActions>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,
  isAuthResolved: false,
  login: async (username, password) => {
    set({ isLoading: true, error: null });
    try {
      await authService.login({ username, password });
      const user = await authService.getCurrentUser();
      set({ user, isAuthenticated: true });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Login failed',
        user: null,
        isAuthenticated: false,
      });
    } finally {
      set({ isLoading: false });
    }
  },

  signup: async (userData) => {
    set({ isLoading: true, error: null });
    try {
      await authService.signup(userData);
      const user = await authService.getCurrentUser();
      set({ user, isAuthenticated: true });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Signup failed',
        user: null,
        isAuthenticated: false,
      });
    } finally {
      set({ isLoading: false });
    }
  },

  logout: async () => {
    set({ isLoading: true });
    try {
      await authService.logout();
      set({ user: null, isAuthenticated: false });
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      set({ isLoading: false });
    }
  },

  getCurrentUser: async () => {
    try {
      const user = await authService.getCurrentUser();
      set({ user, isAuthenticated: true, isAuthResolved: true });
      return user;
    } catch (error) {
      set({ user: null, isAuthenticated: false, isAuthResolved: true });
      return null;
    }
  },

  clearError: () => set({ error: null }),
  setUser: (user) => set({ user, isAuthenticated: !!user }),
}));
