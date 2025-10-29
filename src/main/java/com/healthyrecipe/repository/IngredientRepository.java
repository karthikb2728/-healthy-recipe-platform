package com.healthyrecipe.repository;

import com.healthyrecipe.entity.Ingredient;
import com.healthyrecipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    List<Ingredient> findByRecipe(Recipe recipe);
    
    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Ingredient> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT DISTINCT i.name FROM Ingredient i ORDER BY i.name")
    List<String> findDistinctIngredientNames();
    
    @Query("SELECT i FROM Ingredient i WHERE i.recipe = :recipe ORDER BY i.name")
    List<Ingredient> findByRecipeOrderByName(@Param("recipe") Recipe recipe);
}