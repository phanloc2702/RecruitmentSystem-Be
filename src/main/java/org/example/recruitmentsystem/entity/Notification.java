package org.example.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.recruitmentsystem.enumtype.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // notifications.user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private NotificationType type = NotificationType.SYSTEM_NOTIFICATION;

    @Column(name = "redirect_url", length = 500)
    private String redirectUrl;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.type == null) {
            this.type = NotificationType.SYSTEM_NOTIFICATION;
        }

        if (this.isRead == null) {
            this.isRead = false;
        }

        this.createdAt = LocalDateTime.now();
    }
}