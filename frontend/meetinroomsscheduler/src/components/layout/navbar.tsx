'use client';

import { useAuthStore } from '@/store/auth';
import { CalendarDays, LogOut, User } from 'lucide-react';

export function Navbar() {
  const { user, logout } = useAuthStore();

  const handleLogout = async () => {
    await logout();
    window.location.href = '/login';
  };

  return (
    <nav className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <div className="flex-shrink-0 flex items-center">
              <CalendarDays className="h-8 w-8 text-blue-600" />
              <span className="ml-2 text-xl font-bold text-gray-900">Meeting Scheduler</span>
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <a
              href="/dashboard"
              className="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              Dashboard
            </a>
            <a
              href="/reservation"
              className="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              My Reservations
            </a>
            <a
              href="/room"
              className="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              Rooms
            </a>
            {user?.role === 'ADMIN' && (
              <a
                href="/admin"
                className="text-red-600 hover:text-red-700 px-3 py-2 rounded-md text-sm font-medium border border-red-200 hover:border-red-300"
              >
                Admin Panel
              </a>
            )}

            <div className="flex items-center space-x-2">
              <div className="flex items-center space-x-1">
                <User className="h-4 w-4 text-gray-500" />
                <span className="text-sm text-gray-700">{user?.name}</span>
              </div>
              <button
                onClick={handleLogout}
                className="text-gray-700 hover:text-gray-900 p-2 rounded-md"
                title="Logout"
              >
                <LogOut className="h-4 w-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}
