package com.healthyrecipe.service;

import com.healthyrecipe.dto.recipe.RecipeCreateRequest;
import com.healthyrecipe.entity.Ingredient;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.IngredientRepository;
import com.healthyrecipe.repository.RecipeRepository;
import com.healthyrecipe.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeService {
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private AuthService authService;
    
    public Recipe createRecipe(RecipeCreateRequest request) {
        User currentUser = authService.getCurrentUser();
        
        Recipe recipe = new Recipe();
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setPreparationTime(request.getPreparationTime());
        recipe.setCookingTime(request.getCookingTime());
        recipe.setServings(request.getServings());
        recipe.setDifficultyLevel(request.getDifficultyLevel());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setCategories(request.getCategories());
        recipe.setDietaryTags(request.getDietaryTags());
        recipe.setAuthor(currentUser);
        
        // Set nutrition info
        Recipe.NutritionInfo nutritionInfo = new Recipe.NutritionInfo();
        nutritionInfo.setCalories(request.getCalories());
        nutritionInfo.setProtein(request.getProtein());
        nutritionInfo.setCarbohydrates(request.getCarbohydrates());
        nutritionInfo.setFat(request.getFat());
        nutritionInfo.setFiber(request.getFiber());
        nutritionInfo.setSugar(request.getSugar());
        nutritionInfo.setSodium(request.getSodium());
        recipe.setNutritionInfo(nutritionInfo);
        
        // Set status based on user role
        if (currentUser.getRole() == User.Role.ADMIN) {
            recipe.setStatus(Recipe.RecipeStatus.APPROVED);
        } else {
            recipe.setStatus(Recipe.RecipeStatus.PENDING);
        }
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // Create ingredients
        if (request.getIngredients() != null) {
            for (RecipeCreateRequest.IngredientDto ingredientDto : request.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDto.getName());
                ingredient.setQuantity(ingredientDto.getQuantity());
                ingredient.setUnit(ingredientDto.getUnit());
                ingredient.setNotes(ingredientDto.getNotes());
                ingredient.setRecipe(savedRecipe);
                ingredientRepository.save(ingredient);
            }
        }
        
        return savedRecipe;
    }
    
    public Recipe updateRecipe(Long id, RecipeCreateRequest request) {
        Recipe recipe = getRecipeById(id);
        User currentUser = authService.getCurrentUser();
        
        // Check if user can update this recipe
        if (!recipe.getAuthor().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("You don't have permission to update this recipe");
        }
        
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setPreparationTime(request.getPreparationTime());
        recipe.setCookingTime(request.getCookingTime());
        recipe.setServings(request.getServings());
        recipe.setDifficultyLevel(request.getDifficultyLevel());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setCategories(request.getCategories());
        recipe.setDietaryTags(request.getDietaryTags());
        
        // Update nutrition info
        Recipe.NutritionInfo nutritionInfo = recipe.getNutritionInfo();
        nutritionInfo.setCalories(request.getCalories());
        nutritionInfo.setProtein(request.getProtein());
        nutritionInfo.setCarbohydrates(request.getCarbohydrates());
        nutritionInfo.setFat(request.getFat());
        nutritionInfo.setFiber(request.getFiber());
        nutritionInfo.setSugar(request.getSugar());
        nutritionInfo.setSodium(request.getSodium());
        
        // Update ingredients
        ingredientRepository.deleteAll(recipe.getIngredients());
        
        if (request.getIngredients() != null) {
            for (RecipeCreateRequest.IngredientDto ingredientDto : request.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDto.getName());
                ingredient.setQuantity(ingredientDto.getQuantity());
                ingredient.setUnit(ingredientDto.getUnit());
                ingredient.setNotes(ingredientDto.getNotes());
                ingredient.setRecipe(recipe);
                ingredientRepository.save(ingredient);
            }
        }
        
        return recipeRepository.save(recipe);
    }
    
    public Recipe getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
        
        // Calculate and set computed fields
        Double avgRating = ratingRepository.findAverageRatingByRecipe(recipe);
        Long totalRatings = ratingRepository.countByRecipe(recipe);
        
        recipe.setAverageRating(avgRating != null ? avgRating : 0.0);
        recipe.setTotalRatings(totalRatings.intValue());
        
        return recipe;
    }
    
    public Page<Recipe> getAllApprovedRecipes(Pageable pageable) {
        return recipeRepository.findByStatus(Recipe.RecipeStatus.APPROVED, pageable);
    }
    
    public Page<Recipe> searchRecipes(String keyword, Pageable pageable) {
        return recipeRepository.findApprovedByTitleOrDescriptionContaining(keyword, pageable);
    }
    
    public Page<Recipe> getRecipesByCategories(List<Recipe.RecipeCategory> categories, Pageable pageable) {
        return recipeRepository.findApprovedByCategories(categories, pageable);
    }
    
    public Page<Recipe> getRecipesByDietaryTags(List<String> tags, Pageable pageable) {
        return recipeRepository.findApprovedByDietaryTags(tags, pageable);
    }
    
    public Page<Recipe> getRecipesByCalorieRange(Integer minCalories, Integer maxCalories, Pageable pageable) {
        return recipeRepository.findApprovedByCalorieRange(minCalories, maxCalories, pageable);
    }
    
    public Page<Recipe> getRecipesByMaxTime(Integer maxTime, Pageable pageable) {
        return recipeRepository.findApprovedByMaxTotalTime(maxTime, pageable);
    }
    
    public Page<Recipe> getRecipesByDifficulty(Recipe.DifficultyLevel difficulty, Pageable pageable) {
        return recipeRepository.findApprovedByDifficulty(difficulty, pageable);
    }
    
    public Page<Recipe> getTopRatedRecipes(Pageable pageable) {
        return recipeRepository.findTopRatedApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getLatestRecipes(Pageable pageable) {
        return recipeRepository.findLatestApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getMostFavoritedRecipes(Pageable pageable) {
        return recipeRepository.findMostFavoritedApprovedRecipes(pageable);
    }
    
    public Page<Recipe> getUserRecipes(User user, Pageable pageable) {
        return recipeRepository.findByAuthor(user, pageable);
    }
    
    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        User currentUser = authService.getCurrentUser();
        
        // Check if user can delete this recipe
        if (!recipe.getAuthor().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("You don't have permission to delete this recipe");
        }
        
        recipeRepository.delete(recipe);
    }
    
    // Admin methods
    public Recipe approveRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        recipe.setStatus(Recipe.RecipeStatus.APPROVED);
        return recipeRepository.save(recipe);
    }
    
    public Recipe rejectRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        recipe.setStatus(Recipe.RecipeStatus.REJECTED);
        return recipeRepository.save(recipe);
    }
    
    public Page<Recipe> getPendingRecipes(Pageable pageable) {
        return recipeRepository.findByStatus(Recipe.RecipeStatus.PENDING, pageable);
    }
}