package com.example.jwt.service;

import com.example.jwt.DTO.JwtResponse;
import com.example.jwt.DTO.LoginRequest;
import com.example.jwt.DTO.SignUpRequest;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Set<String> blacklistedTokens = new HashSet<>();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Register user
    public String registerUser(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "User registered successfully!";
    }

    // Authenticate user and generate JWT
    public JwtResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        // Use getUserId() method or the alias getId() method
        return new JwtResponse(token, user.getUserId()); // or user.getId()
    }

    // Fetch all users' emails
    public ArrayList<String> getUser() {
        return userRepository.findAllEmail();
    }

    // Logout user (blacklist token)
    public String logout(String token) {
        blacklistedTokens.add(token);
        SecurityContextHolder.clearContext();
        return "Logged out successfully!";
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}