'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth';
import { AdminNavbar } from '@/components/admin/admin-navbar';
import { Building2, Plus, Edit, Trash2, Search, MapPin, Users, AlertCircle } from 'lucide-react';
import { Room } from '@/types';
import { adminService } from '@/services/admin';
import { RoomModal } from '@/components/admin/room-modal';
import { DeleteConfirmModal } from '@/components/admin/delete-confirm-modal';

export default function AdminRoomsPage() {
  const { user, isAuthenticated, getCurrentUser } = useAuthStore();
  const router = useRouter();
  const [rooms, setRooms] = useState<Room[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);
  const [deleteConfirm, setDeleteConfirm] = useState<number | null>(null);


  useEffect(() => {
    if (!isAuthenticated) {
      getCurrentUser().catch(() => {
        router.push('/login');
      });
    } else if (user && user.role !== 'ADMIN') {
      router.push('/dashboard');
    }
  }, [isAuthenticated, getCurrentUser, router, user]);

  // Load rooms from API
  useEffect(() => {
    const fetchRooms = async () => {
      try {
        setLoading(true);
        const data = await adminService.getAllRooms();
        setRooms(data);
      } catch (err) {
        setError('Failed to load rooms');
        console.error('Error fetching rooms:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRooms();
  }, []);

  const filteredRooms = rooms.filter(room =>
    room.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    room.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreateRoom = async (roomData: {
    name: string;
    location: string;
    capacity: number;
    available: boolean;
    description?: string;
    equipment?: string;
    price?: number;
  }) => {
    try {
      const newRoom = await adminService.createRoom(roomData);
      setRooms([...rooms, newRoom]);
      setShowCreateModal(false);
    } catch (err) {
      setError('Failed to create room');
      console.error('Error creating room:', err);
    }
  };

  const handleUpdateRoom = async (id: number, roomData: Partial<Room>) => {
    try {
      const updatedRoom = await adminService.updateRoom(id, roomData);
      setRooms(rooms.map(room => room.id === id ? updatedRoom : room));
      setShowEditModal(false);
      setSelectedRoom(null);
    } catch (err) {
      setError('Failed to update room');
      console.error('Error updating room:', err);
    }
  };

  const handleDeleteRoom = async (id: number) => {
    try {
      await adminService.deleteRoom(id);
      setRooms(rooms.filter(room => room.id !== id));
      setDeleteConfirm(null);
    } catch (err) {
      setError('Failed to delete room');
      console.error('Error deleting room:', err);
    }
  };

  const openEditModal = (room: Room) => {
    setSelectedRoom(room);
    setShowEditModal(true);
  };

  if (!isAuthenticated || !user || user.role !== 'ADMIN') {
    return <div>Loading...</div>;
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <AdminNavbar />
        <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <div className="px-4 py-6 sm:px-0">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
              <p className="mt-4 text-gray-600">Loading rooms...</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <AdminNavbar />
      
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">
              Room Management
            </h1>
            <button 
              onClick={() => setShowCreateModal(true)}
              className="btn-primary flex items-center"
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Room
            </button>
          </div>

          {/* Error Message */}
          {error && (
            <div className="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg flex items-center">
              <AlertCircle className="h-5 w-5 text-red-400 mr-2" />
              <span className="text-red-800">{error}</span>
              <button 
                onClick={() => setError(null)}
                className="ml-auto text-red-600 hover:text-red-800"
              >
                ×
              </button>
            </div>
          )}

          {/* Search Bar */}
          <div className="mb-6">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              <input
                type="text"
                placeholder="Search rooms..."
                className="input-field pl-10"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          {/* Rooms Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredRooms.map((room) => (
              <div key={room.id} className="card hover:shadow-lg transition-shadow">
                <div className="flex items-start justify-between">
                  <div className="flex items-center">
                    <div className="h-10 w-10 rounded-lg bg-green-100 flex items-center justify-center">
                      <Building2 className="h-5 w-5 text-green-600" />
                    </div>
                    <div className="ml-3">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {room.name}
                      </h3>
                      <div className="flex items-center text-sm text-gray-500 mt-1">
                        <MapPin className="h-4 w-4 mr-1" />
                        {room.location}
                      </div>
                    </div>
                  </div>
                  <div className="flex space-x-2">
                    <button 
                      onClick={() => openEditModal(room)}
                      className="text-blue-600 hover:text-blue-900 p-1"
                      title="Edit room"
                    >
                      <Edit className="h-4 w-4" />
                    </button>
                    <button 
                      onClick={() => setDeleteConfirm(room.id)}
                      className="text-red-600 hover:text-red-900 p-1"
                      title="Delete room"
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                </div>
                
                <div className="mt-4 flex items-center justify-between">
                  <div className="flex items-center text-sm text-gray-600">
                    <Users className="h-4 w-4 mr-1" />
                    Capacity: {room.capacity} people
                  </div>
                  <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                    room.available 
                      ? 'bg-green-100 text-green-800' 
                      : 'bg-red-100 text-red-800'
                  }`}>
                    {room.available ? 'Available' : 'Unavailable'}
                  </span>
                </div>

                <div className="mt-4 pt-4 border-t border-gray-200">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-500">Created:</span>
                    <span className="text-gray-900">
                      {new Date(room.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Empty State */}
          {filteredRooms.length === 0 && (
            <div className="text-center py-12">
              <Building2 className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">No rooms found</h3>
              <p className="mt-1 text-sm text-gray-500">
                {searchTerm ? 'Try adjusting your search terms.' : 'Get started by creating a new room.'}
              </p>
              <div className="mt-6">
              </div>
            </div>
          )}

          {/* Pagination */}
          {filteredRooms.length > 0 && (
            <div className="mt-6 flex items-center justify-between">
              <div className="text-sm text-gray-700">
                Showing <span className="font-medium">1</span> to <span className="font-medium">{filteredRooms.length}</span> of{' '}
                <span className="font-medium">{rooms.length}</span> results
              </div>
              <div className="flex space-x-2">
                <button className="btn-secondary px-3 py-2 text-sm">
                  Previous
                </button>
                <button className="btn-secondary px-3 py-2 text-sm">
                  Next
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Create Room Modal */}
      {showCreateModal && (
        <RoomModal
          mode="create"
          onClose={() => setShowCreateModal(false)}
          onSubmit={handleCreateRoom}
        />
      )}

      {/* Edit Room Modal */}
      {showEditModal && selectedRoom && (
        <RoomModal
          mode="edit"
          room={selectedRoom}
          onClose={() => {
            setShowEditModal(false);
            setSelectedRoom(null);
          }}
          onSubmit={(data) => handleUpdateRoom(selectedRoom.id, data)}
        />
      )}

      {/* Delete Confirmation Modal */}
      {deleteConfirm && (
        <DeleteConfirmModal
          onClose={() => setDeleteConfirm(null)}
          onConfirm={() => handleDeleteRoom(deleteConfirm)}
          title="Delete Room"
          message="Are you sure you want to delete this room? This action cannot be undone."
        />
      )}
    </div>
  );
}
