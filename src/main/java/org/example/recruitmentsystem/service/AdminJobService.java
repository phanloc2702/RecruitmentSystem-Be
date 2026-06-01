package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminJobFilterRequest;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;

public interface AdminJobService {

    PageResponse<JobResponse> getJobs(
            AdminJobFilterRequest request
    );

    JobResponse updateApprovalStatus(
            Long jobId,
            ApprovalStatus approvalStatus
    );
}