package org.example.recruitmentsystem.mapper;

import org.example.recruitmentsystem.dto.response.CandidateCvResponse;
import org.example.recruitmentsystem.entity.CandidateCv;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateCvMapper {

    @Mapping(target = "candidateId", source = "candidate.id")
    CandidateCvResponse toResponse(CandidateCv candidateCv);
}