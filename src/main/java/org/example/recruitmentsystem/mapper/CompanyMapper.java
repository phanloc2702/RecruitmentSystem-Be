package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.request.CompanyRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.entity.Company;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "recruiterId", source = "recruiter.id")
    @Mapping(target = "jobCount", expression = "java(0L)")
    CompanyResponse toResponse(Company company);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(
            CompanyRequest request,
            @MappingTarget Company company
    );
}