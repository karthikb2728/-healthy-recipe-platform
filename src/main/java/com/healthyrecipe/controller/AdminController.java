package com.healthyrecipe.controller;

import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.UserRepository;
import com.healthyrecipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/recipes/pending")
    public ResponseEntity<Page<Recipe>> getPendingRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getPendingRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @PutMapping("/recipes/{id}/approve")
    public ResponseEntity<?> approveRecipe(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.approveRecipe(id);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving recipe: " + e.getMessage());
        }
    }
    
    @PutMapping("/recipes/{id}/reject")
    public ResponseEntity<?> rejectRecipe(@PathVariable Long id) {
        try {
            Recipe recipe = recipeService.rejectRecipe(id);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rejecting recipe: " + e.getMessage());
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id, 
            @RequestParam User.AccountStatus status) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setStatus(status);
            userRepository.save(user);
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user status: " + e.getMessage());
        }
    }
    
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id, 
            @RequestParam User.Role role) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setRole(role);
            userRepository.save(user);
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user role: " + e.getMessage());
        }
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = userRepository.findByNameContaining(name);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/by-role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam User.Role role) {
        List<User> users = userRepository.findByRole(role);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/by-status")
    public ResponseEntity<List<User>> getUsersByStatus(@RequestParam User.AccountStatus status) {
        List<User> users = userRepository.findByStatus(status);
        return ResponseEntity.ok(users);
    }
}