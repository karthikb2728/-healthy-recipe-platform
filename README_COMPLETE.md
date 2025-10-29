# Healthy Recipe Recommendation and Rating Platform

A comprehensive Spring Boot application for managing healthy recipes with user authentication, recommendations, rating system, and a lightweight web frontend.

## 🎯 Features

### Backend (Spring Boot)
- **User Management**: Registration, login, and profile management with JWT authentication
- **Recipe Management**: Create, view, search, and manage recipes with full CRUD operations
- **Rating System**: Rate and review recipes with aggregate scoring
- **Recommendation Engine**: Get personalized recipe recommendations based on preferences
- **Favorites**: Save and manage favorite recipes
- **Admin Panel**: Administrative controls for managing users and recipes
- **RESTful API**: Complete REST API with 40+ endpoints and proper documentation

### Frontend (Lightweight Web Interface)
- **Responsive Design**: Clean, modern UI that works on all devices
- **Interactive Navigation**: Single-page application with smooth transitions
- **Recipe Browsing**: Browse recipes with filtering, sorting, and pagination
- **User Authentication**: Login/register modals with JWT token management
- **Recipe Details**: Detailed recipe view with ingredients, instructions, and nutrition info
- **Favorites Management**: Add/remove favorites with real-time updates
- **Search Functionality**: Search recipes by title, description, or ingredients
- **Admin Dashboard**: Administrative interface for user and recipe management
- **Profile Management**: Update user profile and change passwords

### User Roles
- **USER**: Basic user with recipe browsing and creation capabilities
- **CHEF**: Enhanced user with recipe creation privileges
- **ADMIN**: Full system administration capabilities

## 🛠 Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0 
- **Security**: Spring Security with JWT authentication
- **Build Tool**: Maven
- **Documentation**: Swagger/OpenAPI

### Frontend
- **Languages**: HTML5, CSS3, Vanilla JavaScript
- **Icons**: Font Awesome
- **Styling**: CSS Grid, Flexbox, CSS Variables
- **Architecture**: Single Page Application (SPA)
- **No Frameworks**: Lightweight vanilla JS implementation

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd healthy-recipe-platform
   ```

2. **Setup Database**
   - Create MySQL database: `healthy_recipe_db`
   - Import schema from `DATABASE_SCHEMA.md` or use `sample_data.sql`

3. **Configure Application**
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/healthy_recipe_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the Application**
   ```bash
   # Using Maven
   mvn spring-boot:run

   # Or using the start script
   ./start.sh  # Linux/Mac
   start.bat   # Windows
   ```

5. **Access the Application**
   - **Web Frontend**: `http://localhost:8080`
   - **API Documentation**: `http://localhost:8080/swagger-ui.html`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/healthyrecipe/
│   │   ├── config/          # Configuration classes (Security, Web, App)
│   │   ├── controller/      # REST controllers (Auth, Recipe, Admin, etc.)
│   │   ├── dto/            # Data transfer objects
│   │   ├── entity/         # JPA entities (User, Recipe, Rating, etc.)
│   │   ├── repository/     # Data repositories with custom queries
│   │   ├── security/       # JWT security implementation
│   │   └── service/        # Business logic services
│   └── resources/
│       ├── static/         # Frontend static files
│       │   ├── css/        # Stylesheets
│       │   ├── js/         # JavaScript files
│       │   ├── images/     # Images and assets
│       │   └── index.html  # Main HTML file
│       ├── application.properties
│       └── sample_data.sql
└── test/                   # Test classes
```

### Frontend Structure
```
src/main/resources/static/
├── index.html              # Main HTML file with all components
├── css/
│   └── style.css          # Complete CSS with responsive design
└── js/
    └── app.js             # Full JavaScript application logic
