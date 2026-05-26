package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.ApplicationFilterRequest;
import org.example.recruitmentsystem.dto.request.ApplicationRequest;
import org.example.recruitmentsystem.dto.request.UpdateApplicationStatusReques;
import org.example.recruitmentsystem.dto.response.ApplicationResponse;

public interface ApplicationService {

    // CANDIDATE
    ApplicationResponse applyJob(
            String email,
            ApplicationRequest request
    );

    PageResponse<ApplicationResponse> getMyApplications(
            String email,
            ApplicationFilterRequest request
    );

    // RECRUITER
    PageResponse<ApplicationResponse> getRecruiterApplications(
            String email,
            ApplicationFilterRequest request
    );
    ApplicationResponse updateApplicationStatus(
            String email,
            Long applicationId,
            UpdateApplicationStatusReques request
    );
}