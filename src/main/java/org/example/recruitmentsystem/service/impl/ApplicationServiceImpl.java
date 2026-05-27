package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.ApplicationFilterRequest;
import org.example.recruitmentsystem.dto.request.ApplicationRequest;
import org.example.recruitmentsystem.dto.request.UpdateApplicationStatusReques;
import org.example.recruitmentsystem.dto.response.ApplicationResponse;
import org.example.recruitmentsystem.entity.*;
import org.example.recruitmentsystem.enumtype.ApplicationStatus;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.JobPostStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.ApplicationMapper;
import org.example.recruitmentsystem.repository.*;
import org.example.recruitmentsystem.service.ApplicationService;
import org.example.recruitmentsystem.specification.ApplicationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CompanyRepository companyRepository;
    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final CandidateCvRepository candidateCvRepository;
    @Override
    @Transactional
    public ApplicationResponse applyJob(String email, ApplicationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (jobPost.getStatus() != JobPostStatus.OPEN) {
            throw new BusinessException(ErrorCode.JOB_NOT_OPEN);
        }

        if (jobPost.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new BusinessException(ErrorCode.JOB_NOT_APPROVED);
        }

        if (applicationRepository.existsByJobPostAndCandidate(jobPost, candidate)) {
            throw new BusinessException(ErrorCode.APPLICATION_ALREADY_EXISTS);
        }

        CandidateCv selectedCv = null;

        if (request.getCvId() != null) {
            selectedCv = candidateCvRepository
                    .findByIdAndCandidateId(
                            request.getCvId(),
                            candidate.getId()
                    )
                    .orElseThrow(() -> new BusinessException(ErrorCode.CV_NOT_FOUND));
        }

        Application application = Application.builder()
                .jobPost(jobPost)
                .candidate(candidate)
                .cv(selectedCv)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();

        Application savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponse(savedApplication);
    }

    @Override
    public PageResponse<ApplicationResponse> getMyApplications(
            String email,
            ApplicationFilterRequest request
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        Pageable pageable = PaginationUtils.buildPageable(request);

        Specification<Application> specification = Specification
                .where(ApplicationSpecification.belongsToCandidate(candidate.getId()))
                .and(ApplicationSpecification.hasStatus(request.getStatus()));

        Page<Application> applicationPage = applicationRepository.findAll(specification, pageable);

        return PageResponse.<ApplicationResponse>builder()
                .content(applicationPage.getContent()
                        .stream()
                        .map(applicationMapper::toResponse)
                        .toList())
                .currentPage(applicationPage.getNumber())
                .totalPages(applicationPage.getTotalPages())
                .totalElements(applicationPage.getTotalElements())
                .pageSize(applicationPage.getSize())
                .build();
    }

    @Override
    public PageResponse<ApplicationResponse> getRecruiterApplications(
            String email,
            ApplicationFilterRequest request
    ) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Company company = companyRepository.findByRecruiter(recruiter)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        Pageable pageable = PaginationUtils.buildPageable(request);

        Specification<Application> specification = Specification
                .where(ApplicationSpecification.belongsToRecruiterCompany(company.getId()))
                .and(ApplicationSpecification.hasStatus(request.getStatus()));

        Page<Application> applicationPage = applicationRepository.findAll(specification, pageable);

        return PageResponse.<ApplicationResponse>builder()
                .content(applicationPage.getContent()
                        .stream()
                        .map(applicationMapper::toResponse)
                        .toList())
                .currentPage(applicationPage.getNumber())
                .totalPages(applicationPage.getTotalPages())
                .totalElements(applicationPage.getTotalElements())
                .pageSize(applicationPage.getSize())
                .build();
    }
    @Override
    @Transactional
    public ApplicationResponse updateApplicationStatus(
            String email,
            Long applicationId,
            UpdateApplicationStatusReques request
    ) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Company company = companyRepository.findByRecruiter(recruiter)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!application.getJobPost().getCompany().getId().equals(company.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        application.setStatus(request.getStatus());

        Application savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponse(savedApplication);
    }
}