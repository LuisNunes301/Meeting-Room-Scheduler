'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth';
import { Navbar } from '@/components/layout/navbar';
import { CalendarDays, Building2, Clock, CheckCircle } from 'lucide-react';
import { Reservation, Room } from '@/types';
import { roomService } from '@/services/rooms';
import { reservationService } from '@/services/reservations';

export default function DashboardPage() {
  const { user, isAuthenticated, getCurrentUser } = useAuthStore();
  const [rooms, setRooms] = useState<Room[]>([]);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      getCurrentUser().catch(() => {
        router.push('/login');
      });
    }
  }, [isAuthenticated, router]);

  useEffect(() => {
    async function fetchData() {
      try {
        const [roomsData, reservationsData] = await Promise.all([
          roomService.getRooms(),
          reservationService.getReservations(),
        ]);
        setRooms(roomsData);
        setReservations(reservationsData);
      } catch (error) {
        // Optionally handle error
      }
    }
    fetchData();
  }, []);

  if (!isAuthenticated || !user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <h1 className="text-3xl font-bold text-gray-900 mb-8">
            Welcome back, {user.name}!
          </h1>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <div className="card">
              <div className="flex items-center">
                <CalendarDays className="h-8 w-8 text-blue-600" />
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">Total Reservations</p>
                  <p className="text-2xl font-semibold text-gray-900"> {reservations.length}</p>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex items-center">
                <Building2 className="h-8 w-8 text-green-600" />
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">Available Rooms</p>
                  <p className="text-2xl font-semibold text-gray-900">{rooms.filter(room => room.available).length}</p>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex items-center">
                <Clock className="h-8 w-8 text-yellow-600" />
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">Pending</p>
                  <p className="text-2xl font-semibold text-gray-900">{reservations.filter(reservation => reservation.status === 'PENDING').length}</p>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex items-center">
                <CheckCircle className="h-8 w-8 text-green-600" />
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">Confirmed</p>
                  <p className="text-2xl font-semibold text-gray-900">{reservations.filter(reservation => reservation.status === 'CONFIRMED').length}</p>
                </div>
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Quick Actions
              </h2>
              <div className="space-y-3">
                <a
                  href="/reservation"
                  className="btn-primary block text-center"
                >
                  Book a Room
                </a>
                <a
                  href="/reservation"
                  className="btn-secondary block text-center"
                >
                  View My Reservations
                </a>
                <a
                  href="/room"
                  className="btn-secondary block text-center"
                >
                  Browse Available Rooms
                </a>
              </div>
            </div>

            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Recent Activity
              </h2>
              <div className="text-gray-600 text-sm">
                <p>No recent activity</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
