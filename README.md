# xischedigital

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

![SSD](https://github.com/user-attachments/assets/f69aa76e-b378-4607-b9ca-9bd074924d2a)

---

## Setup & Installation

### Prerequisites
- Install **Java 17** or later
- Install **Maven** (`mvn`)

### Running the Application
1. Clone the repository:  
   ```sh
   git clone https://github.com/your-repo.git
   cd your-repo




   