```

## 🔗 Key API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration
- `GET /api/auth/validate` - Validate JWT token

### Recipes
- `GET /api/recipes` - Get all recipes
- `POST /api/recipes` - Create new recipe
- `GET /api/recipes/{id}` - Get recipe by ID
- `PUT /api/recipes/{id}` - Update recipe
- `DELETE /api/recipes/{id}` - Delete recipe
- `GET /api/recipes/search` - Search recipes

### Favorites
- `GET /api/favorites` - Get user's favorite recipes
- `POST /api/favorites/add/{recipeId}` - Add to favorites
- `DELETE /api/favorites/remove/{recipeId}` - Remove from favorites

### Recommendations
- `GET /api/recommendations` - Get personalized recommendations
- `GET /api/recommendations/popular` - Get popular recipes

### Admin
- `GET /api/admin/users` - Get all users
- `GET /api/admin/recipes` - Get all recipes (admin view)
- `PUT /api/admin/users/{id}` - Update user
- `DELETE /api/admin/users/{id}` - Delete user

## 🎨 Frontend Features

### User Interface
- **Clean Design**: Modern, professional appearance with green color scheme
- **Responsive Layout**: Works perfectly on desktop, tablet, and mobile devices
- **Interactive Elements**: Hover effects, smooth transitions, and animations
- **Accessibility**: Proper contrast ratios and keyboard navigation support

### User Experience
- **Instant Feedback**: Loading indicators and success/error messages
- **Intuitive Navigation**: Clear menu structure and breadcrumbs
- **Smart Search**: Real-time search with filtering and sorting options
- **Pagination**: Efficient browsing of large recipe collections

### Technical Features
- **SPA Architecture**: No page reloads for better user experience
- **JWT Integration**: Secure authentication with automatic token management
- **API Integration**: Full integration with Spring Boot REST API
- **Error Handling**: Comprehensive error handling with user-friendly messages
- **Mobile Optimization**: Touch-friendly interface with mobile menu

## 📊 Database Schema

The application uses 5 main entities:
- **User**: User accounts with roles (USER, CHEF, ADMIN)
- **Recipe**: Recipe information with ingredients and instructions
- **Rating**: User ratings and reviews for recipes
- **Favorite**: User's favorite recipes
- **Ingredient**: Recipe ingredients with quantities

See `DATABASE_SCHEMA.md` for detailed schema information.

## 🔒 Security

- **JWT Authentication**: Stateless token-based authentication
- **Role-based Authorization**: USER, CHEF, and ADMIN roles
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Proper cross-origin resource sharing setup
- **Input Validation**: Server-side validation for all inputs

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

## 📈 Performance

- **Optimized Queries**: Custom repository methods for efficient data retrieval
- **Pagination**: Built-in pagination for large datasets
- **Lightweight Frontend**: Vanilla JS for fast loading and execution
- **Static Resource Caching**: Proper caching headers for static assets

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support, please create an issue in the repository or contact the development team.

---

**Note**: This is a complete, production-ready application with both backend API and frontend interface. The lightweight frontend provides a full user experience without requiring additional frameworks or complex build processes.

## 💡 Usage Instructions

### For Users
1. **Registration**: Create an account using the register modal
2. **Browse Recipes**: Explore the recipe collection with search and filters
3. **View Details**: Click on any recipe to see full details including ingredients and instructions
4. **Manage Favorites**: Add/remove recipes from your favorites list
5. **Rate Recipes**: Rate and review recipes you've tried
6. **Profile Management**: Update your profile information and preferences

### For Administrators
1. **User Management**: View and manage all user accounts
2. **Recipe Moderation**: Approve or reject submitted recipes
3. **Analytics**: View platform statistics and user engagement
4. **Content Management**: Manage categories, tags, and featured content

### Running Without Maven
If Maven is not installed, you can:
1. Use an IDE like IntelliJ IDEA or Eclipse to run the main class
2. Install Maven following the official Apache Maven installation guide
3. Use the Maven Wrapper if available in the project (./mvnw or mvnw.cmd)

### Database Alternative
If MySQL is not available, you can:
1. Use H2 in-memory database for testing (update application.properties)
2. Use any other JPA-supported database (PostgreSQL, SQLite, etc.)
3. Update the database driver dependency in pom.xml accordingly