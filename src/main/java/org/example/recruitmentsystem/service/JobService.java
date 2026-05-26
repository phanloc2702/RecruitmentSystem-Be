package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.JobPostFilterRequest;
import org.example.recruitmentsystem.dto.request.RecruiterJobFilterRequest;
import org.example.recruitmentsystem.dto.request.RecruiterJobRequest;
import org.example.recruitmentsystem.dto.response.JobCategoryResponse;
import org.example.recruitmentsystem.dto.response.JobResponse;

import java.util.List;

public interface JobService {

    // PUBLIC
    PageResponse<JobResponse> getJobs(JobPostFilterRequest request);

    JobResponse getJobById(Long id);

    List<JobCategoryResponse> getCategories();

    // RECRUITER
    PageResponse<JobResponse> getRecruiterJobs(
            String email,
            RecruiterJobFilterRequest request
    );

    JobResponse getRecruiterJobById(
            String email,
            Long id
    );

    JobResponse createJob(
            String email,
            RecruiterJobRequest request
    );

    JobResponse updateJob(
            String email,
            Long id,
            RecruiterJobRequest request
    );

    void deleteJob(
            String email,
            Long id
    );
}