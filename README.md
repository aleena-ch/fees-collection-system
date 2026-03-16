# Fee Collection System

A microservices-based student fee collection
system built with Spring Boot.

## Architecture
```
API Gateway (8080)
    ├── Student Service (8081)
    └── Fee Service (8082)
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| API Gateway | 8080 | Single entry point |
| Student Service | 8081 | Manages students |
| Fee Service | 8082 | Manages fee collection |

## Tech Stack
- Spring Boot 4.0.3
- Spring Data JPA
- H2 In-memory Database
- Spring Cloud OpenFeign
- Spring Cloud Gateway WebMVC
- Lombok
- JUnit 5 + Mockito

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps
```bash
# 1. Clone the repository
git clone https://github.com/aleena-ch/fees-collection-system

# 2. Start Student Service
cd student-service
mvn spring-boot:run

# 3. Start Fee Service
cd fee-service
mvn spring-boot:run

# 4. Start API Gateway
cd api-gateway
mvn spring-boot:run
```
## API Endpoints

### Student Service
```
POST /api/v1/students
→ Add new student

GET /api/v1/students/{studentId}
→ Get student by ID

GET /api/v1/students
→ Get all students (paginated)
```

### Fee Service
```
POST /api/v1/fees/collect
→ Collect fee and generate receipt

GET /api/v1/fees/receipt/{receiptNumber}
→ Get receipt by number

GET /api/v1/fees/receipt/student/{studentId}
→ Get all receipts for student
```
### Access Swagger UI
```
Student Service:
http://localhost:8081/swagger-ui.html

Fee Service:
http://localhost:8082/swagger-ui.html
```
## Postman Collection

A Postman collection is available to test
all APIs in the `docs/` folder.

### How to Import
```
1. Open Postman
2. Click Import
3. Select file:
   docs/fee-collection-system.postman_collection.json
4. Click Import
```

### Collection Structure
```
Fee Collection System/
├── Student APIs/
│   ├── Add Student          (POST)
│   ├── Get Student by ID    (GET)
│   └── Get All Students     (GET)
├── Fee APIs/
│   ├── Collect Fee - Card   (POST)
│   ├── Collect Fee - Cash   (POST)
│   ├── Get Receipt          (GET)
│   └── Get Student Receipts (GET)
└── Error Scenarios/
    ├── Validation Error     (400)
    ├── Student Not Found    (404)
    ├── Duplicate Fee        (409)
    └── Invalid Amount       (400)
```

### Base URL
```
All requests use Gateway:
http://localhost:8080
```

## Sample Requests

### Add Student
```json
POST /api/v1/students
{
  "studentName": "Mary John",
  "grade": "Grade 1",
  "mobileNumber": "+971501234567",
  "schoolName": "Skiply School of Excellence"
}
```

### Collect Fee
```json
POST /api/v1/fees/collect
{
  "studentId": "STU-2026-001",
  "feeType": "TUITION",
  "feeDescription": "Tuition Fees - Grade 1",
  "amount": 500.00,
  "currency": "AED",
  "paymentMode": "CARD",
  "cardNumber": "1234-5678-1236-0081",
  "cardType": "MASTERCARD",
  "transactionReference": "132318047471",
  "academicYear": "2026-27",
  "feeMonth": "APRIL",
  "collectedBy": "Admin"
}
```

## Error Responses

| Status | Code | Description |
|--------|------|-------------|
| 400 | VALIDATION_ERROR | Invalid input |
| 404 | STUDENT_NOT_FOUND | Student not found |
| 404 | FEE_NOT_FOUND | Receipt not found |
| 409 | DUPLICATE_FEE | Fee already paid |
| 503 | SERVICE_UNAVAILABLE | Service down |
| 500 | INTERNAL_SERVER_ERROR | Unexpected error |

