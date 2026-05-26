package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationResponse {

    private Long id;

    private String title;

    private String content;

    private NotificationType type;

    private String redirectUrl;

    private Boolean isRead;

    private LocalDateTime createdAt;
}