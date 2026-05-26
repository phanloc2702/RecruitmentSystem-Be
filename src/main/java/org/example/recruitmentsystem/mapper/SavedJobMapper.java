package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.SavedJobResponse;
import org.example.recruitmentsystem.entity.SavedJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {JobPostMapper.class}
)
public interface SavedJobMapper {

    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "jobPostId", source = "jobPost.id")
    @Mapping(target = "job", source = "jobPost")
    SavedJobResponse toResponse(SavedJob savedJob);
}