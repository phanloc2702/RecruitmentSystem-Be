package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.dto.request.CompanyRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.CompanyStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.CompanyMapper;
import org.example.recruitmentsystem.repository.CompanyRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.example.recruitmentsystem.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.example.recruitmentsystem.specification.CompanySpecification;
import org.springframework.data.jpa.domain.Specification;
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponse getMyCompany(String email) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Company company = companyRepository.findByRecruiter(recruiter)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        return companyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse updateMyCompany(String email, CompanyRequest request) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Company company = companyRepository.findByRecruiter(recruiter)
                .orElse(null);

        if (company == null) {
            if (!StringUtils.hasText(request.getName())) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST);
            }

            company = Company.builder()
                    .recruiter(recruiter)
                    .name(request.getName())
                    .status(CompanyStatus.PENDING)
                    .build();
        }

        companyMapper.updateEntityFromRequest(request, company);

        Company savedCompany = companyRepository.save(company);

        return companyMapper.toResponse(savedCompany);
    }
    @Override
    public PageResponse<CompanyResponse> getCompanies(String keyword, String industry, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Company> specification = Specification
                .where(CompanySpecification.hasStatus(CompanyStatus.APPROVED))
                .and(CompanySpecification.keywordContains(keyword))
                .and(CompanySpecification.industryContains(industry));

        Page<Company> companyPage = companyRepository.findAll(specification, pageable);

        return PageResponse.<CompanyResponse>builder()
                .content(companyPage.getContent()
                        .stream()
                        .map(companyMapper::toResponse)
                        .toList())
                .currentPage(companyPage.getNumber())
                .totalPages(companyPage.getTotalPages())
                .totalElements(companyPage.getTotalElements())
                .pageSize(companyPage.getSize())
                .build();
    }
    @Override
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (company.getStatus() != CompanyStatus.APPROVED) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        return companyMapper.toResponse(company);
    }
}