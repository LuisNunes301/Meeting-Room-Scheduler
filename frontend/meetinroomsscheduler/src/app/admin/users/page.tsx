'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth';
import { AdminNavbar } from '@/components/admin/admin-navbar';
import { User, Plus, Edit, Trash2, Search } from 'lucide-react';
import { User as UserType } from '@/types';
import { adminService } from '@/services/admin';
import { DeleteConfirmModal } from '@/components/admin/delete-confirm-modal';
import { UserModal } from '@/components/admin/create-user-modal';

export default function AdminUsersPage() {
  const { user, isAuthenticated, getCurrentUser } = useAuthStore();
  const router = useRouter();

  const [users, setUsers] = useState<UserType[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showCreateUser, setShowCreateUser] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<number | null>(null);
  const [selectedUser, setSelectedUser] = useState<UserType | null>(null);

  useEffect(() => {
    if (!isAuthenticated) {
      getCurrentUser().catch(() => {
        router.push('/login');
      });
    } else if (user && user.role !== 'ADMIN') {
      router.push('/dashboard');
    }
  }, [isAuthenticated, getCurrentUser, router, user]);

  // load user from api
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await adminService.getAllUsers();
        const ids = data.map(user => user.id);
        const uniqueIds = new Set(ids);
        if (ids.length !== uniqueIds.size) {
          console.error('Duplicate user IDs found!');
        }
        setUsers(data);
      } catch (err) {
        console.error('Error fetching users:', err);
      }
    };

    fetchUsers();
  }, []);

  const handleDeleteUser = async (id: number) => {
    try {
      await adminService.deleteUser(id);
      setUsers((prev) => prev.filter((u) => u.id !== id));
    } catch (err) {
      console.error('Error deleting user:', err);
    }
  };

  const handleCreateUser = async (data: { username: string; name: string; email: string; password?: string; role: 'ADMIN' | 'USER' }) => {
    try {
      const newUser = await adminService.createUser(data);
      setUsers((prev) => [...prev, newUser]);
    } catch (err) {
      console.error('Error creating user:', err);
    }
  };

  const handleUpdateUser = async (id: number, data: Partial<UserType>) => {
    try {
      const updatedUser = await adminService.updateUser(id, data);
      setUsers((prev) => prev.map((u) => (u.id === id ? updatedUser : u)));
    } catch (err) {
      console.error('Error updating user:', err);
    }
  };

  const filteredUsers = users.filter(user =>
    (user.name?.toLowerCase() ?? '').includes(searchTerm.toLowerCase()) ||
    (user.email?.toLowerCase() ?? '').includes(searchTerm.toLowerCase()) ||
    (user.username?.toLowerCase() ?? '').includes(searchTerm.toLowerCase())
  );

  if (!isAuthenticated || !user || user.role !== 'ADMIN') {
    return <div>Loading...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <AdminNavbar />

      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">
              User Management
            </h1>
            <button className="btn-primary flex items-center" onClick={() => setShowCreateUser(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Add User
            </button>
          </div>

          {/* Search Bar */}
          <div className="mb-6">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              <input
                type="text"
                placeholder="Search users..."
                className="input-field pl-10"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          {/* Users Table */}
          <div className="card">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      User
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Email
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Role
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Created
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredUsers.map((user) => (
                    <tr key={user.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div className="flex-shrink-0 h-10 w-10">
                            <div className="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                              <User className="h-5 w-5 text-blue-600" />
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {user.name}
                            </div>
                            <div className="text-sm text-gray-500">
                              @{user.username}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {user.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${user.role === 'ADMIN'
                            ? 'bg-red-100 text-red-800'
                            : 'bg-green-100 text-green-800'
                          }`}>
                          {user.role}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(user.createdAt).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex space-x-2">
                          <button
                            className="text-blue-600 hover:text-blue-900"
                            onClick={() => {
                              setSelectedUser(user);
                              setShowEditModal(true);
                            }}
                          >
                            <Edit className="h-4 w-4" />
                          </button>
                          <button
                            className="text-red-600 hover:text-red-900"
                            onClick={() => setDeleteConfirm(user.id)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Pagination */}
          {filteredUsers.length > 0 && (
            <div className="mt-6 flex items-center justify-between">
              <div className="text-sm text-gray-700">
                Showing <span className="font-medium">1</span> to <span className="font-medium">{filteredUsers.length}</span> of{' '}
                <span className="font-medium">{users.length}</span> results
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

          {/* Create User Modal */}
          {showCreateUser && (
            <UserModal
              mode="create"
              onClose={() => setShowCreateUser(false)}
              onSubmit={handleCreateUser}
            />
          )}

          {/* Edit User Modal */}
          {showEditModal && selectedUser && (
            <UserModal
              mode="edit"
              user={selectedUser}
              onClose={() => setShowEditModal(false)}
              onSubmit={(data) => {
                handleUpdateUser(selectedUser.id, data);
                setShowEditModal(false);
              }}
            />
          )}

          {/* Delete Confirm Modal */}
          {deleteConfirm && (
            <DeleteConfirmModal
              title="Delete User"
              message="Are you sure you want to delete this user? This action cannot be undone."
              onClose={() => setDeleteConfirm(null)}
              onConfirm={() => {
                handleDeleteUser(deleteConfirm);
                setDeleteConfirm(null);
              }}
            />
          )}
        </div>
      </div>
    </div>
  );
}
