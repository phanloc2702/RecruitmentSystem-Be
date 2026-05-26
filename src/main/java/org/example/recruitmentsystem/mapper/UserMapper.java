package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.UserRole;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "phone", ignore = true)
    UserResponse toResponse(User user);

    @AfterMapping
    default void fillExtraInfo(User user, @MappingTarget UserResponse.UserResponseBuilder builder) {
        if (user == null || user.getRole() == null) {
            return;
        }

        if (user.getRole() == UserRole.CANDIDATE && user.getCandidateProfile() != null) {
            CandidateProfile profile = user.getCandidateProfile();

            builder.fullName(profile.getFullName());
            builder.avatarUrl(profile.getAvatarUrl());
            builder.phone(profile.getPhone());
        }

        if (user.getRole() == UserRole.RECRUITER && user.getCompany() != null) {
            Company company = user.getCompany();

            builder.fullName(company.getName());
            builder.avatarUrl(company.getLogoUrl());
            builder.phone(company.getPhone());
        }
    }
}