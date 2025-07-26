# Meeting-Room-Scheduler
 docker-compose --env-file .env up --build
 docker-compose -f docker-compose.dev.yml up --build
echo -n "mensaje a firmar" | openssl dgst -sha512 -hmac "tu-clave-secreta"
# 🏢 Sistema de Reservas de Salas

Plataforma avançada para agendamento de salas com autenticação, painel administrativo, validação de conflitos, notificações por e-mail, pagamentos e mensageria assíncrona.

## 🔧 Tecnologias Utilizadas

### Backend
- Java 17 + Spring Boot
- Spring Data JPA + PostgreSQL
- Spring Security (JWT)
- RabbitMQ (Mensageria)
- Stripe API (Pagamentos)
- Mailtrap (Envio de E-mails)
- Swagger (Documentação de API)

### Frontend
- React + TypeScript
- Tailwind CSS
- Zustand ou Redux
- React Router
- React Calendar

---

## 📦 Funcionalidades

### Usuário
- Autenticação com JWT
- Visualização de reservas futuras
- Agendamento de salas com validação de conflitos
- Recebimento de e-mail após confirmação da reserva
- Pagamento simulado via Stripe

### Administrador
- Cadastro, edição e exclusão de salas
- Visualização global das reservas
- Gerenciamento de usuários

---

## 📁 Estrutura do Projeto

```
backend/
├── controller/
├── service/
├── model/
├── repository/
├── dto/
├── config/
├── utils/
└── ...
frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── store/
│   ├── api/
│   └── ...
```

---

## 🚀 Como rodar localmente

### Pré-requisitos

- Java 17
- Node.js 18+
- PostgreSQL
- RabbitMQ
- Conta no Mailtrap (https://mailtrap.io/)
- Conta no Stripe (https://stripe.com/)
- Docker (opcional)

### Backend

```bash
# Acesse o backend
cd backend

# Configure as variáveis no application.properties ou .env (se aplicável)
# Exemplo:
# SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/reservas
# MAILTRAP_API_KEY=...
# STRIPE_SECRET_KEY=...

# Rode a aplicação
./mvnw spring-boot:run
```

### Frontend

```bash
# Acesse o frontend
cd frontend

# Instale as dependências
npm install

# Rode a aplicação
npm run dev
```

---

## 🧪 Testes

- Você pode testar a API usando o Swagger em:
```
http://localhost:8080/swagger-ui/index.html
```

- Também é possível usar o Postman:
    - Faça login e copie o token JWT
    - Use o token para autenticar nas rotas protegidas

---

## 📬 Integrações

| Integração | Descrição |
|------------|-----------|
| Mailtrap | Simula envio de e-mails para testes |
| Stripe | Mock de pagamentos na criação da reserva |
| RabbitMQ | Fila para processar envio de e-mail em segundo plano |

---

## 📆 Lógica de Conflito de Horários

Ao reservar uma sala, o sistema verifica:
- Se a sala já está reservada no mesmo horário
- Se existe interseção entre datas/horários
- Caso exista conflito, a reserva é rejeitada

---

## 📸 Telas (em breve)

- Tela de Login
- Dashboard (Usuário/Admin)
- Calendário de reservas
- Formulário de nova reserva

---

## 📌 Roadmap

- [] Backend com autenticação e JPA
- [] CRUD de salas
- [] Lógica de reserva com conflitos
- [] JWT + RBAC (User/Admin)
- [ ] Integração Stripe
- [ ] Integração Mailtrap via RabbitMQ
- [ ] Calendário interativo
- [ ] Deploy em Vercel + Railway

---

## 🧠 Autor

Desenvolvido por **Luis Nunes**  
👨‍💻 Projeto pessoal para aprofundamento em arquitetura moderna, integração de APIs e sistemas completos com frontend e backend.

---

## 📜 Licença

Este projeto é de uso livre para fins educacionais e portfólio.  
Sinta-se à vontade para clonar, adaptar e expandir!