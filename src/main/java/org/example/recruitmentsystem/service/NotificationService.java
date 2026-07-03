package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.NotificationType;

import java.util.List;

public interface NotificationService {

    void createNotification(
            User user,
            NotificationType type,
            String title,
            String message,
            String redirectUrl
    );

    List<NotificationResponse> getMyNotifications(String email);

    Long countUnread(String email);

    void markAsRead(String email, Long notificationId);

    void markAllAsRead(String email);
}