package org.example.recruitmentsystem.specification;

import org.example.recruitmentsystem.entity.Notification;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

    private NotificationSpecification() {
    }

    public static Specification<Notification> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("user").get("id"),
                    userId
            );
        };
    }
}