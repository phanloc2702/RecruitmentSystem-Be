package org.example.recruitmentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        return ApiResponse.<List<NotificationResponse>>builder()
                .data(notificationService.getMyNotifications(email))
                .build();
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        return ApiResponse.<Long>builder()
                .data(notificationService.countUnread(email))
                .build();
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        String email = jwt.getSubject();

        notificationService.markAsRead(email, id);

        return ApiResponse.<Void>builder()
                .message("Đã đánh dấu là đã đọc.")
                .build();
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        notificationService.markAllAsRead(email);

        return ApiResponse.<Void>builder()
                .message("Đã đánh dấu tất cả là đã đọc.")
                .build();
    }
}