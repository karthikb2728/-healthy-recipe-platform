package com.healthyrecipe.service;

import com.healthyrecipe.entity.Favorite;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private AuthService authService;
    
    public Favorite addToFavorites(Long recipeId) {
        User currentUser = authService.getCurrentUser();
        Recipe recipe = recipeService.getRecipeById(recipeId);
        
        // Check if already favorited
        if (favoriteRepository.existsByUserAndRecipe(currentUser, recipe)) {
            throw new RuntimeException("Recipe is already in favorites");
        }
        
        Favorite favorite = new Favorite(currentUser, recipe);
        return favoriteRepository.save(favorite);
    }
    
    public void removeFromFavorites(Long recipeId) {
        User currentUser = authService.getCurrentUser();
        Recipe recipe = recipeService.getRecipeById(recipeId);
        
        if (!favoriteRepository.existsByUserAndRecipe(currentUser, recipe)) {
            throw new RuntimeException("Recipe is not in favorites");
        }
        
        favoriteRepository.deleteByUserAndRecipe(currentUser, recipe);
    }
    
    public Page<Recipe> getUserFavoriteRecipes(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        return favoriteRepository.findFavoriteRecipesByUser(currentUser, pageable);
    }
    
    public boolean isRecipeFavorited(Long recipeId) {
        User currentUser = authService.getCurrentUser();
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return favoriteRepository.existsByUserAndRecipe(currentUser, recipe);
    }
    
    public Long getFavoriteCountForRecipe(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return favoriteRepository.countByRecipe(recipe);
    }
}