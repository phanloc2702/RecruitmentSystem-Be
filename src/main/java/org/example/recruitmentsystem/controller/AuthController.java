package org.example.recruitmentsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.request.ForgotPasswordRequest;
import org.example.recruitmentsystem.dto.request.LoginRequest;
import org.example.recruitmentsystem.dto.request.RegisterRequest;
import org.example.recruitmentsystem.dto.request.ResetPasswordRequest;
import org.example.recruitmentsystem.dto.response.AuthResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.service.AuthService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);

        return ApiResponse.success("Đăng ký thành công", response);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ApiResponse.success("Đăng nhập thành công", response);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        UserResponse response = authService.getCurrentUser(email);

        return ApiResponse.success("Lấy thông tin người dùng thành công", response);
    }
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        authService.forgotPassword(request);

        return ApiResponse.<Void>builder()
                .message("Nếu email tồn tại, chúng tôi đã gửi hướng dẫn đặt lại mật khẩu.")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        authService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .message("Đặt lại mật khẩu thành công.")
                .build();
    }
}