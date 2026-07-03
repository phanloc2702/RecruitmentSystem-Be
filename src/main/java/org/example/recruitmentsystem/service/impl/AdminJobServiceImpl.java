package org.example.recruitmentsystem.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.AdminJobFilterRequest;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.NotificationType;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.JobPostMapper;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.service.AdminJobService;
import org.example.recruitmentsystem.service.NotificationService;
import org.example.recruitmentsystem.specification.JobPostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminJobServiceImpl implements AdminJobService {

    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final NotificationService notificationService;
    @Override
    public PageResponse<JobResponse> getJobs(AdminJobFilterRequest request) {
        Pageable pageable = PaginationUtils.buildPageable(request);

        Page<JobPost> page = jobPostRepository.findAll(
                JobPostSpecification.adminFilter(request),
                pageable
        );

        Page<JobResponse> responsePage =
                page.map(jobPostMapper::toResponse);

        return PageResponse.<JobResponse>builder()
                .content(responsePage.getContent())
                .currentPage(responsePage.getNumber())
                .totalPages(responsePage.getTotalPages())
                .totalElements(responsePage.getTotalElements())
                .pageSize(responsePage.getSize())
                .build();
    }

    @Override
    public JobResponse updateApprovalStatus(
            Long jobId,
            ApprovalStatus approvalStatus
    ) {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.JOB_NOT_FOUND)
                );

        jobPost.setApprovalStatus(approvalStatus);

        JobPost savedJob = jobPostRepository.save(jobPost);
        User recruiter = jobPost.getCompany().getRecruiter();

        if (jobPost.getApprovalStatus() == ApprovalStatus.APPROVED) {
            notificationService.createNotification(
                    recruiter,
                    NotificationType.JOB_APPROVED,
                    "Tin tuyển dụng đã được duyệt",
                    "Tin \"" + jobPost.getTitle() + "\" đã được admin duyệt.",
                    "/recruiter/jobs/" + jobPost.getId()
            );
        }

        if (jobPost.getApprovalStatus() == ApprovalStatus.REJECTED) {
            notificationService.createNotification(
                    recruiter,
                    NotificationType.JOB_REJECTED,
                    "Tin tuyển dụng bị từ chối",
                    "Tin \"" + jobPost.getTitle() + "\" đã bị admin từ chối.",
                    "/recruiter/jobs/" + jobPost.getId()
            );
        }

        return jobPostMapper.toResponse(savedJob);
    }
}