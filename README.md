# Meeting-Room-Scheduler
 docker-compose --env-file .env up --build
 docker-compose -f docker-compose.dev.yml up --build
# ✅ Projeto 2 — Sistema de Reservas de Salas (Avançado)
Plataforma para agendamento de salas com validação de conflitos e painel administrativo.
🔧 Tecnologias
- Frontend: React + TS, Tailwind, Zustand ou Redux, React Router
- Backend: Spring Boot, Spring Data JPA, PostgreSQL, Swagger,mailtrap , stripe ,rabbitmq
🧩 Funcionalidades 
- Login com autenticação JWT
- Email send( mailtrap)
- Cadastro de salas e usuários (admin)
- rabbitmq
- stripe
- Reserva de sala com data/hora e prevenção de conflito
- Visualização de calendário com reservas
- Painel do usuário e do administrador
🗺️ Roadmap
- Definir entidades: User, Room, Reservation , payment
- Implementar lógica de conflito de horários
- Criar endpoints REST para reservas e salas
- Tela de login, dashboard, reserva e calendário
- Admin pode criar salas e visualizar reservas de todos
- Interface com React Calendar e filtros por data/sala
