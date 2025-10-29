-- Sample data for Healthy Recipe Platform

-- Insert sample users
INSERT INTO users (username, email, password, first_name, last_name, role, status, bio, daily_calorie_target, fitness_goal, created_at, updated_at) VALUES
('admin', 'admin@healthyrecipe.com', '$2a$10$X5wFWGOK5Z0XQ5jTKr0rSuXO6xJwGjRkQKjOXGQnJyI7vWLxKqaJq', 'Admin', 'User', 'ADMIN', 'ACTIVE', 'System Administrator', 2000, 'GENERAL_HEALTH', NOW(), NOW()),
('chef_mario', 'mario@healthyrecipe.com', '$2a$10$X5wFWGOK5Z0XQ5jTKr0rSuXO6xJwGjRkQKjOXGQnJyI7vWLxKqaJq', 'Mario', 'Rossi', 'CHEF', 'ACTIVE', 'Professional chef specializing in Mediterranean cuisine', 2200, 'MAINTAIN_WEIGHT', NOW(), NOW()),
('healthy_jane', 'jane@example.com', '$2a$10$X5wFWGOK5Z0XQ5jTKr0rSuXO6xJwGjRkQKjOXGQnJyI7vWLxKqaJq', 'Jane', 'Smith', 'USER', 'ACTIVE', 'Fitness enthusiast and healthy eating advocate', 1800, 'WEIGHT_LOSS', NOW(), NOW()),
('fitness_john', 'john@example.com', '$2a$10$X5wFWGOK5Z0XQ5jTKr0rSuXO6xJwGjRkQKjOXGQnJyI7vWLxKqaJq', 'John', 'Doe', 'USER', 'ACTIVE', 'Bodybuilder focusing on high-protein meals', 2500, 'MUSCLE_GAIN', NOW(), NOW());

-- Insert dietary preferences
INSERT INTO user_dietary_preferences (user_id, dietary_preference) VALUES
(2, 'MEDITERRANEAN'),
(3, 'VEGETARIAN'),
(3, 'LOW_FAT'),
(4, 'HIGH_PROTEIN');

-- Insert user allergies
INSERT INTO user_allergies (user_id, allergy) VALUES
(3, 'nuts'),
(4, 'dairy');

-- Insert sample recipes
INSERT INTO recipes (title, description, instructions, preparation_time, cooking_time, servings, difficulty_level, status, author_id, calories, protein, carbohydrates, fat, fiber, sugar, sodium, created_at, updated_at) VALUES
('Mediterranean Quinoa Salad', 'A refreshing and nutritious quinoa salad with Mediterranean flavors', 
'1. Rinse quinoa and cook according to package instructions.\n2. Let quinoa cool completely.\n3. Dice tomatoes, cucumber, and red onion.\n4. Chop fresh herbs.\n5. Whisk together olive oil, lemon juice, salt, and pepper.\n6. Combine all ingredients and toss with dressing.\n7. Chill for at least 30 minutes before serving.',
15, 20, 4, 'EASY', 'APPROVED', 2, 320, 12.5, 45.2, 8.1, 6.3, 5.2, 380, NOW(), NOW()),

('Protein-Packed Chicken Bowl', 'High-protein chicken bowl with quinoa and vegetables',
'1. Season chicken breast with herbs and spices.\n2. Grill chicken until cooked through.\n3. Cook quinoa according to package instructions.\n4. Steam broccoli and carrots.\n5. Slice chicken and arrange over quinoa.\n6. Add steamed vegetables.\n7. Drizzle with tahini sauce.',
10, 25, 2, 'MEDIUM', 'APPROVED', 4, 520, 45.8, 35.6, 18.2, 8.1, 3.2, 420, NOW(), NOW()),

('Vegan Buddha Bowl', 'Colorful and nutritious vegan bowl with chickpeas and tahini dressing',
'1. Roast chickpeas with spices until crispy.\n2. Massage kale with lemon juice.\n3. Cook quinoa.\n4. Prepare tahini dressing.\n5. Arrange all components in bowl.\n6. Drizzle with dressing and serve.',
20, 30, 3, 'EASY', 'APPROVED', 3, 420, 18.3, 52.1, 15.6, 12.4, 6.8, 350, NOW(), NOW()),

('Low-Carb Zucchini Noodles', 'Healthy zucchini noodles with turkey meatballs',
'1. Spiralize zucchini into noodles.\n2. Make turkey meatballs with herbs.\n3. Cook meatballs until browned.\n4. Sauté zucchini noodles briefly.\n5. Combine with meatballs and marinara sauce.\n6. Garnish with fresh basil.',
15, 20, 4, 'MEDIUM', 'PENDING', 3, 280, 28.5, 12.3, 16.2, 4.1, 8.9, 520, NOW(), NOW());

