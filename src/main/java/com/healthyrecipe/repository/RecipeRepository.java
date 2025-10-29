package com.healthyrecipe.repository;

import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    List<Recipe> findByAuthor(User author);
    
    Page<Recipe> findByAuthor(User author, Pageable pageable);
    
    List<Recipe> findByStatus(Recipe.RecipeStatus status);
    
    Page<Recipe> findByStatus(Recipe.RecipeStatus status, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Recipe> findByTitleOrDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED' " +
           "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Recipe> findApprovedByTitleOrDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r JOIN r.categories c WHERE c IN :categories AND r.status = 'APPROVED'")
    Page<Recipe> findApprovedByCategories(@Param("categories") List<Recipe.RecipeCategory> categories, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r JOIN r.dietaryTags dt WHERE dt IN :tags AND r.status = 'APPROVED'")
    Page<Recipe> findApprovedByDietaryTags(@Param("tags") List<String> tags, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED' " +
           "AND r.nutritionInfo.calories BETWEEN :minCalories AND :maxCalories")
    Page<Recipe> findApprovedByCalorieRange(@Param("minCalories") Integer minCalories, 
                                          @Param("maxCalories") Integer maxCalories, 
                                          Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED' " +
           "AND r.preparationTime + r.cookingTime <= :maxTime")
    Page<Recipe> findApprovedByMaxTotalTime(@Param("maxTime") Integer maxTime, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED' " +
           "AND r.difficultyLevel = :difficulty")
    Page<Recipe> findApprovedByDifficulty(@Param("difficulty") Recipe.DifficultyLevel difficulty, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r LEFT JOIN r.ratings rt " +
           "WHERE r.status = 'APPROVED' " +
           "GROUP BY r.id " +
           "ORDER BY AVG(rt.rating) DESC")
    Page<Recipe> findTopRatedApprovedRecipes(Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED' " +
           "ORDER BY r.createdAt DESC")
    Page<Recipe> findLatestApprovedRecipes(Pageable pageable);
    
    @Query("SELECT r FROM Recipe r LEFT JOIN r.favorites f " +
           "WHERE r.status = 'APPROVED' " +
           "GROUP BY r.id " +
           "ORDER BY COUNT(f.id) DESC")
    Page<Recipe> findMostFavoritedApprovedRecipes(Pageable pageable);
}