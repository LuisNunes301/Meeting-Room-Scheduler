
 docker-compose --env-file .env up --build
 


# Meeting-Room-Scheduler

Descrição:
Aplicativo de gerenciamento de reservas de salas de reunião, desenvolvido com Spring Boot no backend e React + TypeScript no frontend. Permite criar, editar e gerenciar reservas, com autenticação, validação de datas, controle de status e notificações.

##  Tecnologias Utilizadas

### Backend
- Java 17 + Spring Boot
- Spring Data JPA + PostgreSQL
- Spring Security (JWT)
- RabbitMQ AMQP (Mensageria)
- Jackson para serialização JSON e suporte a datas ISO
- MAILHOG SMTP (Envio de E-mails)
- Swagger (Documentação de API)
- Docker (para conteinerização)

### Frontend
- Next.js (next) – framework React para SSR e roteamento.
- React (react, react-dom) – biblioteca principal para UI.
- Tailwind CSS (tailwindcss, @tailwindcss/postcss) – estilização moderna e responsiva.
- Zustand – gerenciamento de estado global leve.
- Axios – requisições HTTP para a API do backend.
- React Query (@tanstack/react-query, @tanstack/react-query-devtools) – gerenciamento de estado assíncrono e cache de dados.
- Headless UI + Heroicons – componentes acessíveis e ícones.
- Lucide React – ícones adicionais.
- Prettier – formatação de código.
- TypeScript – tipagem estática para maior robustez.

---

##  Funcionalidades

- Cadastro e login de usuários (admin e usuário comum)
- Cadastro, edição e remoção de reservas
- Controle de status da reserva (PENDING, CONFIRMED, CANCELLED)
- Validação de datas no frontend e backend (@FutureOrPresent)
- Visualização das reservas do usuário em lista detalhada
- Notificações assíncronas via RabbitMQ
- Conversão automática de datas para ISO 8601 / UTC
- Modal de reserva dinâmico com validação de horário
- Arquitetura preparada para eventos e pagamentos
---

##  Estrutura do Projeto

```
backend/
├── config/
├── controller/
├── dto/
├── entities/
├── exceptions/
├── rabbit/
├── repository/
├── runner/
├── service/
└── security/

frontend/
├── src/
├──  app/
│ ├─ admin/ # Painel admin
│ ├─ dashboard/ # Dashboard do usuário
│ ├─ login/
│ ├─ signup/
│ ├─ forgot-password/
│ ├─ reset-password/
│ ├─ reservation/
│ └─room/
│─ components/ # Componentes centrais
├─ services/ # Chamadas API (auth, rooms, reservations)
├─ lib/ # Custom React hooks
├─ providers/ # Zustand store ou React Context
├─ store/ # Zustand store ou React Context
└─ types/ # Tipos TypeScript
```

---

##  Como rodar localmente

### Pré-requisitos

- Java 17
- Node.js 18+
- PostgreSQL
- RabbitMQ
- Docker

### Backend

```bash
# Acesse o backend
cd backend

Configure uma chave no seu .env com este codigo:1
echo -n "x" | openssl dgst -sha512 -hmac "tu-clave-secreta"

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
### Docker
```bash
docker-compose -f docker-compose.yml up --build
```
---

##  Testes

- Você pode testar a API usando o Swagger em:
```
http://localhost:8080/swagger-ui/index.html
```

- Também é possível usar o Postman:
    - Faça login e copie o token JWT
    - Use o token para autenticar nas rotas protegidas

---

##  Integrações

| Integração | Descrição |
|------------|-----------|
| MAILHOG | Simula envio de e-mails para testes |
| RabbitMQ | Fila para processar envio de e-mail em segundo plano |

---

##  Autor

Desenvolvido por **Luis Nunes**  
 Projeto pessoal para aprofundamento em arquitetura moderna, integração de APIs e sistemas completos com frontend e backend.

---



##  Licença

Este projeto é de uso livre para fins educacionais e portfólio.  
Sinta-se à vontade para clonar, adaptar e expandir!