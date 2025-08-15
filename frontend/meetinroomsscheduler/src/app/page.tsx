'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth';
import { CalendarDays, Building2, Clock, Users } from 'lucide-react';

export default function HomePage() {
  const { isAuthenticated, getCurrentUser } = useAuthStore();
  const router = useRouter();

  useEffect(() => {
    // Only redirect if user is already authenticated
    if (isAuthenticated) {
      router.push('/dashboard');
    }
  }, [isAuthenticated, router]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <CalendarDays className="h-8 w-8 text-blue-600" />
              <span className="ml-2 text-xl font-bold text-gray-900">Meeting Scheduler</span>
            </div>
            <div className="flex items-center space-x-4">
              <a
                href="/login"
                className="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                Sign In
              </a>
              <a href="/signup" className="btn-primary">
                Get Started
              </a>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center">
          <h1 className="text-4xl font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
            <span className="block">Simplify Your</span>
            <span className="block text-blue-600">Meeting Room Booking</span>
          </h1>
          <p className="mt-6 max-w-2xl mx-auto text-xl text-gray-600">
            Streamline your meeting room reservations with our intuitive scheduling platform. Book
            rooms, manage schedules, and coordinate meetings effortlessly.
          </p>
          <div className="mt-10 flex justify-center space-x-4">
            <a href="/signup" className="btn-primary text-lg px-8 py-3">
              Start Booking Now
            </a>
            <a href="/login" className="btn-secondary text-lg px-8 py-3">
              Sign In
            </a>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center mb-16">
          <h2 className="text-3xl font-bold text-gray-900">
            Everything you need to manage meeting rooms
          </h2>
          <p className="mt-4 text-lg text-gray-600">
            Powerful features to streamline your room booking process
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div className="text-center">
            <div className="flex justify-center mb-4">
              <CalendarDays className="h-12 w-12 text-blue-600" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Easy Booking</h3>
            <p className="text-gray-600">
              Book meeting rooms with just a few clicks. Simple and intuitive interface.
            </p>
          </div>

          <div className="text-center">
            <div className="flex justify-center mb-4">
              <Building2 className="h-12 w-12 text-green-600" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Room Management</h3>
            <p className="text-gray-600">
              View all available rooms and their current status at a glance.
            </p>
          </div>

          <div className="text-center">
            <div className="flex justify-center mb-4">
              <Clock className="h-12 w-12 text-yellow-600" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Real-time Updates</h3>
            <p className="text-gray-600">
              Get instant notifications and updates on your reservations.
            </p>
          </div>

          <div className="text-center">
            <div className="flex justify-center mb-4">
              <Users className="h-12 w-12 text-purple-600" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Team Collaboration</h3>
            <p className="text-gray-600">
              Coordinate with your team and manage group bookings efficiently.
            </p>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-blue-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-white mb-4">Ready to get started?</h2>
            <p className="text-xl text-blue-100 mb-8">
              Join thousands of teams already using our platform
            </p>
            <a
              href="/signup"
              className="bg-white text-blue-600 font-semibold py-3 px-8 rounded-lg hover:bg-gray-100 transition-colors duration-200"
            >
              Create Your Account
            </a>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-gray-900">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="text-center">
            <CalendarDays className="h-8 w-8 text-blue-600 mx-auto mb-4" />
            <p className="text-gray-400">© 2024 Meeting Room Scheduler. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
