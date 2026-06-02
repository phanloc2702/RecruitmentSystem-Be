package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.AdminCompanyFilterRequest;
import org.example.recruitmentsystem.dto.response.AdminCompanyDetailResponse;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.CompanyMapper;
import org.example.recruitmentsystem.mapper.JobPostMapper;
import org.example.recruitmentsystem.repository.CompanyRepository;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.service.AdminCompanyService;
import org.example.recruitmentsystem.specification.CompanySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompanyServiceImpl implements AdminCompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    @Override
    public PageResponse<CompanyResponse> getCompanies(
            AdminCompanyFilterRequest request
    ) {
        Pageable pageable = PaginationUtils.buildPageable(request);

        Page<Company> page = companyRepository.findAll(
                CompanySpecification.adminFilter(request),
                pageable
        );

        Page<CompanyResponse> responsePage =
                page.map(company -> {

                    CompanyResponse response =
                            companyMapper.toResponse(company);

                    response.setJobCount(
                            jobPostRepository.countByCompanyId(
                                    company.getId()
                            )
                    );

                    return response;
                });

        return PageResponse.<CompanyResponse>builder()
                .content(responsePage.getContent())
                .currentPage(responsePage.getNumber())
                .totalPages(responsePage.getTotalPages())
                .totalElements(responsePage.getTotalElements())
                .pageSize(responsePage.getSize())
                .build();
    }


    @Override
    public AdminCompanyDetailResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.COMPANY_NOT_FOUND)
                );

        CompanyResponse companyResponse = companyMapper.toResponse(company);

        List<JobResponse> jobs = jobPostRepository
                .findByCompanyIdOrderByCreatedAtDesc(company.getId())
                .stream()
                .map(jobPostMapper::toResponse)
                .toList();

        companyResponse.setJobCount((long) jobs.size());

        return AdminCompanyDetailResponse.builder()
                .company(companyResponse)
                .jobs(jobs)
                .jobCount(jobs.size())
                .build();
    }
}