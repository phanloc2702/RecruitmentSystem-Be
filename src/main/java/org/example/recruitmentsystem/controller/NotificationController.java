package org.example.recruitmentsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<PageResponse<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @ModelAttribute PaginationRequest request
    ) {
        PageResponse<NotificationResponse> response =
                notificationService.getMyNotifications(
                        jwt.getSubject(),
                        request
                );

        return ApiResponse.success(
                "Lấy danh sách thông báo thành công",
                response
        );
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markAsRead(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        NotificationResponse response =
                notificationService.markAsRead(
                        jwt.getSubject(),
                        id
                );

        return ApiResponse.success(
                "Đánh dấu đã đọc thành công",
                response
        );
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(
            @AuthenticationPrincipal Jwt jwt
    ) {
        notificationService.markAllAsRead(jwt.getSubject());

        return ApiResponse.success(
                "Đánh dấu tất cả thông báo đã đọc thành công"
        );
    }
}