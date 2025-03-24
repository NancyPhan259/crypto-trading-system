# Crypto Trading System

This project is a simplified Crypto Trading System using Java Spring Boot.  
It allows users to:
- Fetch and aggregate crypto prices from Binance and Huobi.
- Cache best prices using Redis.
- Trade crypto pairs (buy/sell).
- Track trade history.
- View asset balances.
- Use Redis Pub/Sub to publish trade events.
- Auto-update best prices every 10 seconds using a scheduler with ShedLock.

---

## 💻 Tech Stack

- Java 17+
- Spring Boot 3
- H2 in-memory Database
- Redis (with Redisson)
- ShedLock for distributed task locking
- JPA/Hibernate
- Postman for testing API

---

## 📦 Setup Instructions

### 1. Requirements

- Java 17 or higher
- Redis server running locally on port 6379

### 2. How to Run

```bash
git clone https://github.com/yourname/crypto-trading-system.git
cd crypto-trading-system
./mvnw spring-boot:run
