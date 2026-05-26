package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.entity.Notification;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.NotificationType;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.NotificationMapper;
import org.example.recruitmentsystem.repository.NotificationRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.NotificationService;
import org.example.recruitmentsystem.specification.NotificationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public PageResponse<NotificationResponse> getMyNotifications(
            String email,
            PaginationRequest request
    ) {
        User user = getUserByEmail(email);

        Pageable pageable = PaginationUtils.buildPageable(request);

        Specification<Notification> specification = Specification
                .where(NotificationSpecification.belongsToUser(user.getId()));

        Page<Notification> notificationPage = notificationRepository.findAll(
                specification,
                pageable
        );

        return PageResponse.<NotificationResponse>builder()
                .content(
                        notificationPage.getContent()
                                .stream()
                                .map(notificationMapper::toResponse)
                                .toList()
                )
                .currentPage(notificationPage.getNumber())
                .totalPages(notificationPage.getTotalPages())
                .totalElements(notificationPage.getTotalElements())
                .pageSize(notificationPage.getSize())
                .build();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(String email, Long notificationId) {
        User user = getUserByEmail(email);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        notification.setIsRead(true);

        Notification savedNotification = notificationRepository.save(notification);

        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String email) {
        User user = getUserByEmail(email);

        Specification<Notification> specification = Specification
                .where(NotificationSpecification.belongsToUser(user.getId()));

        Page<Notification> notificationPage = notificationRepository.findAll(
                specification,
                Pageable.unpaged()
        );

        notificationPage.getContent().forEach(notification -> notification.setIsRead(true));

        notificationRepository.saveAll(notificationPage.getContent());
    }

    @Override
    @Transactional
    public void createNotification(
            Long userId,
            String title,
            String content,
            NotificationType type,
            String redirectUrl
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .redirectUrl(redirectUrl)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}