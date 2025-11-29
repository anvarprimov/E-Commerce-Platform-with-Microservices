# 🛒 E‑Commerce Microservices Platform

A scalable, production‑grade **E‑commerce Microservices Platform** built with **Spring Boot 3, Spring Cloud, Keycloak, Docker, and PostgreSQL**. The architecture follows modern enterprise patterns: distributed services, API gateway, service registry, centralized config, and event‑driven communication.

---

## 🚀 Key Features

* **7+ Microservices**: User, Product, Cart, Order, Payment, Notification
* **Spring Cloud Gateway** for routing & edge security
* **Eureka Discovery Server** for service registry & dynamic load balancing
* **Distributed configuration** via Config Server
* **Keycloak OAuth2/OIDC + JWT** for secure authentication & authorization
* **OpenAPI 3 + Swagger UI** per service
* **Docker & Docker Compose** for easy local deployment
* **PostgreSQL** for persistent storage
* **Event-driven communication** between services (RabbitMQ/Kafka ready)
* **Fault‑tolerant architecture** with retry patterns

---

## 🧱 Architecture Overview

```
                  +---------------------+
                  |   API Gateway       |
                  +----------+----------+
                             |
              +--------------+----------------+
              |              |                |
        +-----+-----+  +-----+-----+   +------+-----+
        |  User     |  | Product   |   |   Cart      |
        +-----------+  +-----------+   +------------+
              |              |                |
        +-----+-----+  +-----+-----+   +------+-----+
        |  Order    |  | Payment   |   | Notification|
        +-----------+  +-----------+   +-------------+
              \\           |             //
               \\         |            //
                 +--------+----------+
                 |  Eureka Server    |
                 +-------------------+
```

---

## 🛠️ Tech Stack

### Backend

* Java 17+
* Spring Boot 3
* Spring Cloud (Gateway, Eureka, Config, etc.)
* Spring Security (OAuth2 + Keycloak)
* MapStruct
* Lombok
* Resilience4j
* OpenAPI/Swagger

### Infrastructure

* Docker
* Docker Compose
* PostgreSQL
* Kafka/RabbitMQ

---

## 📦 Microservices

### 1. **User Service**

* Authentication & authorization
* Profile management
* JWT tokens via Keycloak

### 2. **Product Service**

* CRUD operations
* Categories, details, pricing
* Search support

### 3. **Cart Service**

* Add/remove items
* Update quantities
* Works with Product & User service

### 4. **Order Service**

* Checkout process
* Order creation & validation

### 5. **Payment Service**

* Payment request + response flows
* Event-driven processing

### 6. **Notification Service**

* Email or async messages
* Decoupled delivery events

---

## 🔐 Authentication & Authorization

Using **Keycloak** identity provider:

* Access tokens
* Refresh tokens
* Role-based access control (RBAC)

APIs: protected by **Spring Security + Keycloak Adapter**.

---

## 🔗 API Documentation

Each service exposes Swagger UI at runtime:

```
/service-name/swagger-ui.html
```

And OpenAPI specs:

```
/service-name/v3/api-docs
```

---

## 🐳 Running Locally with Docker

```bash
docker compose up --build
```

Services run automatically, and Eureka + Gateway connect dynamically.

---

## 📁 Folder Structure (simplified)

```
├── gateway-service
├── eureka-server
├── config-server
├── user-service
├── product-service
├── order-service
├── cart-service
├── notification-service
├── payment-service
└── docker-compose.yml
```

---

## 🧪 Future Enhancements

* Kubernetes deployment
* Observability: Grafana + Prometheus
* Event-sourcing & CQRS
* Caching layer (Redis)
* Saga pattern for distributed transactions

---

## 🤝 Contributions

Feel free to submit issues or PRs.

---

## 📝 License

No license has been selected yet. This project is currently not licensed for public or commercial use.


---

## ⭐ If you like it

Leave a star and follow for updates!
