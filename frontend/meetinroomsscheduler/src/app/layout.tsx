import React from 'react';
import { QueryProvider } from '@/providers/query-provider';
import './globals.css';

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <head>
        <title>Meeting Rooms Scheduler</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta name="description" content="Schedule and manage meeting rooms efficiently" />
      </head>
      <body className="bg-gray-50 min-h-screen">
        <QueryProvider>
          {children}
        </QueryProvider>
      </body>
    </html>
  );
}