# Meeting Room Scheduler Frontend

A modern React/Next.js frontend for the Meeting Room Scheduler application.

## Features

- 🚀 **Next.js 15** with App Router
- 🎨 **Tailwind CSS** for styling
- 🔄 **React Query** for data fetching
- 📦 **Zustand** for state management
- 🔐 **JWT Authentication** with cookies
- 📱 **Responsive Design**
- 🎯 **TypeScript** for type safety

## Getting Started

### Prerequisites

- Node.js 18+ 
- npm or yarn
- Backend server running on `http://localhost:8080`

### Installation

1. Install dependencies:
```bash
npm install
```

2. Create environment file:
```bash
# Create .env.local file
echo "NEXT_PUBLIC_API_URL=http://localhost:8080" > .env.local
```

3. Run the development server:
```bash
npm run dev
```

4. Open [http://localhost:3000](http://localhost:3000) in your browser.

## Project Structure

```
src/
├── app/                    # Next.js App Router pages
│   ├── dashboard/         # Dashboard page
│   ├── login/            # Login page
│   ├── signup/           # Signup page
│   ├── globals.css       # Global styles
│   ├── layout.tsx        # Root layout
│   └── page.tsx          # Home page
├── components/            # React components
│   ├── auth/             # Authentication components
│   └── layout/           # Layout components
├── lib/                  # Utilities and configurations
│   └── api.ts           # API client setup
├── providers/            # React providers
│   └── query-provider.tsx # React Query provider
├── services/             # API services
│   ├── auth.ts          # Authentication service
│   ├── reservations.ts  # Reservation service
│   └── rooms.ts         # Room service
├── store/               # Zustand stores
│   └── auth.ts          # Authentication store
└── types/               # TypeScript types
    └── index.ts         # Type definitions
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run start` - Start production server
- `npm run lint` - Run ESLint

## API Integration

The frontend communicates with the backend API at `http://localhost:8080`. Make sure your backend server is running before starting the frontend.

### Authentication

The app uses JWT tokens stored in HTTP-only cookies for authentication. The API client is configured to include credentials in all requests.

### Key Features

- **Landing Page**: Beautiful landing page with feature highlights
- **Authentication**: Login and signup forms with validation
- **Dashboard**: Overview of reservations and quick actions
- **Responsive Design**: Works on desktop and mobile devices
- **Type Safety**: Full TypeScript support

## Development

### Adding New Pages

1. Create a new folder in `src/app/`
2. Add a `page.tsx` file
3. Export a default React component

### Adding New Components

1. Create a new folder in `src/components/`
2. Add your component file
3. Use the `'use client'` directive for client-side components

### API Calls

Use the services in `src/services/` for API calls:

```typescript
import { reservationService } from '@/services/reservations';

// Get reservations
const reservations = await reservationService.getReservations();

// Create reservation
const newReservation = await reservationService.createReservation(data);
```

## Deployment

The app can be deployed to Vercel, Netlify, or any other platform that supports Next.js.

1. Build the app: `npm run build`
2. Deploy the `.next` folder and static assets
3. Set environment variables in your deployment platform

## Contributing

1. Follow the existing code structure
2. Use TypeScript for all new code
3. Add proper error handling
4. Test your changes thoroughly
