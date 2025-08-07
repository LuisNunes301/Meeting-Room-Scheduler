export interface User {
  id: number;
  username: string;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER';
  createdAt: string;
}

export interface Room {
  id: number;
  name: string;
  location: string;
  capacity: number;
  available: boolean;
  description?: string;
  equipment?: string;
  price?: number;
  createdAt: string;
  updatedAt?: string;
}

export interface Reservation {
  id: number;
  room: Room;
  user: User;
  startTime: string;
  endTime: string;
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED';
  createdAt: string;
}

export interface CreateReservationRequest {
  roomId: number;
  startTime: string;
  endTime: string;
}

export interface UpdateReservationStatusRequest {
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED';
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignUpRequest {
  username: string;
  password: string;
  name: string;
  email: string;
}

export interface AuthResponse {
  message: string;
}

export interface ApiError {
  message: string;
  status: number;
}
