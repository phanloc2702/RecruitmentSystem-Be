package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.UserRole;
import org.example.recruitmentsystem.enumtype.UserStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private UserRole role;

    private UserStatus status;

    private String fullName;

    private String avatarUrl;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}