package com.healthyrecipe.service;

import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private AuthService authService;
    
    public Page<Recipe> getPersonalizedRecommendations(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        
        // Get user's dietary preferences and fitness goals
        Set<User.DietaryPreference> dietaryPreferences = currentUser.getDietaryPreferences();
        User.FitnessGoal fitnessGoal = currentUser.getFitnessGoal();
        Integer calorieTarget = currentUser.getDailyCalorieTarget();
        Set<String> allergies = currentUser.getAllergies();
        
        // Build recommendation criteria
        List<String> dietaryTags = new ArrayList<>();
        
        if (dietaryPreferences != null) {
            dietaryTags.addAll(dietaryPreferences.stream()
                    .map(pref -> pref.name().toLowerCase().replace('_', '-'))
                    .collect(Collectors.toList()));
        }
        
        // If user has dietary preferences, find recipes with matching tags
        if (!dietaryTags.isEmpty()) {
            return recipeRepository.findApprovedByDietaryTags(dietaryTags, pageable);
        }
        
        // If user has calorie target, find recipes within range
        if (calorieTarget != null) {
            int minCalories = calorieTarget - 200; // 200 calorie buffer
            int maxCalories = calorieTarget + 200;
            return recipeRepository.findApprovedByCalorieRange(minCalories, maxCalories, pageable);
        }
        
        // Default to top-rated recipes
        return recipeRepository.findTopRatedApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getRecipesByFitnessGoal(User.FitnessGoal fitnessGoal, Pageable pageable) {
        List<String> recommendedTags = new ArrayList<>();
        
        switch (fitnessGoal) {
            case WEIGHT_LOSS:
                recommendedTags.add("low-calorie");
                recommendedTags.add("low-fat");
                recommendedTags.add("high-fiber");
                break;
            case WEIGHT_GAIN:
                recommendedTags.add("high-calorie");
                recommendedTags.add("high-protein");
                break;
            case MUSCLE_GAIN:
                recommendedTags.add("high-protein");
                recommendedTags.add("post-workout");
                break;
            case MAINTAIN_WEIGHT:
                recommendedTags.add("balanced");
                recommendedTags.add("moderate-calorie");
                break;
            case GENERAL_HEALTH:
                recommendedTags.add("healthy");
                recommendedTags.add("nutritious");
                recommendedTags.add("whole-foods");
                break;
        }
        
        if (!recommendedTags.isEmpty()) {
            return recipeRepository.findApprovedByDietaryTags(recommendedTags, pageable);
        }
        
        return recipeRepository.findTopRatedApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getQuickRecipes(Pageable pageable) {
        // Recipes that can be prepared in 30 minutes or less
        return recipeRepository.findApprovedByMaxTotalTime(30, pageable);
    }
    
    public Page<Recipe> getHealthyRecipes(Pageable pageable) {
        List<String> healthyTags = List.of("healthy", "nutritious", "low-sodium", "whole-foods", "clean-eating");
        return recipeRepository.findApprovedByDietaryTags(healthyTags, pageable);
    }
    
    public Page<Recipe> getBeginnerFriendlyRecipes(Pageable pageable) {
        return recipeRepository.findApprovedByDifficulty(Recipe.DifficultyLevel.EASY, pageable);
    }
    
    public Page<Recipe> getRecipesForAllergies(Set<String> allergies, Pageable pageable) {
        // This is a simplified implementation
        // In a real system, you'd want to exclude recipes containing allergens
        List<String> allergyFreeTags = new ArrayList<>();
        
        if (allergies.contains("gluten")) {
            allergyFreeTags.add("gluten-free");
        }
        if (allergies.contains("dairy")) {
            allergyFreeTags.add("dairy-free");
        }
        if (allergies.contains("nuts")) {
            allergyFreeTags.add("nut-free");
        }
        
        if (!allergyFreeTags.isEmpty()) {
            return recipeRepository.findApprovedByDietaryTags(allergyFreeTags, pageable);
        }
        
        return recipeRepository.findTopRatedApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getSimilarRecipes(Long recipeId, Pageable pageable) {
        // Find recipes with similar categories or tags
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        
        Set<Recipe.RecipeCategory> categories = recipe.getCategories();
        
        if (!categories.isEmpty()) {
            List<Recipe.RecipeCategory> categoryList = new ArrayList<>(categories);
            return recipeRepository.findApprovedByCategories(categoryList, pageable);
        }
        
        // Fallback to same difficulty level
        if (recipe.getDifficultyLevel() != null) {
            return recipeRepository.findApprovedByDifficulty(recipe.getDifficultyLevel(), pageable);
        }
        
        return recipeRepository.findLatestApprovedRecipes(pageable);
    }
}