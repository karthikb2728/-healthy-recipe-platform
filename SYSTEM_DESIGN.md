# System Design and Architecture

## System Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Database      │
│   (React/Vue)   │◄──►│   Spring Boot   │◄──►│   MySQL         │
│                 │    │   REST API      │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │   Security      │
                    │   JWT + Spring  │
                    │   Security      │
                    └─────────────────┘
```

## System Components

### 1. Presentation Layer
- **REST Controllers**: Handle HTTP requests and responses
- **DTOs**: Data Transfer Objects for request/response mapping
- **Exception Handlers**: Global error handling
- **Security Filters**: JWT authentication and authorization

### 2. Business Logic Layer
- **Services**: Core business logic implementation
- **Recommendation Engine**: Algorithm for recipe suggestions
- **Authentication Service**: User login and registration
- **Authorization**: Role-based access control

### 3. Data Access Layer
- **Repositories**: JPA repositories for data access
- **Entities**: JPA entities mapping to database tables
- **Database**: MySQL for persistent storage

### 4. Security Layer
- **JWT Authentication**: Stateless authentication
- **Password Encryption**: BCrypt password hashing
- **Role-based Authorization**: USER, CHEF, ADMIN roles
- **CORS Configuration**: Cross-origin resource sharing

## Entity Relationship Diagram

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    User     │     │   Recipe    │     │  Ingredient │
│─────────────│     │─────────────│     │─────────────│
│ id (PK)     │────►│ id (PK)     │────►│ id (PK)     │
│ username    │     │ title       │     │ recipe_id   │
│ email       │     │ description │     │ name        │
│ password    │     │ instructions│     │ quantity    │
│ firstName   │     │ prepTime    │     │ unit        │
│ lastName    │     │ cookTime    │     │ notes       │
│ role        │     │ servings    │     └─────────────┘
│ status      │     │ difficulty  │
│ fitnessGoal │     │ imageUrl    │
│ calorieTarget│     │ status      │
└─────────────┘     │ author_id   │
       │            │ nutrition   │
       │            └─────────────┘
       │                   │
       │                   │
       ▼                   ▼
┌─────────────┐     ┌─────────────┐
│   Rating    │     │  Favorite   │
│─────────────│     │─────────────│
│ id (PK)     │     │ id (PK)     │
│ user_id     │     │ user_id     │
│ recipe_id   │     │ recipe_id   │
│ rating      │     │ created_at  │
│ comment     │     └─────────────┘
│ created_at  │
└─────────────┘
```

## Class Diagram

```
┌─────────────────────────────────┐
│           User                  │
├─────────────────────────────────┤
│ - id: Long                      │
│ - username: String              │
│ - email: String                 │
│ - password: String              │
│ - firstName: String             │
│ - lastName: String              │
│ - role: Role                    │
│ - status: AccountStatus         │
│ - dietaryPreferences: Set       │
│ - allergies: Set<String>        │
│ - fitnessGoal: FitnessGoal      │
│ - dailyCalorieTarget: Integer   │
├─────────────────────────────────┤
│ + getters/setters               │
└─────────────────────────────────┘
                │
                │ 1:N
                ▼
┌─────────────────────────────────┐
│          Recipe                 │
├─────────────────────────────────┤
│ - id: Long                      │
│ - title: String                 │
│ - description: String           │
│ - instructions: String          │
│ - preparationTime: Integer      │
│ - cookingTime: Integer          │
│ - servings: Integer             │
│ - difficultyLevel: Difficulty   │
│ - status: RecipeStatus          │
│ - nutritionInfo: NutritionInfo  │
│ - categories: Set<Category>     │
│ - dietaryTags: Set<String>      │
├─────────────────────────────────┤
│ + getters/setters               │
│ + getTotalTime(): Integer       │
└─────────────────────────────────┘
```

## Sequence Diagrams

### User Authentication Flow
```
User → AuthController → AuthService → UserRepository → Database
  │         │              │             │              │
  │ login   │              │             │              │
  ├────────►│              │             │              │
  │         │ authenticate │             │              │
  │         ├─────────────►│             │              │
  │         │              │ findByUsername            │
  │         │              ├────────────►│              │
  │         │              │             │ SELECT       │
  │         │              │             ├─────────────►│
  │         │              │             │◄─────────────┤
  │         │              │◄────────────┤              │
  │         │ JWT Token    │             │              │
  │         │◄─────────────┤             │              │
  │◄────────┤              │             │              │
  │         │              │             │              │
```

