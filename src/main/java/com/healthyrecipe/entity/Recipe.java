package com.healthyrecipe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String instructions;
    
    @NotNull
    @Positive
    private Integer preparationTime; // in minutes
    
    @NotNull
    @Positive
    private Integer cookingTime; // in minutes
    
    @NotNull
    @Positive
    private Integer servings;
    
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    private RecipeStatus status = RecipeStatus.PENDING;
    
    // Nutritional Information
    @Embedded
    private NutritionInfo nutritionInfo = new NutritionInfo();
    
    // Categories and Tags
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_categories", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "category")
    private Set<RecipeCategory> categories = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_dietary_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "dietary_tag")
    private Set<String> dietaryTags = new HashSet<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ingredient> ingredients = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Rating> ratings = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Favorite> favorites = new HashSet<>();
    
    // Computed fields
    @Transient
    private Double averageRating;
    
    @Transient
    private Integer totalRatings;
    
    @Transient
    private Integer totalTime;
    
    // Constructors
    public Recipe() {}
    
    public Recipe(String title, String description, String instructions, Integer preparationTime, 
                  Integer cookingTime, Integer servings, User author) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.author = author;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
    
    public Integer getCookingTime() { return cookingTime; }
    public void setCookingTime(Integer cookingTime) { this.cookingTime = cookingTime; }
    
    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }
    
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public RecipeStatus getStatus() { return status; }
    public void setStatus(RecipeStatus status) { this.status = status; }
    
    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public void setNutritionInfo(NutritionInfo nutritionInfo) { this.nutritionInfo = nutritionInfo; }
    
    public Set<RecipeCategory> getCategories() { return categories; }
    public void setCategories(Set<RecipeCategory> categories) { this.categories = categories; }
    
    public Set<String> getDietaryTags() { return dietaryTags; }
    public void setDietaryTags(Set<String> dietaryTags) { this.dietaryTags = dietaryTags; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public Set<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(Set<Ingredient> ingredients) { this.ingredients = ingredients; }
    
    public Set<Rating> getRatings() { return ratings; }
    public void setRatings(Set<Rating> ratings) { this.ratings = ratings; }
    
    public Set<Favorite> getFavorites() { return favorites; }
    public void setFavorites(Set<Favorite> favorites) { this.favorites = favorites; }
    
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    
    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }
    
    public Integer getTotalTime() { 
        return (preparationTime != null ? preparationTime : 0) + (cookingTime != null ? cookingTime : 0);
    }
    
    // Enums
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
    
    public enum RecipeStatus {
        PENDING, APPROVED, REJECTED
    }
    
    public enum RecipeCategory {
        BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, APPETIZER, BEVERAGE, SALAD, SOUP, MAIN_COURSE, SIDE_DISH
    }
    
    // Embedded class for nutrition information
    @Embeddable
    public static class NutritionInfo {
        private Integer calories;
        private Double protein; // in grams
        private Double carbohydrates; // in grams
        private Double fat; // in grams
        private Double fiber; // in grams
        private Double sugar; // in grams
        private Integer sodium; // in mg
        
        // Constructors
        public NutritionInfo() {}
        
        // Getters and Setters
        public Integer getCalories() { return calories; }
        public void setCalories(Integer calories) { this.calories = calories; }
        
        public Double getProtein() { return protein; }
        public void setProtein(Double protein) { this.protein = protein; }
        
        public Double getCarbohydrates() { return carbohydrates; }
        public void setCarbohydrates(Double carbohydrates) { this.carbohydrates = carbohydrates; }
        
        public Double getFat() { return fat; }
        public void setFat(Double fat) { this.fat = fat; }
        
        public Double getFiber() { return fiber; }
        public void setFiber(Double fiber) { this.fiber = fiber; }
        
        public Double getSugar() { return sugar; }
        public void setSugar(Double sugar) { this.sugar = sugar; }
        
        public Integer getSodium() { return sodium; }
        public void setSodium(Integer sodium) { this.sodium = sodium; }
    }
}