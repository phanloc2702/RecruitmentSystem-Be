package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.NotificationResponse;
import org.example.recruitmentsystem.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}