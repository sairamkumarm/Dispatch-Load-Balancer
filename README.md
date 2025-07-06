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
- Total orders, unassigned orders list
- Total + average travel distance
- Great for audits and logistics insights

### 🔁 Dispatch Strategy Interface 
Built with strategy interface to allow:
- Swappable algorithms (e.g. `Greedy`, `PriorityFirst`)
- Easier testing, experimentation, and future enhancements
- Includes a resultBuilder for easy summary building
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
  "message": "Plan created",
  "status": "Success",
  "data": [
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
### Dispatch Plan Response with summary
```json
{
  "message": "Full Plan created",
  "status": "Success",
  "data": {
    "totalOrders": 22,
    "averageDistance": "16 km",
    "totalDistance": "83 km",
    "totalLoad": 490,
    "dispatchPlan": [
      {
        "vehicleId": "VEH005",
        "totalLoad": 110,
        "totalDistance": "14 km",
        "assignedOrders": [
          {
            "orderId": "ORD026",
            "latitude": 28.7041,
            "longitude": 77.1025,
            "address": "Karol Bagh, Delhi, India",
            "packageWeight": 25,
            "priority": "HIGH"
          },
          ...
        ]
      }
    ],
    "unassignedOrders": [
      {
        "orderId": "ORD010",
        "latitude": 28.6139,
        "longitude": 77.209,
        "address": "Connaught Place, Delhi, India",
        "packageWeight": 30,
        "priority": "LOW"
      },
      ...
    ]
  }
}
```
---

## Prioritization Logic
- Orders sorted by priority: `HIGH > MEDIUM > LOW`
- Then within the priority, orders are sorted on weight, to prevent fragmentation
- Vehicle is only considered if order(n) fits into the remaining capacity
- Vehicle closest to order(n) claims the delivery
- If an order is too big to fit in the remaining capacity of any vehicle then it stays unassigned
- This logic is greedy and aims to find the local minima, while compromising on finding an elusive global minima

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
- Persist orders/vehicles in DB (H2 or Postgres)
- Add pagination and plan caching
- Implement zone-based strategy

---

## Author
Sairam – Built for FreightFox

---
