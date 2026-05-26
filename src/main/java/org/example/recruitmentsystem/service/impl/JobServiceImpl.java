package org.example.recruitmentsystem.service.impl;

import org.example.recruitmentsystem.dto.request.RecruiterJobFilterRequest;
import org.example.recruitmentsystem.entity.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.JobPostFilterRequest;
import org.example.recruitmentsystem.dto.request.RecruiterJobRequest;
import org.example.recruitmentsystem.dto.response.JobCategoryResponse;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.entity.JobCategory;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.JobPostStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.JobCategoryMapper;
import org.example.recruitmentsystem.mapper.JobPostMapper;
import org.example.recruitmentsystem.repository.CompanyRepository;
import org.example.recruitmentsystem.repository.JobCategoryRepository;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.JobService;
import org.example.recruitmentsystem.specification.JobPostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobPostRepository jobPostRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobPostMapper jobPostMapper;
    private final JobCategoryMapper jobCategoryMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    @Override
    public PageResponse<JobResponse> getJobs(JobPostFilterRequest request) {
        Pageable pageable = PaginationUtils.buildPageable(request);

        Specification<JobPost> specification = Specification
                .where(JobPostSpecification.hasOpenStatus())
                .and(JobPostSpecification.hasApprovedStatus())
                .and(JobPostSpecification.keywordContains(request.getKeyword()))
                .and(JobPostSpecification.locationContains(request.getLocation()))
                .and(JobPostSpecification.hasEmploymentType(request.getEmploymentType()))
                .and(JobPostSpecification.hasCategoryId(request.getCategoryId()))
                .and(JobPostSpecification.salaryMinGreaterThanOrEqual(request.getSalaryMin()))
                .and(JobPostSpecification.salaryMaxLessThanOrEqual(request.getSalaryMax()));

        Page<JobPost> jobPage = jobPostRepository.findAll(specification, pageable);

        return PageResponse.<JobResponse>builder()
                .content(
                        jobPage.getContent()
                                .stream()
                                .map(jobPostMapper::toResponse)
                                .toList()
                )
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalElements(jobPage.getTotalElements())
                .pageSize(jobPage.getSize())
                .build();
    }

    @Override
    public JobResponse getJobById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (jobPost.getStatus() != org.example.recruitmentsystem.enumtype.JobPostStatus.OPEN
                || jobPost.getApprovalStatus() != org.example.recruitmentsystem.enumtype.ApprovalStatus.APPROVED) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        jobPost.setViewCount(jobPost.getViewCount() + 1);
        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return jobPostMapper.toResponse(savedJobPost);
    }

    @Override
    public List<JobCategoryResponse> getCategories() {
        return jobCategoryRepository.findAll()
                .stream()
                .map(jobCategoryMapper::toResponse)
                .toList();
    }
    @Override
    public PageResponse<JobResponse> getRecruiterJobs(String email, RecruiterJobFilterRequest request) {
        Company company = getRecruiterCompany(email);

        Pageable pageable = PaginationUtils.buildPageable(request);

        Specification<JobPost> specification = Specification
                .where(JobPostSpecification.belongsToCompany(company.getId()))
                .and(JobPostSpecification.recruiterKeywordContains(request.getKeyword()))
                .and(JobPostSpecification.hasStatus(request.getStatus()))
                .and(JobPostSpecification.hasApprovalStatus(request.getApprovalStatus()));

        Page<JobPost> jobPage = jobPostRepository.findAll(specification, pageable);

        return PageResponse.<JobResponse>builder()
                .content(jobPage.getContent()
                        .stream()
                        .map(jobPostMapper::toResponse)
                        .toList())
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalElements(jobPage.getTotalElements())
                .pageSize(jobPage.getSize())
                .build();
    }

    @Override
    public JobResponse getRecruiterJobById(String email, Long id) {
        Company company = getRecruiterCompany(email);

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        validateJobOwnership(jobPost, company);

        return jobPostMapper.toResponse(jobPost);
    }

    @Override
    @Transactional
    public JobResponse createJob(String email, RecruiterJobRequest request) {
        Company company = getRecruiterCompany(email);

        JobCategory category = null;
        if (request.getCategoryId() != null) {
            category = jobCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        }

        JobPost jobPost = jobPostMapper.toEntity(request);
        jobPost.setCompany(company);
        jobPost.setCategory(category);
        jobPost.setStatus(JobPostStatus.OPEN);
        jobPost.setApprovalStatus(ApprovalStatus.PENDING);
        jobPost.setViewCount(0);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return jobPostMapper.toResponse(savedJobPost);
    }

    @Override
    @Transactional
    public JobResponse updateJob(String email, Long id, RecruiterJobRequest request) {
        Company company = getRecruiterCompany(email);

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        validateJobOwnership(jobPost, company);

        jobPostMapper.updateEntityFromRequest(request, jobPost);

        if (request.getCategoryId() != null) {
            JobCategory category = jobCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
            jobPost.setCategory(category);
        }

        jobPost.setApprovalStatus(ApprovalStatus.PENDING);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return jobPostMapper.toResponse(savedJobPost);
    }

    @Override
    @Transactional
    public void deleteJob(String email, Long id) {
        Company company = getRecruiterCompany(email);

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        validateJobOwnership(jobPost, company);

        jobPost.setStatus(JobPostStatus.HIDDEN);
        jobPostRepository.save(jobPost);
    }

    private Company getRecruiterCompany(String email) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return companyRepository.findByRecruiter(recruiter)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private void validateJobOwnership(JobPost jobPost, Company company) {
        if (!jobPost.getCompany().getId().equals(company.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}