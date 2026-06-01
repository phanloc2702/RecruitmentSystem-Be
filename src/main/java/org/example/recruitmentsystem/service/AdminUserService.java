package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminUserFilterRequest;
import org.example.recruitmentsystem.dto.response.AdminUserDetailResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.enumtype.UserStatus;

public interface AdminUserService {

    PageResponse<UserResponse> getUsers(
            AdminUserFilterRequest request
    );

    UserResponse updateUserStatus(
            Long userId,
            UserStatus status
    );
    AdminUserDetailResponse getUserById(Long id);
}