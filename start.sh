#!/bin/bash

echo "Starting Healthy Recipe Platform Setup..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 17 or later."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven 3.6 or later."
    exit 1
fi

# Check if MySQL is running
if ! command -v mysql &> /dev/null; then
    echo "MySQL is not installed. Please install MySQL 8.0 or later."
    echo "Or use Docker Compose to run the application with MySQL."
    exit 1
fi

echo "Setting up database..."
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS healthy_recipe_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

echo "Building application..."
mvn clean package -DskipTests

echo "Starting application..."
java -jar target/healthy-recipe-platform-1.0.0.jar

echo "Application started successfully!"
echo "Access the application at: http://localhost:8080"
echo "API Documentation: http://localhost:8080/swagger-ui.html"