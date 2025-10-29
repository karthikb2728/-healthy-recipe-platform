package com.healthyrecipe.service;

import com.healthyrecipe.dto.auth.JwtResponse;
import com.healthyrecipe.dto.auth.LoginRequest;
import com.healthyrecipe.dto.auth.SignupRequest;
import com.healthyrecipe.entity.User;
import com.healthyrecipe.repository.UserRepository;
import com.healthyrecipe.security.JwtUtils;
import com.healthyrecipe.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles);
    }
    
    public String registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "Error: Username is already taken!";
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Error: Email is already in use!";
        }
        
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName());
        
        // Set optional fields
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setBio(signUpRequest.getBio());
        
        // Set role
        Set<String> strRoles = signUpRequest.getRole();
        User.Role role = User.Role.USER;
        
        if (strRoles != null && !strRoles.isEmpty()) {
            // Get the first role from the set
            String roleStr = strRoles.iterator().next();
            switch (roleStr.toUpperCase()) {
                case "ADMIN":
                    role = User.Role.ADMIN;
                    break;
                case "CHEF":
                    role = User.Role.CHEF;
                    break;
                default:
                    role = User.Role.USER;
            }
        }
        
        user.setRole(role);
        
        // Set dietary preferences and health goals
        if (signUpRequest.getDietaryPreferences() != null) {
            user.setDietaryPreferences(signUpRequest.getDietaryPreferences());
        }
        
        if (signUpRequest.getAllergies() != null) {
            user.setAllergies(signUpRequest.getAllergies());
        }
        
        user.setFitnessGoal(signUpRequest.getFitnessGoal());
        user.setDailyCalorieTarget(signUpRequest.getDailyCalorieTarget());
        
        userRepository.save(user);
        
        return "User registered successfully!";
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        return userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}