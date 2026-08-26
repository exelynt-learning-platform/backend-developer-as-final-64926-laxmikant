# Resource Booking System

A RESTful Resource Booking System built with **Spring Boot 3.2.5**, **Java 17**, **Spring Security**, **JWT**, and **H2 / MySQL / PostgreSQL**.

The system provides complete Role-Based Access Control (**RBAC**) for **ADMIN** and **USER** roles, allowing users to view available resources and manage their own reservations while administrators maintain full operational control.

---

## Technical Stack

- **Java Version**: 17+ (Java 17 / 21 compatible)
- **Framework**: Spring Boot 3.2.5
- **Security**: Spring Security 6 with JWT (JSON Web Tokens)
- **Database**: H2 (In-Memory for Dev/Test) / MySQL 8+ / PostgreSQL (Production)
- **Persistence**: Spring Data JPA / Hibernate 6
- **Documentation**: OpenAPI 3.0 / Swagger UI (`springdoc-openapi`)
- **Build Tool**: Apache Maven (`./mvnw` wrapper included)

---

## Features

### Authentication & Security
- **`POST /auth/login`**: Authenticates credentials and returns a signed JWT bearer token.
- **Stateless Session**: Every request is authenticated via `Authorization: Bearer <token>`.
- **Password Encoding**: Passwords hashed with BCrypt.
- **OpenAPI / Swagger UI**: Accessible at `/swagger-ui/index.html` and `/swagger-ui.html`.

### Role-Based Access Control (RBAC)

| Endpoint | Method | Required Role | Description |
|---|---|---|---|
| `/auth/login` | POST | Public | Authenticate user & retrieve JWT token |
| `/swagger-ui/**` | GET | Public | Swagger UI API documentation |
| `/v3/api-docs/**` | GET | Public | OpenAPI specification JSON |
| `/resources` | GET | USER / ADMIN | List all available resources |
| `/resources/{id}` | GET | USER / ADMIN | View specific resource details |
| `/resources` | POST | ADMIN | Create a new resource |
| `/resources/{id}` | PUT | ADMIN | Update resource details |
| `/resources/{id}` | DELETE | ADMIN | Delete a resource |
| `/reservations` | GET | USER / ADMIN | List reservations (USER sees own; ADMIN sees all) |
| `/reservations/{id}` | GET | USER / ADMIN | Get reservation by ID (ownership enforced) |
| `/reservations` | POST | USER / ADMIN | Create a new reservation |
| `/reservations/{id}` | PUT | USER / ADMIN | Update a reservation (ownership enforced) |
| `/reservations/{id}` | DELETE | USER / ADMIN | Cancel/delete a reservation (ownership enforced) |

### Business Logic Safeguards
- **Ownership Security**: `USER` accounts can **only** view, update, or delete reservations that belong to them. Attempting to access another user's reservation returns `404 Not Found`.
- **Resource Deletion Safety**: A resource with active reservations **cannot** be deleted (returns `400 Bad Request` instead of DB 500 error).
- **Cancelled Status Enforcement**: Cancelled reservations cannot be modified.
- **Time Window Validation**: `startTime` must strictly precede `endTime`.

---

## Seed Credentials (Data Initializer)

On application startup, pre-seeded test accounts are created if they do not exist:

| Role | Username | Password | Purpose |
|---|---|---|---|
| **ADMIN** | `admin` | `admin123` | Full access to manage resources and all reservations |
| **USER** | `Laxmikant` | `user123` | Read-only resource access; create and manage own reservations |

> **Production Security Note**: Seed credentials and JWT secret keys (`jwt.secret`) can be overridden via environment variables:
> ```bash
> export JWT_SECRET="YourVeryLongAndSecureSecretKeyWithAtLeast256BitsOfEntropyString"
> export SEED_ADMIN_PASSWORD="YourSecureAdminPassword"
> export SEED_USER_PASSWORD="YourSecureUserPassword"
> ```

---

## API Documentation (Swagger / OpenAPI)

Once the application is running, access Swagger UI in your browser:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

To authenticate in Swagger UI:
1. Call `POST /auth/login` with body:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
2. Copy the returned `token`.
3. Click the **Authorize** button in Swagger UI, enter `Bearer <your_token>`, and click **Authorize**.

---

## Quick Start & Local Setup

### Prerequisites
- JDK 17 or JDK 21 installed.
- Maven (or use the included `./mvnw` wrapper).

### Build & Test

```bash
# Clone the repository
git clone <repository-url>
cd resource-booking

# Run unit and integration tests
./mvnw clean test

# Package the application
./mvnw clean package -DskipTests
```

### Run Locally

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

---

