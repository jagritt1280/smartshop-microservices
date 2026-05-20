# 🛒 SmartShop Microservices

A production-ready e-commerce platform built with Spring Boot microservices architecture.

## 🚀 Tech Stack

| Technology | Usage |
|-----------|-------|
| Spring Boot 3.5 | Microservices framework |
| Java 21 | Programming language |
| MySQL 8.0 | Relational database |
| MongoDB Atlas | Product catalog |
| Apache Kafka | Event streaming |
| Netflix Eureka | Service discovery |
| Spring Cloud Gateway | API Gateway |
| JWT | Authentication |
| Docker | Containerization |
| Swagger/OpenAPI | API documentation |

## 📦 Services

### 1. Eureka Server (8761)
- Service registry and discovery
- All services register here
- Health monitoring

### 2. API Gateway (8080)
- Single entry point
- Request routing
- CORS handling
- Load balancing via Eureka

### 3. User Service (8081)
- User registration and login
- JWT token generation
- BCrypt password hashing
- MySQL database

### 4. Product Service (8082)
- Product CRUD operations
- MongoDB Atlas storage
- Product search and filtering

### 5. Inventory Service (8083)
- Stock management
- Kafka producer for stock events
- MySQL database

### 6. Order Service (8084)
- Order lifecycle management
- Status: PENDING → CONFIRMED → SHIPPED → DELIVERED
- Kafka event publishing
- MySQL database

### 7. Payment Service (8085)
- Payment processing
- Idempotency key support
- Payment ledger
- MySQL database

### 8. Notification Service (8086)
- Kafka consumer
- Email notifications
- Order status updates

## 🐳 Docker Setup

### Prerequisites
- Docker Desktop or Rancher Desktop
- Java 21
- Maven

### Run all services:

```bash
# Clone the repo
git clone https://github.com/jagritt1280/smartshop-microservices.git
cd smartshop-microservices

# Build all JARs
for service in eureka-server api-gateway user-service product-service inventory-service order-service payment-service notification-service; do
  cd $service
  ./mvnw clean package -DskipTests
  cd ..
done

# Start all containers
docker compose up --build
```

### Services will be available at:
| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8080 |
| User Service Swagger | http://localhost:8081/swagger-ui/index.html |
| Product Service Swagger | http://localhost:8082/swagger-ui/index.html |
| Inventory Service Swagger | http://localhost:8083/swagger-ui/index.html |
| Order Service Swagger | http://localhost:8084/swagger-ui/index.html |
| Payment Service Swagger | http://localhost:8085/swagger-ui/index.html |

## 🔑 API Endpoints

### Auth

POST /api/auth/register  → Register user
POST /api/auth/login     → Login + get JWT token


### Products
GET  /api/products       → Get all products
GET  /api/products/{id}  → Get product by ID
POST /api/products       → Create product
PUT  /api/products/{id}  → Update product

### Orders
POST /api/orders              → Place order
GET  /api/orders/user/{id}    → Get user orders
GET  /api/orders/{id}         → Get order by ID
PUT  /api/orders/{id}/status  → Update order status

### Payments
POST /api/payments/initiate      → Process payment
POST /api/payments/{id}/confirm  → Confirm payment

## 🔐 Authentication

All protected endpoints require JWT token:
Authorization: Bearer <token>

Get token from login endpoint and include in all requests.

## 🎯 Key Features

- ✅ JWT Authentication
- ✅ Service Discovery (Eureka)
- ✅ API Gateway routing
- ✅ Event-driven architecture (Kafka)
- ✅ Idempotent payment processing
- ✅ Docker containerization
- ✅ Swagger API documentation
- ✅ Global exception handling
- ✅ MongoDB Atlas integration
- ✅ CORS configuration

## 🌐 Frontend

React frontend available at:
[SmartShop Frontend](https://github.com/jagritt1280/smartshop-frontend)
