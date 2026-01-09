# DeskReserve Pro (Work-desk Reservation System)

A **working, runnable IntelliJ project** showing:
- **Java + Spring Boot microservices**
- **OAuth 2.0 / JWT** auth via **Keycloak**
- **Apache Kafka** event streaming between services
- **PostgreSQL** persistence for the reservation service

## Architecture
- `reservation-service` (REST API, persists data, publishes Kafka events)
- `notification-service` (Kafka consumer, stores recent events in memory, exposes them via REST)
- `common` (shared event DTOs)

---

## Prerequisites
- IntelliJ IDEA (Community or Ultimate)
- JDK **17**
- Docker Desktop (for Keycloak/Kafka/Postgres)

> IntelliJ has built-in Maven support. You can import and run without installing Maven separately.

---

## 1) Start dependencies (Keycloak + Kafka + Postgres)

From the repo root:

```bash
docker compose up -d
```

Services:
- Keycloak: http://localhost:8080
- Kafka: localhost:9092
- Postgres: localhost:5432 (db: `deskreserve`, user: `deskreserve`, pass: `deskreserve`)

Keycloak is preloaded with a realm, users, and a client.

---

## 2) Open in IntelliJ
1. **File → Open** → select the folder `deskreserve-pro`
2. Let IntelliJ import Maven
3. Run the services:
   - `reservation-service/src/main/java/.../ReservationServiceApplication.java`
   - `notification-service/src/main/java/.../NotificationServiceApplication.java`

Each service runs on:
- reservation-service: http://localhost:8081
- notification-service: http://localhost:8082

---

## 3) Get an access token (OAuth 2.0 Password Grant for local dev)

> For real production, use Authorization Code + PKCE.  
> This repo uses password grant ONLY to keep local testing easy.

```bash
curl -s -X POST "http://localhost:8080/realms/deskreserve/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=deskreserve-cli" \
  -d "grant_type=password" \
  -d "username=alex" \
  -d "password=alex" | python -c "import sys, json; print(json.load(sys.stdin)['access_token'])"
```

Users:
- `alex` / `alex` (ROLE_USER)
- `admin` / `admin` (ROLE_ADMIN)

Export token into a shell variable:

```bash
TOKEN="<paste token here>"
```

---

## 4) Call the API

### 4.1 Create desks (ADMIN only)
```bash
curl -X POST "http://localhost:8081/api/desks" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Desk-101","location":"Floor 10"}'
```

If you used `alex`, you’ll get 403. Use the token for `admin`.

### 4.2 List desks
```bash
curl -s "http://localhost:8081/api/desks" \
  -H "Authorization: Bearer $TOKEN"
```

### 4.3 Create a reservation (USER)
```bash
curl -X POST "http://localhost:8081/api/reservations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"deskId":1,"startDate":"2026-01-10","endDate":"2026-01-10"}'
```

This publishes a Kafka event to topic `desk-reservations`.

### 4.4 See events consumed by notification-service
```bash
curl -s "http://localhost:8082/api/notifications" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 5) IntelliJ Run Config tips
If IntelliJ can’t resolve JDK:
- **File → Project Structure → Project SDK** → choose Java 17

If Kafka isn’t reachable:
- ensure `docker compose ps` shows Kafka running
- verify `localhost:9092` is available

---

## Environment variables (optional)
You can override defaults:

Reservation service:
- `SPRING_DATASOURCE_URL` (default `jdbc:postgresql://localhost:5432/deskreserve`)
- `SPRING_DATASOURCE_USERNAME` (default `deskreserve`)
- `SPRING_DATASOURCE_PASSWORD` (default `deskreserve`)
- `KAFKA_BOOTSTRAP_SERVERS` (default `localhost:9092`)
- `OAUTH_ISSUER_URI` (default `http://localhost:8080/realms/deskreserve`)

Notification service:
- `KAFKA_BOOTSTRAP_SERVERS`
- `OAUTH_ISSUER_URI`

---

## Notes
- This is intentionally kept small and “run-it-now”.
- The services demonstrate the core patterns (REST + DB + OAuth2 + Kafka events).
- If you want, tell me which microservices you want (e.g., user service, attendance policy service, admin portal) and I can expand this repo.
