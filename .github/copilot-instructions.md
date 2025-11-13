# Banking Application - AI Coding Instructions

## Architecture Overview
This is a Spring Boot 3.5.7 banking application using Java 17, JPA/Hibernate with MySQL, and Spring Security. The application follows a clean layered architecture: Controller → Service → Repository → Entity.

## Key Patterns & Conventions

### Package Structure
- `controller.api.*` - REST endpoints under `/api/*` paths
- `dto.*` - Request/response objects (use `ApiResponse` for consistent error handling)
- `service.*` - Business logic with `@Transactional` for financial operations
- `exception.*` - Custom RuntimeExceptions for domain-specific errors
- `util.AuthUtils` - Static utility for extracting current user from SecurityContext

### Entity Relationships
- `UserEntity` → `AccountEntity` (1:N via userId field, not JPA relationship)
- `AccountEntity` ↔ `TransactionEntity` (bidirectional @OneToMany with fromAccount/toAccount)
- All entities use `Instant` for timestamps with `@PreUpdate` hooks
- Account numbers are unique strings, balances are Double

### Security & Authentication
- JWT-like stateless security (form login disabled)
- CORS enabled globally via `GlobalCorsConfig`
- Auth endpoints `/api/auth/**` are public
- Use `AuthUtils.getUsername()` to get current user
- `UserService.getCurrentUser()` throws `NoUserLoggedInException` if not authenticated

### Error Handling Pattern
Controllers use try-catch blocks returning `ApiResponse` objects:
```java
try {
    // service call
    return ResponseEntity.ok(result);
} catch (SpecificException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ApiResponse("ERROR", e.getMessage()));
}
```

### Transaction Management
- Use `@Transactional` on service methods that modify financial data
- Account operations (deposit, transfer) create `TransactionEntity` records
- Check user ownership: `AccountRepository.findAccountByIdAndUserId(id, userId)`

### Database Configuration
- MySQL 8 on localhost:3306/bank_db
- Hibernate DDL auto-update enabled
- SQL logging enabled for development
- Use `AccountType` enum: CHECKING, CREDIT, SAVINGS

## Development Workflow

### Running the Application
```bash
./mvnw spring-boot:run
# Application runs on port 8082
```

### Testing
```bash
./mvnw test
# Currently minimal test coverage - enhance as needed
```

### Key Files to Understand
- `SecurityConfig.java` - Authentication setup and endpoint permissions
- `UserService.getCurrentUser()` - Central auth pattern used throughout
- `AccountService.java` - Core banking operations with transaction handling
- `ApiResponse.java` - Standardized response wrapper

## Common Tasks

### Adding New Endpoints
1. Create DTO in `dto/` package
2. Add business logic in appropriate service
3. Create controller method with error handling pattern
4. Ensure user authorization via `getCurrentUser()`

### New Entity Fields
1. Add field to entity class
2. Update constructors and getters/setters  
3. Consider database migration (currently auto-update)
4. Update related DTOs and services

### Financial Operations
- Always use `@Transactional` on service methods
- Create corresponding `TransactionEntity` records
- Validate account ownership and sufficient balance
- Use custom exceptions: `InsufficientBalanceException`, `AccountNotFoundException`