package com.healthyrecipe.dto.recipe;

import com.healthyrecipe.entity.Recipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public class RecipeCreateRequest {
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @NotBlank
    @Size(max = 1000)
    private String description;
    
    @NotBlank
    @Size(max = 5000)
    private String instructions;
    
    @NotNull
    @Positive
    private Integer preparationTime;
    
    @NotNull
    @Positive
    private Integer cookingTime;
    
    @NotNull
    @Positive
    private Integer servings;
    
    private Recipe.DifficultyLevel difficultyLevel;
    
    private String imageUrl;
    
    private Set<Recipe.RecipeCategory> categories;
    
    private Set<String> dietaryTags;
    
    // Nutrition Information
    private Integer calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;
    private Double sugar;
    private Integer sodium;
    
    // Ingredients
    private List<IngredientDto> ingredients;
    
    public RecipeCreateRequest() {}
    
    // Getters and Setters
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
    
    public Recipe.DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(Recipe.DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Set<Recipe.RecipeCategory> getCategories() { return categories; }
    public void setCategories(Set<Recipe.RecipeCategory> categories) { this.categories = categories; }
    
    public Set<String> getDietaryTags() { return dietaryTags; }
    public void setDietaryTags(Set<String> dietaryTags) { this.dietaryTags = dietaryTags; }
    
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
    
    public List<IngredientDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDto> ingredients) { this.ingredients = ingredients; }
    
    // Inner class for ingredient data
    public static class IngredientDto {
        @NotBlank
        private String name;
        
        @NotNull
        @Positive
        private Double quantity;
        
        @NotBlank
        private String unit;
        
        private String notes;
        
        public IngredientDto() {}
        
        public IngredientDto(String name, Double quantity, String unit, String notes) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
            this.notes = notes;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}