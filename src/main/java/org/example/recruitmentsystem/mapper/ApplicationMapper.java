package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.ApplicationResponse;
import org.example.recruitmentsystem.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(
        componentModel = "spring",
        uses = {
                JobPostMapper.class,
                CandidateProfileMapper.class,
                CandidateCvMapper.class
        }
)
public interface ApplicationMapper {

    @Mapping(target = "jobPostId", source = "jobPost.id")
    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "job", source = "jobPost")
    @Mapping(target = "cv", source = "cv")
    ApplicationResponse toResponse(Application application);
}