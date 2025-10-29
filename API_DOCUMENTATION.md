# API Documentation

## Authentication Endpoints

### POST /api/auth/signin
Login with username and password.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "accessToken": "string",
  "tokenType": "Bearer",
  "id": 1,
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "roles": ["ROLE_USER"]
}
```

### POST /api/auth/signup
Register a new user account.

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "bio": "string",
  "role": ["user"],
  "dietaryPreferences": ["VEGETARIAN", "GLUTEN_FREE"],
  "allergies": ["nuts", "dairy"],
  "fitnessGoal": "WEIGHT_LOSS",
  "dailyCalorieTarget": 2000
}
```

## Recipe Endpoints

### GET /api/recipes/public
Get all approved recipes with pagination.

**Parameters:**
- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: "createdAt")
- `sortDir` (default: "desc")

### POST /api/recipes
Create a new recipe. Requires authentication.

**Request Body:**
```json
{
  "title": "Healthy Quinoa Salad",
  "description": "A nutritious and delicious quinoa salad",
  "instructions": "1. Cook quinoa...",
  "preparationTime": 15,
  "cookingTime": 20,
  "servings": 4,
  "difficultyLevel": "EASY",
  "imageUrl": "https://example.com/image.jpg",
  "categories": ["SALAD", "LUNCH"],
  "dietaryTags": ["vegetarian", "gluten-free"],
  "calories": 320,
  "protein": 12.5,
  "carbohydrates": 45.2,
  "fat": 8.1,
  "fiber": 6.3,
  "sugar": 5.2,
  "sodium": 380,
  "ingredients": [
    {
      "name": "Quinoa",
      "quantity": 1.0,
      "unit": "cup",
      "notes": "rinsed"
    }
  ]
}
```

### GET /api/recipes/search
Search recipes by keyword.

**Parameters:**
- `keyword` (required)
- `page` (default: 0)
- `size` (default: 10)

### GET /api/recipes/category
Filter recipes by categories.

**Parameters:**
- `categories` (required, array)
- `page` (default: 0)
- `size` (default: 10)

### GET /api/recipes/calories
Filter recipes by calorie range.

**Parameters:**
- `minCalories` (required)
- `maxCalories` (required)
- `page` (default: 0)
- `size` (default: 10)

## Rating Endpoints

### POST /api/ratings/recipe/{recipeId}
Rate a recipe. Requires authentication.

**Parameters:**
- `rating` (1-5, required)
- `comment` (optional)

### GET /api/ratings/recipe/{recipeId}
Get all ratings for a recipe.

**Parameters:**
- `page` (default: 0)
- `size` (default: 10)

### GET /api/ratings/recipe/{recipeId}/average
Get average rating and total count for a recipe.

**Response:**
```json
{
  "averageRating": 4.2,
  "totalRatings": 15
}
```

## Favorites Endpoints

### POST /api/favorites/recipe/{recipeId}
Add recipe to favorites. Requires authentication.

### DELETE /api/favorites/recipe/{recipeId}
Remove recipe from favorites. Requires authentication.

### GET /api/favorites/my-favorites
Get user's favorite recipes. Requires authentication.

**Parameters:**
- `page` (default: 0)
- `size` (default: 10)

## Recommendation Endpoints

### GET /api/recommendations/personalized
Get personalized recipe recommendations based on user preferences. Requires authentication.

### GET /api/recommendations/fitness-goal
Get recipes by fitness goal.

**Parameters:**
- `fitnessGoal` (WEIGHT_LOSS, WEIGHT_GAIN, MUSCLE_GAIN, MAINTAIN_WEIGHT, GENERAL_HEALTH)

### GET /api/recommendations/quick
Get quick recipes (≤30 minutes total time).

### GET /api/recommendations/healthy
Get healthy recipes with nutritious tags.

### GET /api/recommendations/beginner
Get beginner-friendly recipes (easy difficulty).

## Profile Endpoints

### GET /api/profile
Get current user's profile. Requires authentication.

### PUT /api/profile
Update user profile. Requires authentication.

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string",
  "bio": "string",
  "profileImageUrl": "string",
  "dietaryPreferences": ["VEGETARIAN"],
  "allergies": ["nuts"],
  "fitnessGoal": "WEIGHT_LOSS",
  "dailyCalorieTarget": 1800
}
```

### PUT /api/profile/change-password
Change user password. Requires authentication.

**Request Body:**
```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```

## Admin Endpoints

### GET /api/admin/recipes/pending
Get all pending recipes for approval. Requires ADMIN role.

### PUT /api/admin/recipes/{id}/approve
Approve a pending recipe. Requires ADMIN role.

### PUT /api/admin/recipes/{id}/reject
Reject a pending recipe. Requires ADMIN role.

### GET /api/admin/users
Get all users. Requires ADMIN role.

### PUT /api/admin/users/{id}/status
Update user account status. Requires ADMIN role.

**Parameters:**
- `status` (ACTIVE, INACTIVE, SUSPENDED)

### PUT /api/admin/users/{id}/role
Update user role. Requires ADMIN role.

**Parameters:**
- `role` (USER, CHEF, ADMIN)

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "error": "Error message describing what went wrong"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized"
}
```

### 403 Forbidden
```json
{
  "error": "Access denied"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

## Authentication

Include JWT token in the Authorization header for protected endpoints:

```
Authorization: Bearer <your-jwt-token>
```

## Data Types and Enums

### User.Role
- USER
- CHEF
- ADMIN

### User.AccountStatus
- ACTIVE
- INACTIVE
- SUSPENDED

### User.DietaryPreference
- VEGETARIAN
- VEGAN
- GLUTEN_FREE
- KETO
- PALEO
- LOW_CARB
- LOW_FAT
- DAIRY_FREE
- MEDITERRANEAN

### User.FitnessGoal
- WEIGHT_LOSS
- WEIGHT_GAIN
- MUSCLE_GAIN
- MAINTAIN_WEIGHT
- GENERAL_HEALTH

### Recipe.DifficultyLevel
- EASY
- MEDIUM
- HARD

### Recipe.RecipeCategory
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

### Recipe.RecipeStatus
- PENDING
- APPROVED
- REJECTED