-- Insert recipe categories
INSERT INTO recipe_categories (recipe_id, category) VALUES
(1, 'SALAD'),
(1, 'LUNCH'),
(2, 'MAIN_COURSE'),
(2, 'DINNER'),
(3, 'MAIN_COURSE'),
(3, 'LUNCH'),
(4, 'MAIN_COURSE'),
(4, 'DINNER');

-- Insert recipe dietary tags
INSERT INTO recipe_dietary_tags (recipe_id, dietary_tag) VALUES
(1, 'vegetarian'),
(1, 'gluten-free'),
(1, 'mediterranean'),
(2, 'high-protein'),
(2, 'gluten-free'),
(3, 'vegan'),
(3, 'high-fiber'),
(3, 'gluten-free'),
(4, 'low-carb'),
(4, 'high-protein');

-- Insert ingredients
INSERT INTO ingredients (recipe_id, name, quantity, unit, notes) VALUES
-- Mediterranean Quinoa Salad
(1, 'Quinoa', 1.0, 'cup', 'rinsed'),
(1, 'Cherry tomatoes', 1.5, 'cups', 'halved'),
(1, 'Cucumber', 1.0, 'large', 'diced'),
(1, 'Red onion', 0.25, 'cup', 'finely diced'),
(1, 'Fresh parsley', 0.25, 'cup', 'chopped'),
(1, 'Fresh mint', 2.0, 'tbsp', 'chopped'),
(1, 'Olive oil', 3.0, 'tbsp', 'extra virgin'),
(1, 'Lemon juice', 2.0, 'tbsp', 'fresh'),
(1, 'Salt', 0.5, 'tsp', ''),
(1, 'Black pepper', 0.25, 'tsp', 'freshly ground'),

-- Protein-Packed Chicken Bowl
(2, 'Chicken breast', 8.0, 'oz', 'boneless, skinless'),
(2, 'Quinoa', 0.75, 'cup', ''),
(2, 'Broccoli', 2.0, 'cups', 'florets'),
(2, 'Carrots', 1.0, 'large', 'sliced'),
(2, 'Tahini', 2.0, 'tbsp', ''),
(2, 'Lemon juice', 1.0, 'tbsp', ''),
(2, 'Garlic', 1.0, 'clove', 'minced'),
(2, 'Olive oil', 1.0, 'tbsp', ''),

-- Vegan Buddha Bowl
(3, 'Chickpeas', 1.0, 'can', 'drained and rinsed'),
(3, 'Kale', 4.0, 'cups', 'chopped'),
(3, 'Quinoa', 0.5, 'cup', ''),
(3, 'Avocado', 1.0, 'medium', 'sliced'),
(3, 'Tahini', 3.0, 'tbsp', ''),
(3, 'Lemon juice', 2.0, 'tbsp', ''),
(3, 'Maple syrup', 1.0, 'tsp', ''),

-- Low-Carb Zucchini Noodles
(4, 'Zucchini', 4.0, 'medium', ''),
(4, 'Ground turkey', 1.0, 'lb', 'lean'),
(4, 'Egg', 1.0, 'large', ''),
(4, 'Almond flour', 0.25, 'cup', ''),
(4, 'Marinara sauce', 1.0, 'cup', 'sugar-free'),
(4, 'Fresh basil', 0.25, 'cup', 'chopped');

-- Insert sample ratings
INSERT INTO ratings (user_id, recipe_id, rating, comment, created_at, updated_at) VALUES
(3, 1, 5, 'Absolutely delicious and so healthy! Perfect for meal prep.', NOW(), NOW()),
(4, 1, 4, 'Great flavors, but I added some feta cheese for extra protein.', NOW(), NOW()),
(1, 2, 5, 'Perfect post-workout meal. Great protein content!', NOW(), NOW()),
(3, 2, 4, 'Tasty and filling, though a bit high in calories for my goals.', NOW(), NOW()),
(2, 3, 5, 'Beautiful presentation and amazing taste. Love the tahini dressing!', NOW(), NOW()),
(4, 3, 3, 'Good recipe, but needs more protein for my fitness goals.', NOW(), NOW());

-- Insert sample favorites
INSERT INTO favorites (user_id, recipe_id, created_at) VALUES
(3, 1, NOW()),
(3, 3, NOW()),
(4, 1, NOW()),
(4, 2, NOW()),
(2, 3, NOW()),
(1, 2, NOW());

-- Note: Password for all sample users is 'password123'