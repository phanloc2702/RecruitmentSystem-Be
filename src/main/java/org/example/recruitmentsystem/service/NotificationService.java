package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.enumtype.NotificationType;

public interface NotificationService {

    PageResponse<NotificationResponse> getMyNotifications(
            String email,
            PaginationRequest request
    );

    NotificationResponse markAsRead(
            String email,
            Long notificationId
    );

    void markAllAsRead(String email);

    void createNotification(
            Long userId,
            String title,
            String content,
            NotificationType type,
            String redirectUrl
    );
}