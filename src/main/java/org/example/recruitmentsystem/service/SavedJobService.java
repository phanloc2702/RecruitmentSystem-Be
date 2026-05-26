package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.SavedJobResponse;

public interface SavedJobService {

    PageResponse<SavedJobResponse> getMySavedJobs(
            String email,
            PaginationRequest request
    );

    void saveJob(
            String email,
            Long jobPostId
    );

    void unsaveJob(
            String email,
            Long jobPostId
    );
}