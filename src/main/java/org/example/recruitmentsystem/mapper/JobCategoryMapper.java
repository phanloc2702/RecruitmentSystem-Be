package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.JobCategoryResponse;
import org.example.recruitmentsystem.entity.JobCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobCategoryMapper {

    JobCategoryResponse toResponse(JobCategory jobCategory);
}