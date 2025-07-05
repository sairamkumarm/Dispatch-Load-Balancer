# Dispatch Load Balancer - Backend Assignment

Spring Boot application that optimizes the assignment of delivery orders to a fleet of vehicles based on location, vehicle capacity, and delivery priority. Orders are assigned to minimize total travel distance using the Haversine formula.

---

## Tech Stack
- Java 21
- Spring Boot 3.5.3
- Maven
- JUnit 5
- Lombok

---

## Architecture Overview

### Monolithic Design
This project is implemented as a **monolith**, not a microservice, for the following reasons:
- Single-domain responsibility (dispatch planning only)
- No external integrations (no need for async comms, discovery, etc.)
- Simple persistence layer (in-memory or file-based)
- Faster to develop, test, and deploy within a limited timeframe

---

## Core Features

### ✅ Accept Delivery Orders
- POST `/api/dispatch/orders`
- Accepts a list of orders with coordinates, address, weight, and priority

### ✅ Accept Vehicle Details
- POST `/api/dispatch/vehicles`
- Accepts a list of vehicles with location and capacity

### ✅ Generate Dispatch Plan
- GET `/api/dispatch/plan`
- Returns optimized dispatch allocation
  - High-priority orders assigned first
  - Total load within vehicle capacity
  - Nearest vehicle selected using Haversine

---
### Extras

### ✨ Plan Metrics Summary 
Response includes:
- Total orders, assigned/unassigned count
- Total + average travel distance
- Great for audits and logistics insights

### 🔁 Dispatch Strategy Interface 
Built with strategy interface to allow:
- Swappable algorithms (e.g. `Greedy`, `PriorityFirst`)
- Easier testing, experimentation, and future enhancements

---

## 📐 Data Models

### Order
```json
{
  "orderId": "ORD001",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "address": "Connaught Place, Delhi, India",
  "packageWeight": 10,
  "priority": "HIGH"
}
```

### Vehicle
```json
{
  "vehicleId": "VEH001",
  "capacity": 100,
  "currentLatitude": 28.6139,
  "currentLongitude": 77.2090,
  "currentAddress": "Connaught Place, Delhi, India"
}
```

### Dispatch Plan Response
```json
{
  "planStats": {
    "totalOrders": 30,
    "assignedOrders": 28,
    "unassignedOrders": 2,
    "averageDistance": "5.6 km",
    "totalDistance": "140.3 km"
  },
  "dispatchPlan": [
    {
      "vehicleId": "VEH001",
      "totalLoad": 25,
      "totalDistance": "10.3 km",
      "assignedOrders": [ ... ]
    },
    ...
  ]
}
```

---

## Prioritization Logic
- Orders sorted by priority: `HIGH > MEDIUM > LOW`
- Then by vehicle proximity (Haversine)
- Order is only assigned if it fits into the remaining capacity

---

## Flow Summary
```
Client → Controller → Service → OrderAssigner (Strategy) → HaversineUtil → DispatchPlan
```

---

## Package Structure
```
com.dispatch
├── api               // Controllers (REST layer only)
├── dto               // Request & response payloads
├── model             // Domain entities (Order, Vehicle, etc.)
├── service
│   ├── core          // DispatchService, OrderAssigner, Strategy Interface
│   └── store         // In-memory store service
├── util              // Haversine + Mapper utils
├── config            // Enum, strategy registry (future proof)
├── exception         // Custom exceptions + handler
└── test              // Unit + integration tests
```

---

## Build & Run
### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

App runs on `http://localhost:8080`

---

## Testing
### Run All Tests
```bash
mvn test
```

Test coverage includes:
- Order/vehicle input validation
- Assignment plan generation
- Haversine distance accuracy
- Capacity & priority edge cases
- Strategy algorithm output verification

---

## Error Handling
- Invalid/missing fields → `400 Bad Request`
- Empty orders/vehicles → returns empty plan
- Overcapacity → order skipped, not failed

---

## API Spec
See [OpenAPI JSON file](./dispatch-loadbalancer-openapi.json) for full schema.

Import into:
- Postman
- Swagger UI
- Stoplight Studio

---

## Future Improvements
- Export plan as CSV
- Persist orders/vehicles in DB (H2 or Postgres)
- Add pagination and plan caching
- Implement zone-based strategy

---

## Author
Sairam – Built for FreightFox

---