## Environment Configuration

Configuration properties can be set via `src/main/resources/application.properties` or environment variables:

| Property | Environment Variable | Default Value | Description |
|---|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/resource_booking` | Database connection URL |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | `root` | Database username |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | `admin` | Database password |
| `jwt.secret` | `JWT_SECRET` | `VGhpc0lzQVNlY3JldEtleUZvclJlc291cmNlQm9va2luZ1N5c3RlbQ==` | 256-bit JWT secret key |
| `jwt.expiration` | `JWT_EXPIRATION` | `86400000` (24 hours) | Token validity in milliseconds |

---

## API Usage Examples

### 1. Login (Retrieve Token)

**Request:**
```http
POST /auth/login
Content-Type: application/json

{
  "username": "Laxmikant",
  "password": "user123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMYXhtaWthbnQiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTY5MzAwMDAwMCwiZXhwIjoxNjkzMDg2NDAwfQ..."
}
```

---

### 2. Get Available Resources

**Request:**
```http
GET /resources
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Conference Hall A",
    "type": "CONFERENCE_HALL",
    "capacity": 50
  },
  {
    "id": 2,
    "name": "Meeting Room 101",
    "type": "MEETING_ROOM",
    "capacity": 8
  }
]
```

---

### 3. Create a Reservation

**Request:**
```http
POST /reservations
Authorization: Bearer <token>
Content-Type: application/json

{
  "resourceId": 1,
  "startTime": "2026-09-01T10:00:00",
  "endTime": "2026-09-01T12:00:00",
  "price": 150.00,
  "status": "PENDING"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "resource": {
    "id": 1,
    "name": "Conference Hall A",
    "type": "CONFERENCE_HALL",
    "capacity": 50
  },
  "user": {
    "id": 2,
    "username": "Laxmikant",
    "role": "USER"
  },
  "startTime": "2026-09-01T10:00:00",
  "endTime": "2026-09-01T12:00:00",
  "price": 150.00,
  "status": "PENDING"
}
```

---

### 4. Search & Filter Reservations (with Pagination and Sorting)

**Request:**
```http
GET /reservations?status=PENDING&minPrice=50&maxPrice=200&page=0&size=5&sort=startTime,desc
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "resource": { "id": 1, "name": "Conference Hall A", "type": "CONFERENCE_HALL", "capacity": 50 },
      "user": { "id": 2, "username": "Laxmikant", "role": "USER" },
      "startTime": "2026-09-01T10:00:00",
      "endTime": "2026-09-01T12:00:00",
      "price": 150.00,
      "status": "PENDING"
    }
  ],
  "pageable": { "pageNumber": 0, "pageSize": 5 },
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 5. Create Resource (ADMIN Only)

**Request:**
```http
POST /resources
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Executive Boardroom",
  "type": "MEETING_ROOM",
  "capacity": 16
}
```

**Response (200 OK):**
```json
{
  "id": 3,
  "name": "Executive Boardroom",
  "type": "MEETING_ROOM",
  "capacity": 16
}
```

---

## Data Validation Rules

- `name`: Must not be blank.
- `capacity`: Must be an integer >= 1.
- `startTime` / `endTime`: Must be non-null valid ISO ISO-8601 datetimes; `startTime` must precede `endTime`.
- `price`: Must be >= 0.00.

Validation errors return `400 Bad Request` with field-level error messages.

---

## Error Responses

| Scenario | HTTP Status |
|---|---|
| Resource / Reservation not found | `404 Not Found` |
| Validation failure | `400 Bad Request` |
| Unauthorized access (wrong user's data) | `404 Not Found` |
| Missing or invalid JWT token | `403 Forbidden` |
| Insufficient role (e.g., USER calling admin-only endpoint) | `403 Forbidden` |

---

## Running Tests

```bash
./mvnw test
```

### Test Coverage

| Test Class | What It Tests |
|---|---|
| `ReservationServiceTest` | Unit test: verifies a USER cannot access another user's reservation (ownership enforcement) |
| `SecurityAuthorizationTest` | Integration test: verifies unauthenticated requests are rejected and ADMIN can create resources |
| `ResourceBookingApplicationTests` | Spring context loads successfully |

---

## Security Architecture

- **Stateless** sessions — no server-side session state.
- **JWT** tokens are validated on every request via `JwtAuthenticationFilter` (a `OncePerRequestFilter`).
- **BCrypt** password hashing for all stored credentials.
- **CSRF** disabled (appropriate for stateless REST APIs).
- User identity is always resolved from the JWT — never trusted from the request body.
- Verified and fully compatible with Spring Boot 3.2.5 and Java 17/21 runtime environments.