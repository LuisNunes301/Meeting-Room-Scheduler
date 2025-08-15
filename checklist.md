✅ 1. BACKEND (Spring Boot)
1.1. Estrutura base
Java + Spring Boot

JPA + PostgreSQL

Arquitetura limpa/modular (mesmo fora do hexagonal)

RabbitMQ para mensageria assíncrona

Serviço de e-mail com EmailService e templates Thymeleaf

1.2. Observabilidade FEITO
Sentry: captura de exceções e erros

1.3. Notificações assíncronas
RabbitMQ: FEITO

Eventos como ReservationCreated, UserRegistered

Consumidores escutam e acionam ações como envio de e-mail

EmailService: FEITO

Você coloca dentro da pasta service ou application/service

Injetado nos consumidores para envio de e-mail com templates

1.4. Push Notifications em tempo real (popup para frontend) fazer dps do frontend
Socket.io-like: use Spring WebSocket + STOMP

Exemplo: quando reserva for criada, você emite um evento WebSocket para o usuário ver o popup na tela

Integração com frontend via SockJS + STOMP client

// ajutar o frontend
✅ 2. FRONTEND (Next.js com React + Tailwind)
2.1. Stack base
Next.js 14+ (React App Router) ok

Zustand ou Redux (gerenciamento de estado) feito

tanskquery ok

TailwindCSS (estilização rápida) ok

Toasts (react-hot-toast, react-toastify, ou Radix UI + custom)

Socket client (WebSocket via STOMP client)

2.2. Notificações para o usuário
Toasts de sucesso, erro, info

Ex: "Reserva criada com sucesso", "Erro ao salvar", etc

Modal (room/reservation/admin/users)

WebSocket para mensagens em tempo real (ex: “Sala reservada por outro usuário”)

✅ 3. INFRA (DevOps)
Docker + Docker Compose

PostgreSQL ok

RabbitMQ ok

Sentry (Observabilidade) ok

Mailhog (para testar e-mails em desenvolvimento) ok

GitHub Actions (CI/CD)