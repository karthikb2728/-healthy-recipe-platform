package com.healthyrecipe.controller;

import com.healthyrecipe.entity.Rating;
import com.healthyrecipe.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @PostMapping("/recipe/{recipeId}")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> rateRecipe(
            @PathVariable Long recipeId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        try {
            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }
            
            Rating ratingEntity = ratingService.createOrUpdateRating(recipeId, rating, comment);
            return ResponseEntity.ok(ratingEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rating recipe: " + e.getMessage());
        }
    }
    
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<Page<Rating>> getRecipeRatings(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Rating> ratings = ratingService.getRecipeRatings(recipeId, pageable);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/my-ratings")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<Page<Rating>> getMyRatings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Rating> ratings = ratingService.getUserRatings(pageable);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/recipe/{recipeId}/my-rating")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRatingForRecipe(@PathVariable Long recipeId) {
        try {
            Optional<Rating> rating = ratingService.getUserRatingForRecipe(recipeId);
            if (rating.isPresent()) {
                return ResponseEntity.ok(rating.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting rating: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRating(@PathVariable Long ratingId) {
        try {
            ratingService.deleteRating(ratingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting rating: " + e.getMessage());
        }
    }
    
    @GetMapping("/recipe/{recipeId}/average")
    public ResponseEntity<?> getAverageRating(@PathVariable Long recipeId) {
        try {
            Double avgRating = ratingService.getAverageRatingForRecipe(recipeId);
            Long totalRatings = ratingService.getTotalRatingsForRecipe(recipeId);
            
            return ResponseEntity.ok(new RatingStats(
                avgRating != null ? avgRating : 0.0,
                totalRatings
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting rating statistics: " + e.getMessage());
        }
    }
    
    public static class RatingStats {
        public final Double averageRating;
        public final Long totalRatings;
        
        public RatingStats(Double averageRating, Long totalRatings) {
            this.averageRating = averageRating;
            this.totalRatings = totalRatings;
        }
    }
}