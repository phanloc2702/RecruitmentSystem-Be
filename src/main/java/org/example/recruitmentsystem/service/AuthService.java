package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.request.LoginRequest;
import org.example.recruitmentsystem.dto.request.RegisterRequest;
import org.example.recruitmentsystem.dto.response.AuthResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String email);
}