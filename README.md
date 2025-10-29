# Healthy Recipe Recommendation and Rating Platform

## Overview
A comprehensive Spring Boot application that helps users discover healthy recipes tailored to their dietary preferences and health goals.

## Features

### Core Functionality
- **User Authentication & Authorization**: JWT-based secure authentication
- **Recipe Management**: Create, update, delete, and manage recipes
- **Rating & Review System**: Rate recipes and share feedback
- **Favorites System**: Save and track favorite recipes
- **Recommendation Engine**: Personalized recipe suggestions
- **Admin Panel**: Recipe approval and user management

### User Roles
- **USER**: Basic user with recipe browsing and creation capabilities
- **CHEF**: Enhanced user with recipe creation privileges
- **ADMIN**: Full system administration capabilities

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **Authentication**: JSON Web Tokens
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 17

## Database Schema

### Main Entities

#### Users
- User authentication and profile management
- Dietary preferences and health goals
- Role-based access control

#### Recipes
- Recipe details with nutrition information
- Categories and dietary tags
- Approval workflow for content moderation

#### Ratings
- User ratings and reviews for recipes
- Average rating calculation

#### Favorites
- User's favorite recipes tracking

#### Ingredients
- Recipe ingredients with quantities and units

## API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### Recipes
- `GET /api/recipes/public` - Get all approved recipes
- `POST /api/recipes` - Create new recipe
- `PUT /api/recipes/{id}` - Update recipe
- `DELETE /api/recipes/{id}` - Delete recipe
- `GET /api/recipes/search` - Search recipes
- `GET /api/recipes/{id}` - Get recipe by ID

### Ratings
- `POST /api/ratings/recipe/{recipeId}` - Rate a recipe
- `GET /api/ratings/recipe/{recipeId}` - Get recipe ratings
- `GET /api/ratings/my-ratings` - Get user's ratings

### Favorites
- `POST /api/favorites/recipe/{recipeId}` - Add to favorites
- `DELETE /api/favorites/recipe/{recipeId}` - Remove from favorites
- `GET /api/favorites/my-favorites` - Get user's favorites

### Recommendations
- `GET /api/recommendations/personalized` - Personalized recommendations
- `GET /api/recommendations/quick` - Quick recipes
- `GET /api/recommendations/healthy` - Healthy recipes

### Profile Management
- `GET /api/profile` - Get current user profile
- `PUT /api/profile` - Update profile
- `PUT /api/profile/change-password` - Change password

### Admin
- `GET /api/admin/recipes/pending` - Get pending recipes
- `PUT /api/admin/recipes/{id}/approve` - Approve recipe
- `PUT /api/admin/recipes/{id}/reject` - Reject recipe
- `GET /api/admin/users` - Get all users

## Getting Started

### Prerequisites
- Java 17 or later
- Maven 3.6 or later
- MySQL 8.0 or later

### Database Setup
1. Create a MySQL database named `healthy_recipe_db`
2. Update the database configuration in `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthy_recipe_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation
Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`

## Configuration

### JWT Configuration
Update the JWT secret and expiration in `application.properties`:
```properties
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000
```

### File Upload Configuration
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.dir=uploads/
```

## Security

The application implements JWT-based authentication with role-based access control:
- Public endpoints for recipe browsing
- Protected endpoints for user operations
- Admin-only endpoints for system management

## Testing

Run tests with:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.