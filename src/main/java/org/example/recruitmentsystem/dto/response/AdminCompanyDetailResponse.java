package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AdminCompanyDetailResponse {

    private CompanyResponse company;

    private List<JobResponse> jobs;

    private long jobCount;
}