### Recipe Creation Flow
```
User → RecipeController → RecipeService → RecipeRepository → Database
  │         │               │              │               │
  │ POST    │               │              │               │
  ├────────►│               │              │               │
  │         │ createRecipe  │              │               │
  │         ├──────────────►│              │               │
  │         │               │ save         │               │
  │         │               ├─────────────►│               │
  │         │               │              │ INSERT        │
  │         │               │              ├──────────────►│
  │         │               │              │◄──────────────┤
  │         │               │◄─────────────┤               │
  │         │ Recipe        │              │               │
  │         │◄──────────────┤              │               │
  │◄────────┤               │              │               │
  │         │               │              │               │
```

## API Architecture

### REST API Design Principles
1. **Resource-based URLs**: `/api/recipes`, `/api/users`
2. **HTTP Methods**: GET, POST, PUT, DELETE
3. **Status Codes**: 200, 201, 400, 401, 403, 404, 500
4. **JSON Format**: Request and response bodies in JSON
5. **Pagination**: Page-based pagination for collections
6. **Filtering**: Query parameters for filtering results

### Authentication & Authorization
```
Request → JWT Filter → Spring Security → Controller
   │          │            │              │
   │ Bearer   │            │              │
   │ Token    │            │              │
   ├─────────►│            │              │
   │          │ Validate   │              │
   │          ├───────────►│              │
   │          │            │ Authorize    │
   │          │            ├─────────────►│
   │          │            │              │ Process
   │          │            │              │ Request
```

## Data Flow Architecture

### Recipe Recommendation Flow
```
User Profile → Recommendation Engine → Database Query → Filtered Results
     │               │                      │               │
     │ Preferences    │                      │               │
     ├───────────────►│                      │               │
     │               │ Algorithm            │               │
     │               ├─────────────────────►│               │
     │               │                      │ SQL Query     │
     │               │                      │               │
     │               │ Recommendations      │               │
     │               │◄─────────────────────┤               │
     │◄──────────────┤                      │               │
     │               │                      │               │
```

### Rating System Flow
```
User Rating → Validation → Database → Recipe Stats Update
     │           │           │             │
     │ 1-5 stars │           │             │
     ├──────────►│           │             │
     │           │ Validate  │             │
     │           ├──────────►│             │
     │           │           │ INSERT/UPDATE
     │           │           │             │
     │           │           │ Recalculate │
     │           │           ├────────────►│
     │           │           │             │ Avg Rating
     │           │ Success   │             │
     │◄──────────┤           │             │
     │           │           │             │
```

## Security Architecture

### JWT Token Structure
```
Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234654290,
  "roles": ["ROLE_USER"]
}

Signature: HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### Role-based Access Control
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    USER     │    │    CHEF     │    │   ADMIN     │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ • View      │    │ • All USER  │    │ • All CHEF  │
│   recipes   │    │   permissions│    │   permissions│
│ • Rate      │    │ • Enhanced  │    │ • User      │
│   recipes   │    │   recipe    │    │   management│
│ • Favorite  │    │   creation  │    │ • Recipe    │
│   recipes   │    │             │    │   approval  │
│ • Create    │    │             │    │ • System    │
│   recipes   │    │             │    │   admin     │
└─────────────┘    └─────────────┘    └─────────────┘
```

## Deployment Architecture

### Production Deployment
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Nginx     │    │ Spring Boot │    │   MySQL     │
│ Load        │    │ Application │    │ Database    │
│ Balancer    │◄──►│ (Multiple   │◄──►│ Cluster     │
│             │    │ Instances)  │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
       │                   │                   │
       ▼                   ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   SSL/TLS   │    │   Docker    │    │  Backup     │
│ Certificate │    │ Container   │    │  Strategy   │
└─────────────┘    └─────────────┘    └─────────────┘
```

### Docker Configuration
```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/healthy-recipe-platform-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
```

## Performance Considerations

### Database Optimization
- **Indexing**: Primary keys, foreign keys, and search columns
- **Connection Pooling**: HikariCP for connection management
- **Query Optimization**: JPA query optimization and N+1 problem prevention
- **Caching**: Consider Redis for frequently accessed data

### API Performance
- **Pagination**: Limit result sets with page-based navigation
- **Lazy Loading**: JPA lazy loading for associations
- **DTO Mapping**: Reduce data transfer with specific DTOs
- **Compression**: GZIP compression for API responses

### Security Best Practices
- **Password Hashing**: BCrypt with proper salt rounds
- **JWT Security**: Secure secret key and reasonable expiration times
- **Input Validation**: Comprehensive input validation and sanitization
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **HTTPS**: Force HTTPS in production
- **Rate Limiting**: Consider implementing API rate limiting

This architecture supports scalability, maintainability, and security while providing a comprehensive recipe recommendation platform.