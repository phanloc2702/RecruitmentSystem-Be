package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.request.ForgotPasswordRequest;
import org.example.recruitmentsystem.dto.request.LoginRequest;
import org.example.recruitmentsystem.dto.request.RegisterRequest;
import org.example.recruitmentsystem.dto.request.ResetPasswordRequest;
import org.example.recruitmentsystem.dto.response.AuthResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String email);
    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    @Transactional
    void resetPassword(
            ResetPasswordRequest request
    );

    @Transactional
    void resetPassword(
            ResetPasswordRequest request
    );
}