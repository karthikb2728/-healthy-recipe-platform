package com.healthyrecipe.dto.auth;

import com.healthyrecipe.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class SignupRequest {
    
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    @NotBlank
    @Size(max = 50)
    private String firstName;
    
    @NotBlank
    @Size(max = 50)
    private String lastName;
    
    @Size(max = 15)
    private String phoneNumber;
    
    private String bio;
    
    private Set<String> role;
    
    private Set<User.DietaryPreference> dietaryPreferences;
    
    private Set<String> allergies;
    
    private User.FitnessGoal fitnessGoal;
    
    private Integer dailyCalorieTarget;
    
    public SignupRequest() {}
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public Set<String> getRole() {
        return role;
    }
    
    public void setRole(Set<String> role) {
        this.role = role;
    }
    
    public Set<User.DietaryPreference> getDietaryPreferences() {
        return dietaryPreferences;
    }
    
    public void setDietaryPreferences(Set<User.DietaryPreference> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
    
    public Set<String> getAllergies() {
        return allergies;
    }
    
    public void setAllergies(Set<String> allergies) {
        this.allergies = allergies;
    }
    
    public User.FitnessGoal getFitnessGoal() {
        return fitnessGoal;
    }
    
    public void setFitnessGoal(User.FitnessGoal fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }
    
    public Integer getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }
    
    public void setDailyCalorieTarget(Integer dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }
}