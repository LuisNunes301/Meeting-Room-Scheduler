'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/auth';
import { AdminNavbar } from '@/components/admin/admin-navbar';
import { 
  Settings, 
  Bell, 
  Shield, 
  Database, 
  Save,
  Eye,
  EyeOff
} from 'lucide-react';

export default function AdminSettingsPage() {
  const { user, isAuthenticated, getCurrentUser } = useAuthStore();
  const router = useRouter();
  const [settings, setSettings] = useState({
    emailNotifications: true,
    reservationApproval: false,
    maxReservationDuration: 4,
    autoCancelInactive: true,
    inactiveTimeout: 30,
    systemMaintenance: false,
    maintenanceMessage: '',
    showAdminPanel: true
  });
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      getCurrentUser().catch(() => {
        router.push('/login');
      });
    } else if (user && user.role !== 'ADMIN') {
      router.push('/dashboard');
    }
  }, [isAuthenticated, getCurrentUser, router, user]);

  const handleSettingChange = (key: string, value: string | number | boolean) => {
    setSettings(prev => ({
      ...prev,
      [key]: value
    }));
  };

  const handleSave = () => {
    // Save settings logic here
    console.log('Saving settings:', settings);
  };

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
              System Settings
            </h1>
            <button 
              onClick={handleSave}
              className="btn-primary flex items-center"
            >
              <Save className="h-4 w-4 mr-2" />
              Save Settings
            </button>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* General Settings */}
            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                <Settings className="h-5 w-5 mr-2" />
                General Settings
              </h2>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <label className="text-sm font-medium text-gray-700">
                      Email Notifications
                    </label>
                    <p className="text-xs text-gray-500">
                      Send email notifications for reservations
                    </p>
                  </div>
                  <button
                    onClick={() => handleSettingChange('emailNotifications', !settings.emailNotifications)}
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      settings.emailNotifications ? 'bg-blue-600' : 'bg-gray-200'
                    }`}
                  >
                    <span className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                      settings.emailNotifications ? 'translate-x-6' : 'translate-x-1'
                    }`} />
                  </button>
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <label className="text-sm font-medium text-gray-700">
                      Reservation Approval Required
                    </label>
                    <p className="text-xs text-gray-500">
                      Require admin approval for new reservations
                    </p>
                  </div>
                  <button
                    onClick={() => handleSettingChange('reservationApproval', !settings.reservationApproval)}
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      settings.reservationApproval ? 'bg-blue-600' : 'bg-gray-200'
                    }`}
                  >
                    <span className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                      settings.reservationApproval ? 'translate-x-6' : 'translate-x-1'
                    }`} />
                  </button>
                </div>

                <div>
                  <label className="text-sm font-medium text-gray-700">
                    Maximum Reservation Duration (hours)
                  </label>
                  <input
                    type="number"
                    min="1"
                    max="24"
                    value={settings.maxReservationDuration}
                    onChange={(e) => handleSettingChange('maxReservationDuration', parseInt(e.target.value))}
                    className="input-field mt-1"
                  />
                </div>
              </div>
            </div>

            {/* Security Settings */}
            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                <Shield className="h-5 w-5 mr-2" />
                Security Settings
              </h2>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <label className="text-sm font-medium text-gray-700">
                      Auto-cancel Inactive Reservations
                    </label>
                    <p className="text-xs text-gray-500">
                      Automatically cancel reservations if not confirmed
                    </p>
                  </div>
                  <button
                    onClick={() => handleSettingChange('autoCancelInactive', !settings.autoCancelInactive)}
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      settings.autoCancelInactive ? 'bg-blue-600' : 'bg-gray-200'
                    }`}
                  >
                    <span className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                      settings.autoCancelInactive ? 'translate-x-6' : 'translate-x-1'
                    }`} />
                  </button>
                </div>

                <div>
                  <label className="text-sm font-medium text-gray-700">
                    Inactive Timeout (minutes)
                  </label>
                  <input
                    type="number"
                    min="5"
                    max="120"
                    value={settings.inactiveTimeout}
                    onChange={(e) => handleSettingChange('inactiveTimeout', parseInt(e.target.value))}
                    className="input-field mt-1"
                  />
                </div>

                <div>
                  <label className="text-sm font-medium text-gray-700">
                    Admin Password
                  </label>
                  <div className="relative mt-1">
                    <input
                      type={showPassword ? 'text' : 'password'}
                      defaultValue="••••••••"
                      className="input-field pr-10"
                      disabled
                    />
                    <button
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2"
                    >
                      {showPassword ? (
                        <EyeOff className="h-4 w-4 text-gray-400" />
                      ) : (
                        <Eye className="h-4 w-4 text-gray-400" />
                      )}
                    </button>
                  </div>
                </div>
              </div>
            </div>

            {/* Maintenance Settings */}
            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                <Database className="h-5 w-5 mr-2" />
                Maintenance Settings
              </h2>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <label className="text-sm font-medium text-gray-700">
                      System Maintenance Mode
                    </label>
                    <p className="text-xs text-gray-500">
                      Enable maintenance mode to prevent new reservations
                    </p>
                  </div>
                  <button
                    onClick={() => handleSettingChange('systemMaintenance', !settings.systemMaintenance)}
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      settings.systemMaintenance ? 'bg-red-600' : 'bg-gray-200'
                    }`}
                  >
                    <span className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                      settings.systemMaintenance ? 'translate-x-6' : 'translate-x-1'
                    }`} />
                  </button>
                </div>

                <div>
                  <label className="text-sm font-medium text-gray-700">
                    Maintenance Message
                  </label>
                  <textarea
                    value={settings.maintenanceMessage}
                    onChange={(e) => handleSettingChange('maintenanceMessage', e.target.value)}
                    placeholder="Enter maintenance message..."
                    className="input-field mt-1"
                    rows={3}
                  />
                </div>
              </div>
            </div>

            {/* Display Settings */}
            <div className="card">
              <h2 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                <Bell className="h-5 w-5 mr-2" />
                Display Settings
              </h2>
              
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <label className="text-sm font-medium text-gray-700">
                      Show Admin Panel
                    </label>
                    <p className="text-xs text-gray-500">
                      Display admin panel in navigation
                    </p>
                  </div>
                  <button
                    onClick={() => handleSettingChange('showAdminPanel', !settings.showAdminPanel)}
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      settings.showAdminPanel ? 'bg-blue-600' : 'bg-gray-200'
                    }`}
                  >
                    <span className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                      settings.showAdminPanel ? 'translate-x-6' : 'translate-x-1'
                    }`} />
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* System Information */}
          <div className="mt-6 card">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              System Information
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
              <div>
                <span className="text-gray-500">Version:</span>
                <span className="ml-2 text-gray-900">1.0.0</span>
              </div>
              <div>
                <span className="text-gray-500">Last Updated:</span>
                <span className="ml-2 text-gray-900">2024-01-25</span>
              </div>
              <div>
                <span className="text-gray-500">Status:</span>
                <span className="ml-2 text-green-600 font-medium">Online</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
