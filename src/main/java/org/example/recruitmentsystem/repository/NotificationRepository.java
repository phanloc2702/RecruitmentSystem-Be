package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.Notification;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Long countByUserAndIsReadFalse(User user);
}