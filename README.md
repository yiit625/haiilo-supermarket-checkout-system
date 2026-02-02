# Supermarket Checkout System

A full-stack web application for managing a supermarket checkout system with support for bulk purchase offers and discount calculations.

## 🎯 Features

- **Product Management**: Create, view, and delete products with unit prices
- **Bulk Offer Management**: Define special bulk purchase offers (e.g., "Buy 3 for €0.75")
- **Smart Checkout**: Automatically calculates total price applying bulk discounts when applicable
- **Real-time Cart**: Add products to cart and see running totals
- **RESTful API**: Well-documented REST API with Swagger/OpenAPI
- **Responsive UI**: Modern Angular Material-based user interface

## 🛠️ Tech Stack

### Backend
- **Java 17** - Modern Java features and performance
- **Spring Boot 3.4.2** - Rapid application development framework
- **Spring Data JPA** - Database abstraction layer
- **PostgreSQL** - Production database
- **H2 Database** - In-memory database for testing
- **Lombok** - Reduces boilerplate code
- **SpringDoc OpenAPI** - API documentation (Swagger)
- **Maven** - Dependency management and build tool

### Frontend
- **Angular 19** - Modern frontend framework
- **Angular Material** - UI component library
- **TypeScript** - Type-safe JavaScript
- **RxJS** - Reactive programming for async operations

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or higher
- **Node.js 18+** and **npm**
- **Maven 3.6+**
- **Docker** and **Docker Compose** (for containerized setup)
- **PostgreSQL 15+** (optional, if running without Docker)

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)

The easiest way to run the entire application:

```bash
# Clone the repository
git clone https://github.com/yiit625/haiilo-supermarket-checkout-system.git
cd haiilo-supermarket-checkout-system

# Start all services (database, backend, frontend)
docker-compose up --build

# The application will be available at:
# - Frontend: http://localhost:4200
# - Backend API: http://localhost:8080
# - Swagger UI: http://localhost:8080/swagger-ui.html
# - PostgreSQL: localhost:5432
```

### Option 2: Local Development

#### Backend Setup

```bash
cd backend

# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Or using local Maven
mvn spring-boot:run

# For local development with H2 database
# Use profile: local
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The backend will start on `http://localhost:8080`

#### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start

# The frontend will be available at http://localhost:4200
```

**Note**: Make sure the backend is running before starting the frontend, as the frontend makes API calls to `http://localhost:8080`.

## 🧪 Running Tests

### Backend Tests

```bash
cd backend

# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Open coverage report
open target/site/jacoco/index.html
```

### Frontend Tests

```bash
cd frontend

# Run unit tests
ng test
```

## 📡 API Documentation

Once the backend is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html

### Main API Endpoints

#### Products
- `POST /api/products` - Create a new product
- `GET /api/products` - Get all products
- `DELETE /api/products/{id}` - Delete a product

#### Bulk Offers
- `POST /api/offers` - Create a bulk offer for a product
- `GET /api/offers` - Get all bulk offers
- `DELETE /api/offers/{id}` - Delete a bulk offer

#### Checkout
- `POST /api/checkout` - Calculate total price for a list of products

**Example Checkout Request:**
```json
{
  "productIds": [1, 1, 1, 2]
}
```

**Response:**
```json
1.25
```

## 🏗️ Architecture

### Backend Architecture

