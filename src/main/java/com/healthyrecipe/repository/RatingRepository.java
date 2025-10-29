package com.healthyrecipe.repository;

import com.healthyrecipe.entity.Rating;
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
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByRecipe(Recipe recipe);
    
    Page<Rating> findByRecipe(Recipe recipe, Pageable pageable);
    
    List<Rating> findByUser(User user);
    
    Page<Rating> findByUser(User user, Pageable pageable);
    
    Optional<Rating> findByUserAndRecipe(User user, Recipe recipe);
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.recipe = :recipe")
    Double findAverageRatingByRecipe(@Param("recipe") Recipe recipe);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.recipe = :recipe")
    Long countByRecipe(@Param("recipe") Recipe recipe);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.recipe = :recipe AND r.rating = :rating")
    Long countByRecipeAndRating(@Param("recipe") Recipe recipe, @Param("rating") Integer rating);
    
    @Query("SELECT r FROM Rating r WHERE r.recipe = :recipe ORDER BY r.createdAt DESC")
    Page<Rating> findByRecipeOrderByCreatedAtDesc(@Param("recipe") Recipe recipe, Pageable pageable);
    
    @Query("SELECT r FROM Rating r WHERE r.recipe = :recipe AND r.rating >= :minRating ORDER BY r.createdAt DESC")
    List<Rating> findByRecipeAndRatingGreaterThanEqual(@Param("recipe") Recipe recipe, @Param("minRating") Integer minRating);
}