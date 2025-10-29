package com.healthyrecipe.controller;

import com.healthyrecipe.entity.Recipe;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.UserRepository;
import com.healthyrecipe.service.AuthService;
import com.healthyrecipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
@PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
public class ProfileController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            User currentUser = authService.getCurrentUser();
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting profile: " + e.getMessage());
        }
    }
    
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            
            if (request.getFirstName() != null) {
                currentUser.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                currentUser.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                // Check if email is already taken by another user
                if (userRepository.existsByEmail(request.getEmail()) && 
                    !currentUser.getEmail().equals(request.getEmail())) {
                    return ResponseEntity.badRequest().body("Email is already taken");
                }
                currentUser.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null) {
                currentUser.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getBio() != null) {
                currentUser.setBio(request.getBio());
            }
            if (request.getProfileImageUrl() != null) {
                currentUser.setProfileImageUrl(request.getProfileImageUrl());
            }
            if (request.getDietaryPreferences() != null) {
                currentUser.setDietaryPreferences(request.getDietaryPreferences());
            }
            if (request.getAllergies() != null) {
                currentUser.setAllergies(request.getAllergies());
            }
            if (request.getFitnessGoal() != null) {
                currentUser.setFitnessGoal(request.getFitnessGoal());
            }
            if (request.getDailyCalorieTarget() != null) {
                currentUser.setDailyCalorieTarget(request.getDailyCalorieTarget());
            }
            
            User updatedUser = userRepository.save(currentUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }
            
            // Update password
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(currentUser);
            
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error changing password: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-recipes")
    public ResponseEntity<Page<Recipe>> getMyRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.getUserRecipes(currentUser, pageable);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Return public profile information only
            PublicProfile profile = new PublicProfile(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfileImageUrl(),
                user.getRole(),
                user.getCreatedAt()
            );
            
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}/recipes")
    public ResponseEntity<Page<Recipe>> getUserRecipes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Recipe> recipes = recipeService.getUserRecipes(user, pageable);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DTO classes
    public static class ProfileUpdateRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String bio;
        private String profileImageUrl;
        private Set<User.DietaryPreference> dietaryPreferences;
        private Set<String> allergies;
        private User.FitnessGoal fitnessGoal;
        private Integer dailyCalorieTarget;
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        
        public String getProfileImageUrl() { return profileImageUrl; }
        public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
        
        public Set<User.DietaryPreference> getDietaryPreferences() { return dietaryPreferences; }
        public void setDietaryPreferences(Set<User.DietaryPreference> dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }
        
        public Set<String> getAllergies() { return allergies; }
        public void setAllergies(Set<String> allergies) { this.allergies = allergies; }
        
        public User.FitnessGoal getFitnessGoal() { return fitnessGoal; }
        public void setFitnessGoal(User.FitnessGoal fitnessGoal) { this.fitnessGoal = fitnessGoal; }
        
        public Integer getDailyCalorieTarget() { return dailyCalorieTarget; }
        public void setDailyCalorieTarget(Integer dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }
    }
    
    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;
        
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
    
    public static class PublicProfile {
        public final Long id;
        public final String username;
        public final String firstName;
        public final String lastName;
        public final String bio;
        public final String profileImageUrl;
        public final User.Role role;
        public final java.time.LocalDateTime createdAt;
        
        public PublicProfile(Long id, String username, String firstName, String lastName, 
                           String bio, String profileImageUrl, User.Role role, 
                           java.time.LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.bio = bio;
            this.profileImageUrl = profileImageUrl;
            this.role = role;
            this.createdAt = createdAt;
        }
    }
}