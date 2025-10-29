package com.healthyrecipe.repository;

import com.healthyrecipe.entity.Favorite;
import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUser(User user);
    
    Page<Favorite> findByUser(User user, Pageable pageable);
    
    List<Favorite> findByRecipe(Recipe recipe);
    
    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
    
    Boolean existsByUserAndRecipe(User user, Recipe recipe);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.recipe = :recipe")
    Long countByRecipe(@Param("recipe") Recipe recipe);
    
    @Query("SELECT f.recipe FROM Favorite f WHERE f.user = :user ORDER BY f.createdAt DESC")
    Page<Recipe> findFavoriteRecipesByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT f FROM Favorite f WHERE f.user = :user ORDER BY f.createdAt DESC")
    Page<Favorite> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);
    
    void deleteByUserAndRecipe(User user, Recipe recipe);
}