package com.healthyrecipe.controller;

import com.healthyrecipe.entity.Favorite;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/favorites")
@PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity<?> addToFavorites(@PathVariable Long recipeId) {
        try {
            Favorite favorite = favoriteService.addToFavorites(recipeId);
            return ResponseEntity.ok(favorite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding to favorites: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<?> removeFromFavorites(@PathVariable Long recipeId) {
        try {
            favoriteService.removeFromFavorites(recipeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing from favorites: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-favorites")
    public ResponseEntity<Page<Recipe>> getMyFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> favorites = favoriteService.getUserFavoriteRecipes(pageable);
        return ResponseEntity.ok(favorites);
    }
    
    @GetMapping("/recipe/{recipeId}/is-favorited")
    public ResponseEntity<?> isRecipeFavorited(@PathVariable Long recipeId) {
        try {
            boolean isFavorited = favoriteService.isRecipeFavorited(recipeId);
            return ResponseEntity.ok(new FavoriteStatus(isFavorited));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking favorite status: " + e.getMessage());
        }
    }
    
    @GetMapping("/recipe/{recipeId}/count")
    public ResponseEntity<?> getFavoriteCount(@PathVariable Long recipeId) {
        try {
            Long count = favoriteService.getFavoriteCountForRecipe(recipeId);
            return ResponseEntity.ok(new FavoriteCount(count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting favorite count: " + e.getMessage());
        }
    }
    
    public static class FavoriteStatus {
        public final Boolean isFavorited;
        
        public FavoriteStatus(Boolean isFavorited) {
            this.isFavorited = isFavorited;
        }
    }
    
    public static class FavoriteCount {
        public final Long count;
        
        public FavoriteCount(Long count) {
            this.count = count;
        }
    }
}