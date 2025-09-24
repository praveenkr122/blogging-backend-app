package com.example.jwt.controller;

import com.example.jwt.DTO.ApiResponse;
import com.example.jwt.DTO.JwtResponse;
import com.example.jwt.DTO.LoginRequest;
import com.example.jwt.DTO.SignUpRequest;
import com.example.jwt.service.AuthService;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignUpRequest request) {
        String msg = authService.registerUser(request);
        return new ApiResponse<Void>(true, msg, null);
    }

    // Login endpoint
    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse jwt = authService.authenticateUser(request);
        return new ApiResponse<JwtResponse>(true, "Login successful", jwt);
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponse<Void>(false, "Invalid token", null);
        }

        String token = authHeader.substring(7);
        authService.logout(token);
        return new ApiResponse<Void>(true, "Logged out successfully", null);
    }

    // Get all users (emails)
    @GetMapping("/user")
    public ApiResponse<ArrayList<String>> getAllUsers() {
        ArrayList<String> users = authService.getUser();
        return new ApiResponse<ArrayList<String>>(true, "Users fetched successfully", users);
    }

    // Test hello endpoint
    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return new ApiResponse<String>(true, "Hello from server", "hello");
    }
}
