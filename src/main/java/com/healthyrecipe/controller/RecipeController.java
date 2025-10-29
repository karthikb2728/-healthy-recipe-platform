package com.healthyrecipe.controller;

import com.healthyrecipe.dto.recipe.RecipeCreateRequest;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    
    @Autowired
    private RecipeService recipeService;
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeCreateRequest request) {
        try {
            Recipe recipe = recipeService.createRecipe(request);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating recipe: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeCreateRequest request) {
        try {
            Recipe recipe = recipeService.updateRecipe(id, request);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating recipe: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/public")
    public ResponseEntity<Page<Recipe>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Recipe> recipes = recipeService.getAllApprovedRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Recipe>> searchRecipes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.searchRecipes(keyword, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/category")
    public ResponseEntity<Page<Recipe>> getRecipesByCategory(
            @RequestParam List<Recipe.RecipeCategory> categories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getRecipesByCategories(categories, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/dietary-tags")
    public ResponseEntity<Page<Recipe>> getRecipesByDietaryTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getRecipesByDietaryTags(tags, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/calories")
    public ResponseEntity<Page<Recipe>> getRecipesByCalorieRange(
            @RequestParam Integer minCalories,
            @RequestParam Integer maxCalories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getRecipesByCalorieRange(minCalories, maxCalories, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/quick")
    public ResponseEntity<Page<Recipe>> getQuickRecipes(
            @RequestParam Integer maxTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getRecipesByMaxTime(maxTime, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/difficulty")
    public ResponseEntity<Page<Recipe>> getRecipesByDifficulty(
            @RequestParam Recipe.DifficultyLevel difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getRecipesByDifficulty(difficulty, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<Page<Recipe>> getTopRatedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getTopRatedRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/latest")
    public ResponseEntity<Page<Recipe>> getLatestRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getLatestRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/most-favorited")
    public ResponseEntity<Page<Recipe>> getMostFavoritedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getMostFavoritedRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting recipe: " + e.getMessage());
        }
    }
}