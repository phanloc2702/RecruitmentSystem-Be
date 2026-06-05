package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.request.CandidateProfileRequest;
import org.example.recruitmentsystem.dto.response.CandidateProfileResponse;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    CandidateProfileResponse toResponse(CandidateProfile profile);

    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "avatarObjectName", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateEntityFromRequest(
            CandidateProfileRequest request,
            @MappingTarget CandidateProfile profile
    );
}