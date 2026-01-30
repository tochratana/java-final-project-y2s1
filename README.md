## Skill Exchange Platform
The Skill Exchange Platform is a community-driven application where users can share their expertise and learn new skills through direct exchange - no money involved. Whether you want to learn guitar in exchange for teaching programming, or trade photography lessons for cooking classes, this platform facilitates those connections.

---

### Features

User Management
* User registration with validation 
* Secure login system (password hashing ready)
* Profile management 
* Role-based access (USER, ADMIN)

Skill Management
* Browse all available skills from other users 
* View skill details including owner information 
* Filter by availability status

Exchange System
* Create skill exchange requests 
* View sent requests 
* View received requests 
* Accept or reject incoming requests 
* Dual confirmation system for completion 
* Status tracking (PENDING → ACCEPTED → COMPLETED)

Business Rules
* Users cannot request exchanges with themselves 
* Both parties must confirm completion 
* Request validation and authorization 
* Automatic status management

Architecture
* MVC Pattern - Separation of concerns 
* Repository Pattern - Data access abstraction 
* Service Layer - Business logic encapsulation 
* DTO Pattern - Data transfer between layers

---

### Project Structure

```bash
skill-exchange-platform/
│
├── src/
│   ├── config/
│   │   └── DatabaseConfig.java          # Database connection management
│   │
│   ├── model/
│   │   ├── User.java                    # User entity
│   │   ├── Skill.java                   # Skill entity
│   │   └── ExchangeRequest.java         # Exchange request entity
│   │
│   ├── dto/
│   │   ├── UserRegistrationDTO.java     # User registration data
│   │   ├── UserLoginDTO.java            # Login credentials
│   │   ├── SkillDTO.java                # Skill transfer object
│   │   ├── ExchangeRequestDTO.java      # Exchange request display
│   │   └── CreateExchangeRequestDTO.java # Exchange request creation
│   │
│   ├── repository/
│   │   ├── UserRepository.java          # User data interface
│   │   ├── SkillRepository.java         # Skill data interface
│   │   ├── ExchangeRequestRepository.java
│   │   └── impl/
│   │       ├── UserRepositoryImpl.java
│   │       ├── SkillRepositoryImpl.java
│   │       └── ExchangeRequestRepositoryImpl.java
│   │
│   ├── service/
│   │   ├── AuthService.java             # Authentication interface
│   │   ├── SkillService.java            # Skill management interface
│   │   ├── ExchangeService.java         # Exchange logic interface
│   │   └── impl/
│   │       ├── AuthServiceImpl.java
│   │       ├── SkillServiceImpl.java
│   │       └── ExchangeServiceImpl.java
│   │
│   ├── controller/
│   │   ├── AuthController.java          # Authentication flow
│   │   ├── SkillController.java         # Skill operations
│   │   └── ExchangeController.java      # Exchange operations
│   │
│   ├── view/
│   │   ├── LoginView.java               # Login UI
│   │   ├── MainView.java                # Main menu UI
│   │   ├── SkillView.java               # Skill management UI
│   │   └── ExchangeView.java            # Exchange UI
│   │
│   └── main/
│       └── Main.java                    # Application entry point
│
├── database/
│   └── schema.sql                       # Database schema and sample data
│
├── docs/
│   └── demo-scenario.md                 # Demo walkthrough
│
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

---

### Table for using in this project

#### 1. User table
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. Skill table 
```sql
CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    skill_level VARCHAR(20) NOT NULL CHECK (skill_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT')),
    experience_years INTEGER DEFAULT 0 CHECK (experience_years >= 0),
    description TEXT,
    availability BOOLEAN DEFAULT true,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    CONSTRAINT fk_skill_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
);
```


#### 3. Exchange Request Table
```sql
CREATE TABLE exchange_requests (
    id SERIAL PRIMARY KEY,
    requester_id INTEGER NOT NULL,
    provider_id INTEGER NOT NULL,
    requested_skill VARCHAR(100) NOT NULL,
    offered_skill VARCHAR(100) NOT NULL,
    request_message TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'COMPLETED', 'CANCELLED')),
    requester_confirmed BOOLEAN DEFAULT false,
    provider_confirmed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_exchange_requester 
        FOREIGN KEY (requester_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_exchange_provider 
        FOREIGN KEY (provider_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,
    
    -- Constraint: User cannot request exchange with themselves
    CONSTRAINT chk_different_users 
        CHECK (requester_id != provider_id)
);
```

---


Made with Java Class at [ISTAD](https://www.cstad.edu.kh/) for final project year2 semester1