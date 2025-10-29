# Database Schema Documentation

## Entity Relationship Diagram

```
Users ||--o{ Recipes : creates
Users ||--o{ Ratings : gives
Users ||--o{ Favorites : has
Recipes ||--o{ Ingredients : contains
Recipes ||--o{ Ratings : receives
Recipes ||--o{ Favorites : is_favorited_by
```

## Table Structures

### users
Primary table for user authentication and profile information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(50) | NOT NULL, UNIQUE | Unique username |
| email | VARCHAR(100) | NOT NULL, UNIQUE | User email address |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| first_name | VARCHAR(50) | NOT NULL | User's first name |
| last_name | VARCHAR(50) | NOT NULL | User's last name |
| phone_number | VARCHAR(15) | | User's phone number |
| bio | TEXT | | User biography |
| profile_image_url | VARCHAR(255) | | Profile image URL |
| role | ENUM | NOT NULL, DEFAULT 'USER' | User role (USER, CHEF, ADMIN) |
| status | ENUM | NOT NULL, DEFAULT 'ACTIVE' | Account status |
| fitness_goal | ENUM | | User's fitness goal |
| daily_calorie_target | INT | | Daily calorie target |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Account creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Last update time |

### user_dietary_preferences
Junction table for user dietary preferences.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | BIGINT | FOREIGN KEY | Reference to users.id |
| dietary_preference | ENUM | NOT NULL | Dietary preference type |

### user_allergies
Table for user allergies.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | BIGINT | FOREIGN KEY | Reference to users.id |
| allergy | VARCHAR(100) | NOT NULL | Allergy description |

### recipes
Main table for recipe information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique recipe identifier |
| title | VARCHAR(100) | NOT NULL | Recipe title |
| description | TEXT | NOT NULL | Recipe description |
| instructions | TEXT | NOT NULL | Cooking instructions |
| preparation_time | INT | NOT NULL | Prep time in minutes |
| cooking_time | INT | NOT NULL | Cooking time in minutes |
| servings | INT | NOT NULL | Number of servings |
| difficulty_level | ENUM | | Difficulty level |
| image_url | VARCHAR(255) | | Recipe image URL |
| status | ENUM | DEFAULT 'PENDING' | Recipe approval status |
| author_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users.id |
| calories | INT | | Nutritional: calories |
| protein | DECIMAL(5,2) | | Nutritional: protein in grams |
| carbohydrates | DECIMAL(5,2) | | Nutritional: carbs in grams |
| fat | DECIMAL(5,2) | | Nutritional: fat in grams |
| fiber | DECIMAL(5,2) | | Nutritional: fiber in grams |
| sugar | DECIMAL(5,2) | | Nutritional: sugar in grams |
| sodium | INT | | Nutritional: sodium in mg |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Recipe creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Last update time |

### recipe_categories
Junction table for recipe categories.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| recipe_id | BIGINT | FOREIGN KEY | Reference to recipes.id |
| category | ENUM | NOT NULL | Recipe category |

### recipe_dietary_tags
Table for recipe dietary tags.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| recipe_id | BIGINT | FOREIGN KEY | Reference to recipes.id |
| dietary_tag | VARCHAR(50) | NOT NULL | Dietary tag |

### ingredients
Table for recipe ingredients.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique ingredient identifier |
| recipe_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to recipes.id |
| name | VARCHAR(100) | NOT NULL | Ingredient name |
| quantity | DECIMAL(8,2) | NOT NULL | Ingredient quantity |
| unit | VARCHAR(20) | NOT NULL | Unit of measurement |
| notes | VARCHAR(255) | | Preparation notes |

### ratings
Table for recipe ratings and reviews.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique rating identifier |
| user_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users.id |
| recipe_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to recipes.id |
| rating | INT | NOT NULL, CHECK (1-5) | Rating value (1-5 stars) |
| comment | TEXT | | Review comment |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Rating creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Last update time |

**Unique Constraint:** (user_id, recipe_id) - One rating per user per recipe

### favorites
Table for user's favorite recipes.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique favorite identifier |
| user_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users.id |
| recipe_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to recipes.id |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Favorite creation time |

**Unique Constraint:** (user_id, recipe_id) - One favorite per user per recipe

## Indexes

### Primary Indexes
- All tables have primary key indexes on their `id` columns

### Foreign Key Indexes
- `user_dietary_preferences(user_id)`
- `user_allergies(user_id)`
- `recipes(author_id)`
- `recipe_categories(recipe_id)`
- `recipe_dietary_tags(recipe_id)`
- `ingredients(recipe_id)`
- `ratings(user_id, recipe_id)`
- `favorites(user_id, recipe_id)`

### Additional Indexes for Performance
- `users(username)` - Unique index for login
- `users(email)` - Unique index for email lookup
- `recipes(status)` - Index for filtering by approval status
- `recipes(created_at)` - Index for sorting by creation date
- `ratings(recipe_id)` - Index for recipe rating queries
- `favorites(recipe_id)` - Index for favorite count queries

## Enum Values

### User Role
- USER
- CHEF
- ADMIN

### Account Status
- ACTIVE
- INACTIVE
- SUSPENDED

### Dietary Preferences
- VEGETARIAN
- VEGAN
- GLUTEN_FREE
- KETO
- PALEO
- LOW_CARB
- LOW_FAT
- DAIRY_FREE
- MEDITERRANEAN

### Fitness Goals
- WEIGHT_LOSS
- WEIGHT_GAIN
- MUSCLE_GAIN
- MAINTAIN_WEIGHT
- GENERAL_HEALTH

### Recipe Difficulty
- EASY
- MEDIUM
- HARD

### Recipe Categories
- BREAKFAST
- LUNCH
- DINNER
- SNACK
- DESSERT
- APPETIZER
- BEVERAGE
- SALAD
- SOUP
- MAIN_COURSE
- SIDE_DISH

### Recipe Status
- PENDING
- APPROVED
- REJECTED

## Sample Queries

### Get Top Rated Recipes
```sql
SELECT r.*, AVG(rt.rating) as avg_rating, COUNT(rt.id) as total_ratings
FROM recipes r
LEFT JOIN ratings rt ON r.id = rt.recipe_id
WHERE r.status = 'APPROVED'
GROUP BY r.id
ORDER BY avg_rating DESC, total_ratings DESC
LIMIT 10;
```

### Get Recipes by Dietary Preferences
```sql
SELECT DISTINCT r.*
FROM recipes r
JOIN recipe_dietary_tags rdt ON r.id = rdt.recipe_id
WHERE rdt.dietary_tag IN ('vegetarian', 'gluten-free')
AND r.status = 'APPROVED';
```

### Get User's Recommended Recipes
```sql
SELECT DISTINCT r.*
FROM recipes r
JOIN recipe_dietary_tags rdt ON r.id = rdt.recipe_id
JOIN user_dietary_preferences udp ON udp.dietary_preference = rdt.dietary_tag
WHERE udp.user_id = ? 
AND r.status = 'APPROVED'
AND r.author_id != ?;
```

### Get Recipe with Ingredients
```sql
SELECT r.*, i.name, i.quantity, i.unit, i.notes
FROM recipes r
LEFT JOIN ingredients i ON r.id = i.recipe_id
WHERE r.id = ?;
```

## Database Migration Scripts

The application uses JPA/Hibernate DDL auto-generation. For production, consider using proper migration tools like Flyway or Liquibase.

### Initial Setup Script
```sql
CREATE DATABASE healthy_recipe_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'recipe_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON healthy_recipe_db.* TO 'recipe_user'@'localhost';
FLUSH PRIVILEGES;
```