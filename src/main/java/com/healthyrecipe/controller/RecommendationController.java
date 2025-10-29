package com.healthyrecipe.controller;

import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @GetMapping("/personalized")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<Page<Recipe>> getPersonalizedRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recommendations = recommendationService.getPersonalizedRecommendations(pageable);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/fitness-goal")
    public ResponseEntity<Page<Recipe>> getRecipesByFitnessGoal(
            @RequestParam User.FitnessGoal fitnessGoal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getRecipesByFitnessGoal(fitnessGoal, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/quick")
    public ResponseEntity<Page<Recipe>> getQuickRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getQuickRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/healthy")
    public ResponseEntity<Page<Recipe>> getHealthyRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getHealthyRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/beginner")
    public ResponseEntity<Page<Recipe>> getBeginnerFriendlyRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getBeginnerFriendlyRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/allergy-free")
    public ResponseEntity<Page<Recipe>> getAllergyFreeRecipes(
            @RequestParam Set<String> allergies,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getRecipesForAllergies(allergies, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/similar/{recipeId}")
    public ResponseEntity<Page<Recipe>> getSimilarRecipes(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recommendationService.getSimilarRecipes(recipeId, pageable);
        return ResponseEntity.ok(recipes);
    }
}