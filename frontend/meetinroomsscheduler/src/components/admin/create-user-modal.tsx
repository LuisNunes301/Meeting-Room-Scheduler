'use client';
import { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { User } from "@/types";

interface UserModalProps {
  mode: 'create' | 'edit';
  user?: User;
  onClose: () => void;
  onSubmit: (data: {
    username: string;
    password?: string;
    name: string;
    email: string;
    role: "ADMIN" | "USER";
  }) => Promise<void> | void;
}

export function UserModal({ mode, user, onClose, onSubmit }: UserModalProps) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState({
    username: "",
    password: "",
    name: "",
    email: "",
    role: "USER" as "ADMIN" | "USER",
  });

  useEffect(() => {
    if (user && mode === "edit") {
      setForm({
        username: user.username,
        password: "", // senha em branco para não exibir
        name: user.name,
        email: user.email,
        role: user.role,
      });
    }
  }, [user, mode]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!form.username || !form.name || !form.email || (mode === "create" && !form.password)) {
      setError("Please fill in all required fields.");
      return;
    }

    try {
      setLoading(true);
      await onSubmit({
        username: form.username,
        password: form.password || undefined,
        name: form.name,
        email: form.email,
        role: form.role,
      });
      onClose();
    } catch (err) {
      setError("Failed to save user.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      onClick={onClose} // fecha ao clicar fora
    >
      <div
        className="bg-white rounded-lg p-6 w-full max-w-md mx-4"
        onClick={(e) => e.stopPropagation()} // evita fechar ao clicar dentro
      >
        <div className="flex items-center mb-4">
          <h3 className="text-lg font-medium text-gray-900">
            {mode === "create" ? "Create User" : "Edit User"}
          </h3>
          <button
            onClick={onClose}
            className="ml-auto text-gray-400 hover:text-gray-600"
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Username *</label>
            <input
              type="text"
              name="username"
              value={form.username}
              onChange={handleChange}
              className="input-field"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">
              Password {mode === "create" && "*"}
            </label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              className="input-field"
              placeholder={mode === "edit" ? "Leave blank to keep current password" : ""}
              required={mode === "create"}
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Name *</label>
            <input
              type="text"
              name="name"
              value={form.name}
              onChange={handleChange}
              className="input-field"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Email *</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              className="input-field"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Role *</label>
            <select
              name="role"
              value={form.role}
              onChange={handleChange}
              className="input-field"
              required
            >
              <option value="ADMIN">Admin</option>
              <option value="USER">User</option>
            </select>
          </div>

          <div className="flex justify-end">
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              {loading ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
