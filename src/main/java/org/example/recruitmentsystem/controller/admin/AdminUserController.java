package org.example.recruitmentsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminUserFilterRequest;
import org.example.recruitmentsystem.dto.request.UpdateUserStatusRequest;
import org.example.recruitmentsystem.dto.response.AdminUserDetailResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @Valid AdminUserFilterRequest request
    ) {
        return ApiResponse.success(
                "Lấy danh sách người dùng thành công",
                adminUserService.getUsers(request)
        );
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<UserResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ApiResponse.success(
                "Cập nhật trạng thái người dùng thành công",
                adminUserService.updateUserStatus(
                        id,
                        request.getStatus()
                )
        );
    }
    @GetMapping("/{id}")
    public ApiResponse<AdminUserDetailResponse> getUserById(
            @PathVariable Long id
    ) {
        return ApiResponse.success(
                "Lấy chi tiết người dùng thành công",
                adminUserService.getUserById(id)
        );
    }
}