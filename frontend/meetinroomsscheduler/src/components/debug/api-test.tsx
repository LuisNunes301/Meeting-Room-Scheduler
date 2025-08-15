'use client';

import { useState } from 'react';
import { authService } from '@/services/auth';

export function ApiTest() {
  const [result, setResult] = useState<string>('');
  const [loading, setLoading] = useState(false);

  const testSignup = async () => {
    setLoading(true);
    setResult('');

    try {
      const testData = {
        username: 'testuser',
        password: 'testpass123',
        name: 'Test User',
        email: 'test@example.com',
      };

      console.log('Testing signup with data:', testData);
      const response = await authService.signup(testData);
      setResult(`Success: ${JSON.stringify(response, null, 2)}`);
    } catch (error) {
      console.error('Test failed:', error);
      setResult(`Error: ${error instanceof Error ? error.message : 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  const testLogin = async () => {
    setLoading(true);
    setResult('');

    try {
      const testData = {
        username: 'testuser',
        password: 'testpass123',
      };

      console.log('Testing login with data:', testData);
      const response = await authService.login(testData);
      setResult(`Success: ${JSON.stringify(response, null, 2)}`);
    } catch (error) {
      console.error('Test failed:', error);
      setResult(`Error: ${error instanceof Error ? error.message : 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 border rounded-lg bg-gray-50">
      <h3 className="text-lg font-semibold mb-4">API Test Component</h3>

      <div className="space-y-2 mb-4">
        <button
          onClick={testSignup}
          disabled={loading}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50"
        >
          {loading ? 'Testing...' : 'Test Signup'}
        </button>

        <button
          onClick={testLogin}
          disabled={loading}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:opacity-50 ml-2"
        >
          {loading ? 'Testing...' : 'Test Login'}
        </button>
      </div>

      {result && (
        <div className="mt-4">
          <h4 className="font-medium mb-2">Result:</h4>
          <pre className="bg-white p-2 rounded border text-sm overflow-auto">{result}</pre>
        </div>
      )}
    </div>
  );
}
