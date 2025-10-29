package com.healthyrecipe.service;

import com.healthyrecipe.entity.Rating;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private AuthService authService;
    
    public Rating createOrUpdateRating(Long recipeId, Integer rating, String comment) {
        User currentUser = authService.getCurrentUser();
        Recipe recipe = recipeService.getRecipeById(recipeId);
        
        // Check if user already rated this recipe
        Optional<Rating> existingRating = ratingRepository.findByUserAndRecipe(currentUser, recipe);
        
        Rating ratingEntity;
        if (existingRating.isPresent()) {
            // Update existing rating
            ratingEntity = existingRating.get();
            ratingEntity.setRating(rating);
            ratingEntity.setComment(comment);
        } else {
            // Create new rating
            ratingEntity = new Rating();
            ratingEntity.setUser(currentUser);
            ratingEntity.setRecipe(recipe);
            ratingEntity.setRating(rating);
            ratingEntity.setComment(comment);
        }
        
        return ratingRepository.save(ratingEntity);
    }
    
    public Page<Rating> getRecipeRatings(Long recipeId, Pageable pageable) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ratingRepository.findByRecipeOrderByCreatedAtDesc(recipe, pageable);
    }
    
    public Page<Rating> getUserRatings(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        return ratingRepository.findByUser(currentUser, pageable);
    }
    
    public Optional<Rating> getUserRatingForRecipe(Long recipeId) {
        User currentUser = authService.getCurrentUser();
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ratingRepository.findByUserAndRecipe(currentUser, recipe);
    }
    
    public void deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        
        User currentUser = authService.getCurrentUser();
        
        // Check if user can delete this rating
        if (!rating.getUser().getId().equals(currentUser.getId()) && 
            currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("You don't have permission to delete this rating");
        }
        
        ratingRepository.delete(rating);
    }
    
    public Double getAverageRatingForRecipe(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ratingRepository.findAverageRatingByRecipe(recipe);
    }
    
    public Long getTotalRatingsForRecipe(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ratingRepository.countByRecipe(recipe);
    }
}