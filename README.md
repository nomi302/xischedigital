# Xische Digital

# Billing System API

## Overview
This project implements a billing system with discount calculation and currency conversion features. The API integrates with an external **Exchange Rate API (open.er-api.com)** to fetch real-time exchange rates.

## Features
- Calculates bill amounts with applicable discounts.
- Converts the total bill amount to a target currency.
- Uses caching (In-Memory) to optimize exchange rate requests.
- Provides authentication using API Key.

## Technologies Used
- **Java 17**  
- **Spring Boot**  
- **Spring Security** (for API key authentication)  
- **Spring Cache** (for exchange rate caching)  
- **JUnit & Mockito** (for testing)  
- **MockMvc** (for API testing)  

---

## 📌 Architecture Diagram  
The high-level architecture of the system is illustrated below:

![Software Architecture Diagram](https://github.com/user-attachments/assets/042c93db-ba36-4b66-9c6e-50b750aeb4c5)

---


## 📌 UML Diagram  
The system's class structure and interactions:


![UML of CurrencyExchangeDiscountApp](https://github.com/user-attachments/assets/106a63a5-2fa3-4fe7-8344-9091c81bf5aa)

---

## 📌 System Sequence Diagram  
This sequence diagram shows the flow of API calls from client to services:

![SSD](https://github.com/user-attachments/assets/f69aa76e-b378-4607-b9ca-9bd074924d2a)

---

## 📌 Coverage Report  
This sequence diagram shows the flow of API calls from client to services:


![Coverage Report](https://github.com/user-attachments/assets/b46b5145-959e-4fa3-b19b-dd97ae987b30)

---

## Setup & Installation

### Prerequisites
- Install **Java 17** or later
- Install **Maven** (`mvn`)

### Configure Environment Variables
To set up environment variables, update `application.properties`:

```properties
exchange.rate.api.url=https://open.er-api.com/v6/latest
exchange.rate.api.key=your_api_key_here
```

### Build and Run the Application
To build and start the application, execute the following commands:

```sh
mvn clean install
mvn spring-boot:run
```

---

## Running Tests & Coverage Reports

### Running Tests
To execute unit and integration tests, run:

```sh
mvn test
```

### Generating Test Coverage Reports
To generate test coverage reports using **JaCoCo**, run:

```sh
mvn clean verify
```
The report will be available at:

```
target/site/jacoco/index.html
```

### Coverage Report Screenshot  
For reference, here’s a sample coverage report:

![Coverage Report](https://github.com/user-attachments/assets/c1fcbccc-b5b6-479f-a2f2-122b8022b56f)

---

## API Endpoints
| Method | Endpoint                               | Description                     |
|--------|----------------------------------------|---------------------------------|
| POST   | `/calculate`                           | Calculates the bill with discount |

---


## Sample Request & Response

### **Request:**
```json
{
  "items": [
    {"category": "electronics", "price": 300},
    {"category": "clothing", "price": 150}
  ],
  "userType": "employee",
  "customerSince": "2019-05-15",
  "originalCurrency": "USD",
  "targetCurrency": "GBP"
}
```

### **Response:**
```json
{
  "netAmount": 232.05
}
```

### **Request URL:**
```
http://localhost:8080/api/calculate
```

### **API Key (Header):**
```
X-API-Key: NjY4M2U1MDYtOGM0OC00ODIzLWI0OTgtNWQ2ZjI4MzQ0YjFj
```

## External Integration  
This API integrates with the **Exchange Rate API**:  
**Base URL:** `[https://open.er-api.com/v6/latest](https://v6.exchangerate-api.com)`

---

## License  
Numan Tariq License. See `LICENSE` file for details.


---

For any issues, feel free to open a ticket or reach out to the contributors.


