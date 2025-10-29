# 🍽️ Healthy Recipe Recommendation and Rating Platform

A comprehensive full-stack web application built with **Spring Boot** and **React-like frontend** that provides personalized recipe recommendations, nutritional tracking, and role-based access control for users, chefs, and administrators.

## 🌟 Features

### 🔐 **Authentication & Authorization**
- **JWT-based Authentication** with secure token management
- **Role-based Access Control** (USER, CHEF, ADMIN)
- **Secure Registration & Login** with input validation
- **Session Management** with automatic token refresh

### 👥 **User Management**
- **User Profiles** with dietary preferences and fitness goals
- **Allergy Management** for personalized recommendations
- **Activity Tracking** and calorie monitoring
- **Profile Customization** with bio and images

### 🍳 **Recipe Management**
- **Recipe Creation** with detailed ingredients and instructions
- **Nutritional Information** tracking (calories, macros, fiber, etc.)
- **Category Classification** (Breakfast, Lunch, Dinner, etc.)
- **Difficulty Levels** (Easy, Medium, Hard)
- **Recipe Status Management** (Pending, Approved, Rejected)

### ⭐ **Rating & Review System**
- **5-star Rating System** with detailed comments
- **User Reviews** and feedback collection
- **Rating Analytics** for recipe popularity
- **Review Moderation** capabilities

### 💝 **Favorites & Recommendations**
- **Personal Favorites** list management
- **AI-powered Recommendations** based on preferences
- **Dietary Filtering** (Vegetarian, Vegan, Keto, etc.)
- **Calorie-based Suggestions** aligned with fitness goals

### 👨‍💼 **Admin Panel**
- **User Management** - View, activate, suspend users
- **Recipe Moderation** - Approve/reject chef submissions
- **System Analytics** - Usage statistics and trends
- **Content Management** - Bulk operations and data export

### 🎨 **Modern UI/UX**
- **Dark Theme** with excellent readability
- **Responsive Design** for all screen sizes
- **Role-based Navigation** with context-aware menus
- **Real-time Updates** and notifications
- **Accessibility Features** for inclusive design

## 🛠️ Technology Stack

### Backend
- **Java 25** - Latest LTS version
- **Spring Boot 3.2.0** - Enterprise framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database abstraction
- **Hibernate** - ORM framework
- **H2 Database** - In-memory database for development
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Maven 3.9.5** - Build automation

### Frontend
- **Vanilla JavaScript** - Modern ES6+ features
- **HTML5 & CSS3** - Semantic markup and styling
- **Responsive Design** - Mobile-first approach
- **Dark Theme** - Modern UI aesthetics
- **Fetch API** - RESTful service consumption

### Development Tools
- **VS Code** - Primary IDE
- **Git** - Version control
- **GitHub** - Repository hosting
- **Maven** - Dependency management
- **H2 Console** - Database management

## 🚀 Quick Start

### Prerequisites
- **Java 25** or higher
- **Maven 3.9.5** or higher
- **Git** for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SECE-24-28/end-semester-project-karthikb2728.git
   cd end-semester-project-karthikb2728
   ```

2. **Run the application**
   ```bash
   # Windows
   .\apache-maven-3.9.5\bin\mvn.cmd spring-boot:run
   
   # Linux/Mac
   ./apache-maven-3.9.5/bin/mvn spring-boot:run
   ```

3. **Access the application**
   - **Frontend**: http://localhost:8080/api
   - **H2 Database Console**: http://localhost:8080/api/h2-console
   - **API Documentation**: Available in API_DOCUMENTATION.md

### Default Credentials
Create accounts through the registration form with these roles:
- **USER**: Browse and rate recipes
- **CHEF**: Create and manage recipes
- **ADMIN**: Full system administration

## 📁 Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/healthyrecipe/
│   │   │   ├── controller/          # REST API endpoints
│   │   │   ├── service/             # Business logic layer
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── security/            # Security configuration
│   │   │   └── config/              # Application configuration
│   │   └── resources/
│   │       ├── static/              # Frontend assets
│   │       │   ├── css/style.css    # Dark theme styling
│   │       │   ├── js/app.js        # Frontend logic
│   │       │   └── index.html       # Main HTML page
│   │       └── application.properties
│   └── test/                        # Unit and integration tests
├── target/                          # Compiled classes
├── apache-maven-3.9.5/             # Maven installation
├── docs/                            # Documentation files
└── pom.xml                          # Maven configuration
```

## 🔧 Configuration

### Database Configuration
```properties
# H2 Database (Development)
spring.datasource.url=jdbc:h2:mem:healthy_recipe_db
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### JWT Configuration
```properties
# JWT Settings
healthyrecipe.jwt.secret=healthyRecipeSecretKey2024!@#$%^&*()
healthyrecipe.jwt.expiration=86400000
```

### Application Settings
```properties
# Server Configuration
server.servlet.context-path=/api
server.port=8080
spring.application.name=healthy-recipe-platform
```

## 📊 Database Schema

The application uses the following main entities:

- **Users** - User accounts with roles and preferences
- **Recipes** - Recipe information with nutritional data
- **Ratings** - User ratings and reviews for recipes
- **Favorites** - User's favorite recipes
- **Ingredients** - Recipe ingredients with quantities

For detailed schema information, see `DATABASE_SCHEMA.md`.

## 🔗 API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login

### Recipes
- `GET /api/recipes` - Get all recipes
- `POST /api/recipes` - Create new recipe (CHEF/ADMIN)
- `GET /api/recipes/{id}` - Get recipe by ID
- `PUT /api/recipes/{id}` - Update recipe
- `DELETE /api/recipes/{id}` - Delete recipe

### User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/admin/users` - Get all users (ADMIN only)

For complete API documentation, see `API_DOCUMENTATION.md`.

## 🧪 Testing

Run the test suite:
```bash
.\apache-maven-3.9.5\bin\mvn.cmd test
```

## 🚢 Deployment

### Production Build
```bash
.\apache-maven-3.9.5\bin\mvn.cmd clean package
java -jar target/healthy-recipe-platform-1.0.0.jar
```

### Environment Variables
- `JWT_SECRET` - JWT signing secret
- `DATABASE_URL` - Production database URL
- `SPRING_PROFILES_ACTIVE` - Active profile (dev/prod)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Karthik B**
- GitHub: [@karthikb2728](https://github.com/karthikb2728)
- Email: karthikb2728@gmail.com

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- H2 Database for lightweight development database
- JWT.io for token standards
- Modern web development practices and patterns

## 📈 Project Status

- ✅ **Backend Development**: Complete with full REST API
- ✅ **Frontend Development**: Complete with dark theme UI
- ✅ **Authentication System**: JWT-based with role management
- ✅ **Database Design**: Comprehensive schema with relationships
- ✅ **Security Implementation**: Role-based access control
- ✅ **Admin Panel**: Full administrative capabilities
- ✅ **Testing**: Unit tests for core functionality
- 🔄 **Documentation**: Comprehensive guides and API docs
- 🔄 **Deployment**: Production-ready configuration

---

### 🌟 Star this repository if you found it helpful!

For questions, issues, or contributions, please use the GitHub issue tracker.