The backend follows a layered architecture pattern:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (Domain Models)
```

**Key Components:**
- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic (checkout calculation, product management)
- **Repositories**: Data access layer using Spring Data JPA
- **Entities**: Domain models with JPA annotations
- **DTOs**: Data Transfer Objects for API communication
- **Exception Handling**: Global exception handler for consistent error responses

### Frontend Architecture

The frontend uses Angular's component-based architecture:

- **Components**: Reusable UI components (product-list, checkout-panel, dialogs)
- **Services**: Business logic and HTTP communication
- **Models**: TypeScript interfaces for type safety
- **Standalone Components**: Modern Angular approach without NgModules

### Database Schema

- **products**: Stores product information (id, name, unitPrice)
- **bulk_offers**: Stores bulk purchase offers (id, product_id, requiredQuantity, offerPrice)
- **BaseEntity**: Abstract class providing createdAt and updatedAt timestamps

## 💡 Design Decisions

### Why BigDecimal?

Using `BigDecimal` instead of `double` or `float` prevents floating-point precision errors when dealing with calculations. This is a critical best practice for financial applications.

### Bulk Discount Algorithm

The checkout service implements a smart discount calculation:
1. Groups products by type and counts quantities
2. For each product group:
   - Checks if a bulk offer exists
   - Calculates how many "batches" qualify for the offer
   - Applies offer price for batches, regular price for remainder
3. Sums all product group totals

**Example**: 
- Product: Apple (€0.30 each)
- Offer: 3 for €0.75
- Cart: 5 apples
- Calculation: (1 × €0.75) + (2 × €0.30) = €1.35

I chose List<Long> for the checkout request to strictly follow the requirement: "items, in any order". This represents a real-world stream of scanned items (like a barcode scanner) and preserves the sequence of the customer's actions.

### Dynamic Weekly Offers
- I implemented an expiryDate field to support the "Weekly" nature of offers. By using JPA @PrePersist hooks, the system automatically sets a default 1-week validity if no date is provided. During checkout, the system dynamically filters out expired offers, ensuring that business rules are enforced without manual intervention.

### Technology Choices

- **Spring Boot**: Industry standard, excellent ecosystem, rapid development
- **Angular**: Type-safe, component-based, excellent tooling
- **PostgreSQL**: Robust, ACID-compliant, production-ready
- **Docker**: Consistent environments, easy deployment

## 📁 Project Structure

```
haiilo-supermarket-checkout-system/
├── backend/
│   ├── src/
│   │   ├── main/java/com/haiilo/demo/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── repository/       # Data access
│   │   │   ├── entity/           # Domain models
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── config/           # Configuration classes
│   │   │   └── advice/           # Exception handling
│   │   └── test/                 # Test classes
│   └── pom.xml                   # Maven dependencies
├── frontend/
│   ├── src/app/
│   │   ├── components/           # Angular components
│   │   ├── services/             # Angular services
│   │   ├── models/               # TypeScript interfaces
│   │   └── app.component.*       # Root component
│   └── package.json              # NPM dependencies
└── docker-compose.yml            # Multi-container setup
```

## 🧪 Testing Strategy

### Backend Tests
- **Unit Tests**: Service layer logic with mocked dependencies
- **Integration Tests**: End-to-end API testing with TestRestTemplate
- **Controller Tests**: Web layer testing with MockMvc

### Frontend Tests
- **Component Tests**: Angular component testing with TestBed
- **Service Tests**: Service layer testing with HTTP mocking

### Test Coverage
- Checkout service logic (bulk discount calculations)
- Product and offer management
- API endpoints (success and error cases)
- Frontend components and services

### 🔍 Performance & Optimization
- Indexing Strategy: The system is optimized using B-Tree Indexing on Primary Keys (ID), ensuring $O(\log n)$ performance for direct lookups.
- Payload Protection: To prevent Denial of Service (DoS) attacks and excessive memory consumption, the CheckoutRequest is strictly limited to 500 items per request using Jakarta Validation (@Size(max = 500)).
- Used LEFT JOIN FETCH in the repository layer to solve the N+1 problem, ensuring all necessary offer data is retrieved in a single database round-trip.

### 🏗️ Future-Proof Architecture
- Scalable Offer Schema: Although currently limited to one active bulk offer per product for business simplicity, the BulkOffer is decoupled in the database. Why? This allows us to easily transition to a One-to-Many relationship in the future (e.g., "Buy 3 for €1" OR "Buy 10 for €3") with minimal schema changes.

## 👤 Author

Developed as a take-home assignment demonstrating full-stack development capabilities. YIGIT SAHIN

**Happy Coding! 🎉**
