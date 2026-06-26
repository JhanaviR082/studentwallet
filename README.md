# Student Wallet – Daily Budget Manager

A mobile-first web app that helps students manage pocket money with real-time budgeting intelligence. Built with **Angular 17**, **Spring Boot 3**, and **Apache Cassandra**.

## Features

- **Wallet splash animation** – leather wallet flip opens into the dashboard
- **Dark premium theme** – gold accents inspired by CRED / Apple Wallet
- **Real-time budget calculator** – today's safe spend, per-day allowance, month-end surplus, potential savings
- **Expense logging** – Indian student categories with Better/Bad spending verdicts
- **Indian Rupee formatting** – en-IN numbering (1,00,000)

## Project Structure

```
student-wallet/
├── backend/                 # Spring Boot 3 + Cassandra API
├── frontend/student-wallet-ui/  # Angular 17 + Material UI
├── docker-compose.yml       # Cassandra 4.x
└── README.md
```

## Prerequisites

- Node.js 18+
- Java 17+
- Maven 3.8+
- Docker (for Cassandra)

## Quick Start

### 1. Start Cassandra

```bash
docker compose up -d
```

Wait ~60 seconds for Cassandra to be ready, then initialize the schema:

```bash
docker exec -it student-wallet-cassandra cqlsh -f /docker-entrypoint-initdb.d/schema.cql
```

### 2. Start Backend

```bash
cd backend
mvn spring-boot:run
```

API runs at `http://localhost:8080`

### 3. Start Frontend

```bash
cd frontend/student-wallet-ui
npm install
npm start
```

App runs at `http://localhost:4200`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/budget/calculate` | Calculate dashboard from income |
| GET | `/api/budget/dashboard/{userId}` | Get current dashboard |
| POST | `/api/expenses` | Add expense |
| GET | `/api/expenses/{userId}/today` | Today's expenses |
| GET | `/api/expenses/{userId}/pattern` | Spending pattern list |
| POST | `/api/expenses/import-sms` | Parse UPI/bank SMS and auto-log expense |
| POST | `/api/budget/what-if` | Simulate extra savings goal impact |
| GET | `/api/gamification` | Streaks, badges, hostel leaderboard |

### Example: Calculate Budget

```bash
curl -X POST http://localhost:8080/api/budget/calculate \
  -H "Content-Type: application/json" \
  -H "X-User-Id: default-user" \
  -d '{"totalIncome": 15000}'
```

### Example: What-If Simulator

```bash
curl -X POST http://localhost:8080/api/budget/what-if \
  -H "Content-Type: application/json" \
  -H "X-User-Id: default-user" \
  -d '{"extraSavingsGoal": 5000}'
```

### Example: SMS Import

```bash
curl -X POST http://localhost:8080/api/expenses/import-sms \
  -H "Content-Type: application/json" \
  -H "X-User-Id: default-user" \
  -d '{"smsText": "Rs.149 debited from A/c **1234 to VPA zomato@paytm. UPI Ref 123456."}'
```

## Stretch Features

- **Gamification** — streaks, badges (Chai Connoisseur, Metro Man, Mess Monster), hostel leaderboard
- **What-If Simulator** — enter extra savings goal, see adjusted daily budget and tips
- **SMS/UPI Import** — paste debit SMS; auto-detect amount and category

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | Angular 17, Angular Material, SCSS |
| Backend | Java 17, Spring Boot 3.2, Spring Data Cassandra |
| Database | Apache Cassandra 4.1 |
| Currency | INR (₹) with Indian numbering |

## Budget Logic

1. **Base daily allowance** = total income ÷ days in cycle
2. **Today's safe spend** = ideal spend till today − actual spend (dynamic)
3. **Coming days allowance** = remaining money ÷ days left
4. **Month-end surplus** = projected leftover if daily limit is followed
5. **Potential savings** = income − (7-day average daily spend × total days)

## License

MIT
